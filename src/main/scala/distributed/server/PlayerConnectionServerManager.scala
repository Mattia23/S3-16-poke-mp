package distributed.server

import java.util
import java.util.concurrent.{ConcurrentHashMap, ConcurrentMap}

import com.google.gson.reflect.TypeToken
import com.google.gson.{Gson, GsonBuilder}
import com.rabbitmq.client._
import distributed._
import distributed.deserializers.UserMessageDeserializer
import distributed.messages.{ConnectedUsersMessage, UserMessage, UserMessageImpl}
import utilities.Settings

object PlayerConnectionServerManager {
  def apply(connectedUsers: ConcurrentMap[Int, User]): CommunicationManager = new PlayerConnectionServerManager(connectedUsers)
}

class PlayerConnectionServerManager(private val connectedUsers: ConcurrentMap[Int, User]) extends CommunicationManager {
  override def start(): Unit = {
    val channel: Channel = DistributedConnectionImpl().connection.createChannel
    channel.queueDeclare(Settings.PLAYER_CONNECTION_CHANNEL_QUEUE, false, false, false, null)

    val consumer = new DefaultConsumer(channel) {

      override def handleDelivery(consumerTag: String,
                                  envelope: Envelope,
                                  properties: AMQP.BasicProperties,
                                  body: Array[Byte]): Unit = {
        println("server: received")
        println(new String(body, "UTF-8"))
        val gson = new GsonBuilder().registerTypeAdapter(classOf[UserMessageImpl], UserMessageDeserializer).create()
        val user = gson.fromJson(new String(body, "UTF-8"), classOf[UserMessageImpl]).user

        val response = gson.toJson(ConnectedUsersMessage(connectedUsers))
        channel.basicPublish("", Settings.PLAYERS_CONNECTED_CHANNEL_QUEUE + user.userId, null, response.getBytes("UTF-8"))
        println("server: send")
        connectedUsers.put(user.userId, user)
      }
    }

    channel.basicConsume(Settings.PLAYER_CONNECTION_CHANNEL_QUEUE, true, consumer)
  }
}
