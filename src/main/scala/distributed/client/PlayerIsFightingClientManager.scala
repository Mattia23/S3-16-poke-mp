package distributed.client

import java.util.concurrent.ConcurrentMap

import com.google.gson.Gson
import com.rabbitmq.client._
import distributed.{ConnectedPlayers, Player}
import distributed.messages.{PlayerIsFightingMessage, PlayerIsFightingMessageImpl}
import utilities.Settings

trait PlayerIsFightingClientManager {
  def sendPlayerIsFighting(userId: Int, isInBuilding: Boolean): Unit

  def receiveOtherPlayerIsFighting(userId: Int, connectedPlayers: ConnectedPlayers): Unit
}

object PlayerIsFightingClientManager {
  def apply(connection: Connection): PlayerIsFightingClientManager = new PlayerIsFightingClientManagerImpl(connection)
}

class PlayerIsFightingClientManagerImpl(private val connection: Connection) extends PlayerIsFightingClientManager {

  private val gson: Gson = new Gson()
  private val channel: Channel = connection.createChannel()

  private val playerQueue = channel.queueDeclare.getQueue

  channel.queueDeclare(Settings.PLAYER_IS_FIGHTING_CHANNEL_QUEUE, false, false, false, null)

  channel.exchangeDeclare(Settings.PLAYER_IS_FIGHTING_EXCHANGE, "fanout")
  channel.queueBind(playerQueue, Settings.PLAYER_IS_FIGHTING_EXCHANGE, "")

  override def sendPlayerIsFighting(userId: Int, isInBuilding: Boolean): Unit = {
    val playerIsFightingMessage = PlayerIsFightingMessage(userId, isInBuilding)
    channel.basicPublish("", Settings.PLAYER_IS_FIGHTING_CHANNEL_QUEUE, null, gson.toJson(playerIsFightingMessage).getBytes("UTF-8"))
    println(" [x] Sent player is fighting message")
  }

  override def receiveOtherPlayerIsFighting(userId: Int, connectedPlayers: ConnectedPlayers): Unit = {
    val consumer = new DefaultConsumer(channel) {

      override def handleDelivery(consumerTag: String,
                                  envelope: Envelope,
                                  properties: AMQP.BasicProperties,
                                  body: Array[Byte]) {
        println(" [x] Received other player is fighting")
        val playerIsFightingMessage = gson.fromJson(new String(body, "UTF-8"), classOf[PlayerIsFightingMessageImpl])

        if (playerIsFightingMessage.userId != userId)
          connectedPlayers.get(playerIsFightingMessage.userId).isFighting = playerIsFightingMessage.isFighting
      }

    }

    channel.basicConsume(playerQueue, true, consumer)
  }
}
