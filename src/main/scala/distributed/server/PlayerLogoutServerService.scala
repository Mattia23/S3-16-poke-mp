package distributed.server

import java.util.concurrent.ConcurrentMap

import com.google.gson.{Gson, GsonBuilder}
import com.rabbitmq.client._
import distributed.deserializers.PlayerPositionMessageDeserializer
import distributed.messages.{PlayerPositionMessageImpl, UserLogoutMessageImpl}
import distributed.{CommunicationService, User}
import utilities.Settings

object PlayerLogoutServerService {
  def apply(connection: Connection, connectedUsers: ConcurrentMap[Int, User]): CommunicationService = new PlayerLogoutServerService(connection, connectedUsers)
}

class PlayerLogoutServerService(private val connection: Connection, private val connectedUsers: ConcurrentMap[Int, User]) extends CommunicationService{
  override def start(): Unit = {
    val channel: Channel = connection.createChannel
    channel.queueDeclare(Settings.PLAYER_LOGOUT_CHANNEL_QUEUE, false, false, false, null)

    val consumer = new DefaultConsumer(channel) {

      override def handleDelivery(consumerTag: String,
                                  envelope: Envelope,
                                  properties: AMQP.BasicProperties,
                                  body: Array[Byte]): Unit = {
        println("server: received player logout")
        val gson = new Gson()
        val logoutMessage = gson.fromJson(new String(body, "UTF-8"), classOf[UserLogoutMessageImpl])

        connectedUsers.remove(logoutMessage.userId)
        //connectedUsers.values() forEach (user => println(""+user.userId+ " "+user.position.x+" "+user.position.y))

        channel.exchangeDeclare(Settings.PLAYER_LOGOUT_EXCHANGE, "fanout")
        val response = gson.toJson(logoutMessage)
        channel.basicPublish(Settings.PLAYER_LOGOUT_EXCHANGE, "", null, response.getBytes("UTF-8"))
        println("server: send player logout")
      }
    }

    channel.basicConsume(Settings.PLAYER_LOGOUT_CHANNEL_QUEUE, true, consumer)
  }
}
