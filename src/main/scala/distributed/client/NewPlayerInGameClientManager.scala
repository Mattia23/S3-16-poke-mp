package distributed.client

import java.util.concurrent.ConcurrentMap

import com.google.gson.{Gson, GsonBuilder}
import com.rabbitmq.client.{AMQP, Channel, DefaultConsumer, Envelope}
import distributed.deserializers.UserMessageDeserializer
import distributed.messages.UserMessageImpl
import distributed.{CommunicationManager, DistributedConnectionImpl, User}
import utilities.Settings

object NewPlayerInGameClientManager {
  def apply(userId: Int, connectedUsers: ConcurrentMap[Int, User]): CommunicationManager = new NewPlayerInGameClientManager(userId, connectedUsers)
}

class NewPlayerInGameClientManager(private val userId: Int, private val connectedUsers: ConcurrentMap[Int, User]) extends CommunicationManager{

  private var gson: Gson = _
  private var channel: Channel = _

  override def start(): Unit = {
    channel = DistributedConnectionImpl().connection.createChannel()
    val userQueue = channel.queueDeclare.getQueue

    channel.exchangeDeclare(Settings.NEW_PLAYER_EXCHANGE, "fanout")
    channel.queueBind(userQueue, Settings.NEW_PLAYER_EXCHANGE, "")

    val consumer = new DefaultConsumer(channel) {

      override def handleDelivery(consumerTag: String,
                                  envelope: Envelope,
                                  properties: AMQP.BasicProperties,
                                  body: Array[Byte]) {
        println(" [x] Received other player in game")
        val message = new String(body, "UTF-8")
        gson = new GsonBuilder().registerTypeAdapter(classOf[UserMessageImpl], UserMessageDeserializer).create()
        val otherPlayer = gson.fromJson(message, classOf[UserMessageImpl])

        if (otherPlayer.user.userId != userId) connectedUsers.put(otherPlayer.user.userId, otherPlayer.user)

      }
    }

    channel.basicConsume(userQueue, true, consumer)
  }
}