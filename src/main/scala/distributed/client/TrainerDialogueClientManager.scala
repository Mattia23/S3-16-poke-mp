package distributed.client

import java.util

import com.google.gson.{Gson, GsonBuilder}
import com.rabbitmq.client._
import controller.{BattleController, DistributedBattleControllerImpl, GameController}
import distributed.Player
import view.View
//import distributed.deserializers.TrainerDialogueMessageDeserializers
import distributed.messages.{TrainerDialogueMessage, TrainerDialogueMessageImpl}
import utilities.Settings
import view.{ClassicDialoguePanel, TrainerDialoguePanel}

trait TrainerDialogueClientManager {
  def playerId: Int

  def playerId_=(playerId: Int): Unit

  def otherPlayerId: Int

  def otherPlayerId_=(otherPlayerId: Int): Unit

  def sendDialogueRequest(otherPlayerId: Int, wantToFight: Boolean, isFirst: Boolean): Unit

  def receiveResponse(): Unit

  def createBattle(): Unit
}

object TrainerDialogueClientManager {
  def apply(connection: Connection, mapController: GameController): TrainerDialogueClientManager = new TrainerDialogueClientManagerImpl(connection, mapController)
}

class TrainerDialogueClientManagerImpl(private val connection: Connection, private val mapController: GameController) extends TrainerDialogueClientManager {

  private val gson: Gson = new Gson()
  private val channel: Channel = connection.createChannel()
  override var playerId: Int = mapController.trainer.id
  override var otherPlayerId: Int = _
  var otherPlayerName: String = _
  private val playerQueue = Settings.TRAINER_DIALOGUE_CHANNEL_QUEUE + playerId
  private var yourPlayerIsFirst = true

  channel.queueDeclare(playerQueue, false, false, false, null)

  override def sendDialogueRequest(otherPlayerId: Int, wantToFight: Boolean, isFirst: Boolean): Unit = {
    mapController.sendPlayerIsFighting(wantToFight)
    yourPlayerIsFirst = isFirst
    val trainerDialogueMessage = TrainerDialogueMessage(playerId, mapController.trainer.username, otherPlayerId, wantToFight, isFirst)
    channel.queueDeclare(Settings.TRAINER_DIALOGUE_CHANNEL_QUEUE + otherPlayerId, false, false, false, null)
    channel.basicPublish("", Settings.TRAINER_DIALOGUE_CHANNEL_QUEUE + otherPlayerId, null, gson.toJson(trainerDialogueMessage).getBytes("UTF-8"))
  }

  override def receiveResponse(): Unit = {

    val consumer = new DefaultConsumer(channel) {

      override def handleDelivery(consumerTag: String,
                                  envelope: Envelope,
                                  properties: AMQP.BasicProperties,
                                  body: Array[Byte]) {

        val trainerDialogueMessage = gson.fromJson(new String(body, "UTF-8"), classOf[TrainerDialogueMessageImpl])
        otherPlayerId = trainerDialogueMessage.firstPlayerId
        otherPlayerName = trainerDialogueMessage.firstPlayerName
        if(trainerDialogueMessage.wantToFight && trainerDialogueMessage.isFirst) {
          mapController.sendPlayerIsFighting(true)
          yourPlayerIsFirst = false
          mapController.showDialogue(new TrainerDialoguePanel(mapController, TrainerDialogueClientManagerImpl.this,
            util.Arrays.asList(otherPlayerName + " ti ha sfidato")))
        }
        else if(trainerDialogueMessage.wantToFight){
          createBattle()
        }
        else if(!trainerDialogueMessage.wantToFight){
          mapController.sendPlayerIsFighting(false)
          mapController.hideCurrentDialogue()
          mapController.showDialogue(new ClassicDialoguePanel(mapController, util.Arrays.asList(otherPlayerName + " ha rifiutato la sfida :(")))
        }
        //channel.close()
      }
    }
    channel.basicConsume(playerQueue, true, consumer)
  }

  override def createBattle(): Unit = {
    mapController.createDistributedBattle(otherPlayerId, yourPlayerIsFirst)
  }
}
