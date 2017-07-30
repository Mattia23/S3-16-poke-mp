package distributed.client

import com.google.gson.Gson
import com.rabbitmq.client._
import controller.DistributedBattleController
import distributed.messages.{BattleMessage, BattleMessageImpl}

trait BattleClientManager {
  def sendBattleMessage(trainerId: Int, pokemonId: Int, attackId: Int): Unit

  def receiveBattleMessage(): Unit
}

object BattleClientManager {
  def apply(connection: Connection, myPlayerId: Int, otherPlayerId: Int, controller: DistributedBattleController): BattleClientManager =
    new BattleClientManagerImpl(connection, myPlayerId, otherPlayerId, controller)
}

class BattleClientManagerImpl(private val connection: Connection,
                              private val myPlayerId: Int,
                              private val otherPlayerId: Int,
                              private val controller: DistributedBattleController) extends BattleClientManager {

  private val gson: Gson = new Gson()
  private val channel: Channel = connection.createChannel()
  private val myChannelName: String = "battle" + myPlayerId
  private val otherChannelName: String = "battle" + otherPlayerId

  channel.queueDeclare(myChannelName, false, false, false, null)
  channel.queueDeclare(otherChannelName, false, false, false, null)

  override def sendBattleMessage(trainerId: Int, pokemonId: Int, attackId: Int): Unit = {
    val battleMessage = BattleMessage(trainerId,pokemonId,attackId)
    channel.basicPublish("", myChannelName, null, gson.toJson(battleMessage).getBytes("UTF-8"))
  }

  override def receiveBattleMessage(): Unit = {
    val consumer = new DefaultConsumer(channel) {

      override def handleDelivery(consumerTag: String,
                                  envelope: Envelope,
                                  properties: AMQP.BasicProperties,
                                  body: Array[Byte]) {

        val battleMessage = gson.fromJson(new String(body, "UTF-8"), classOf[BattleMessageImpl])
        if(battleMessage.attackId == 0){
          if(battleMessage.pokemonId == 0){
            controller.resumeGame()
          } else {
            controller.otherPokemonChanges(battleMessage.pokemonId)
          }
        } else {
          controller.otherPokemonAttacks(battleMessage.attackId)
        }
      }
    }
    channel.basicConsume(otherChannelName,true,consumer)
  }
}
