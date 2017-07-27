package distributed.client

import java.util

import com.google.gson.{Gson, GsonBuilder}
import com.rabbitmq.client._
import controller.{GameController}
import distributed.Player
import distributed.deserializers.TrainerDialogueMessageDeserializers
import distributed.messages.{TrainerDialogueMessage, TrainerDialogueMessageImpl}
import utilities.Settings
import view.{ClassicDialoguePanel, TrainerDialoguePanel}

trait TrainerDialogueClientManager {
  def player: Player

  def player_=(otherTrainer: Player): Unit

  def otherPlayer: Player

  def otherPlayer_=(otherTrainer: Player): Unit

  def sendDialogueRequest(player: Player, otherPlayer: Player, wantToFight: Boolean): Unit

  def receiveResponse(): Unit
}

object TrainerDialogueClientManager {
  def apply(connection: Connection, mapController: GameController): TrainerDialogueClientManager = new TrainerDialogueClientManagerImpl(connection, mapController)
}

class TrainerDialogueClientManagerImpl(private val connection: Connection, private val mapController: GameController) extends TrainerDialogueClientManager {

  private val trainerId: Int = mapController.trainer.id
  private var gson: Gson = new Gson()
  private val channel: Channel = connection.createChannel()
  private val playerQueue = Settings.TRAINER_DIALOGUE_CHANNEL_QUEUE + trainerId
  override var player: Player = _
  override var otherPlayer: Player = _

  channel.queueDeclare(Settings.TRAINER_DIALOGUE_CHANNEL_QUEUE + trainerId, false, false, false, null)

  override def sendDialogueRequest(player: Player, otherPlayer: Player, wantToFight: Boolean): Unit = {
    this.player = player
    channel.queueDeclare(Settings.TRAINER_DIALOGUE_CHANNEL_QUEUE + otherPlayer.userId, false, false, false, null)
    val trainerDialogueMessage = TrainerDialogueMessage(player, otherPlayer, wantToFight)
    channel.basicPublish("", Settings.TRAINER_DIALOGUE_CHANNEL_QUEUE + otherPlayer.userId, null, gson.toJson(trainerDialogueMessage).getBytes("UTF-8"))
    println(" [x] sent fight request to "+otherPlayer.userId)
  }

  override def receiveResponse(): Unit = {

    val consumer = new DefaultConsumer(channel) {

      override def handleDelivery(consumerTag: String,
                                  envelope: Envelope,
                                  properties: AMQP.BasicProperties,
                                  body: Array[Byte]) {
        println(" [x] Received fight response")
        val gson = new GsonBuilder().registerTypeAdapter(classOf[TrainerDialogueMessageImpl], TrainerDialogueMessageDeserializers).create()
        val trainerDialogueMessage = gson.fromJson(new String(body, "UTF-8"), classOf[TrainerDialogueMessageImpl])
        otherPlayer = trainerDialogueMessage.player
        if(trainerDialogueMessage.wantToFight && !trainerDialogueMessage.otherPlayer.isFighting) {
          println("mi hanno sfidato")
          trainerDialogueMessage.otherPlayer.isFighting_=(true)
          mapController.showDialogue(new TrainerDialoguePanel(mapController, TrainerDialogueClientManagerImpl.this, util.Arrays.asList(trainerDialogueMessage.otherPlayer.userId + " ti ha sfidato")))
        }
        else if(trainerDialogueMessage.wantToFight){
          //TODO parte la sfida
          println("hanno accettato la sfida, si parteeeeee")
        }
        else if(!trainerDialogueMessage.wantToFight){
          mapController.showDialogue(new ClassicDialoguePanel(mapController, util.Arrays.asList(otherPlayer.userId + " ha rifiutato la sfida, stronzommerda")))
        }
        //channel.close()
      }
    }
    channel.basicConsume(playerQueue, true, consumer)
  }
}
