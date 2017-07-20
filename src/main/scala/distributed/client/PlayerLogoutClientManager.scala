package distributed.client

import java.util.concurrent.ConcurrentHashMap

import com.google.gson.Gson
import com.rabbitmq.client._
import distributed.Player
import distributed.messages.{PlayerLogoutMessage, PlayerLogoutMessageImpl}
import utilities.Settings

trait PlayerLogoutClientManager {
  def sendPlayerLogout(userId: Int): Unit

  def receiveOtherPlayerLogout(userId: Int, connectedPlayers: ConcurrentHashMap[Int, Player]): Unit
}

object PlayerLogoutClientManager {
  def apply(connection: Connection): PlayerLogoutClientManager = new PlayerLogoutClientManagerImpl(connection)
}

class PlayerLogoutClientManagerImpl(private val connection: Connection) extends PlayerLogoutClientManager {

  private val gson: Gson = new Gson()
  private val channel: Channel = connection.createChannel()
  private val playerQueue = channel.queueDeclare.getQueue

  channel.queueDeclare(Settings.PLAYER_LOGOUT_CHANNEL_QUEUE, false, false, false, null)

  channel.exchangeDeclare(Settings.PLAYER_LOGOUT_EXCHANGE, "fanout")
  channel.queueBind(playerQueue, Settings.PLAYER_LOGOUT_EXCHANGE, "")

  override def sendPlayerLogout(userId: Int): Unit = {
    val logoutMessage = PlayerLogoutMessage(userId)
    channel.basicPublish("", Settings.PLAYER_LOGOUT_CHANNEL_QUEUE, null, gson.toJson(logoutMessage).getBytes("UTF-8"))
    println(" [x] Sent logout message")
  }

  override def receiveOtherPlayerLogout(userId: Int, connectedPlayers: ConcurrentHashMap[Int, Player]): Unit = {
    val consumer = new DefaultConsumer(channel) {

      override def handleDelivery(consumerTag: String,
                                  envelope: Envelope,
                                  properties: AMQP.BasicProperties,
                                  body: Array[Byte]) {
        println(" [x] Received other player logout")
        val message = new String(body, "UTF-8")
        val logoutMessage = gson.fromJson(new String(body, "UTF-8"), classOf[PlayerLogoutMessageImpl])

        if (logoutMessage.userId != userId) connectedPlayers.remove(logoutMessage.userId)

      }

    }

    channel.basicConsume(playerQueue, true, consumer)
  }
}
