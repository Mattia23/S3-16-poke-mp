package distributed.client

import java.util.concurrent.ConcurrentMap

import com.google.gson.{Gson, GsonBuilder}
import com.rabbitmq.client._
import distributed.deserializers.PlayerMessageDeserializer
import distributed.messages.PlayerMessageImpl
import distributed.{CommunicationService, DistributedConnection, DistributedConnectionImpl, User}
import utilities.Settings

object NewPlayerInGameClientManager {
  def apply(connection: Connection, userId: Int, connectedUsers: ConcurrentMap[Int, User]): CommunicationService =
    new NewPlayerInGameClientManager(connection, userId, connectedUsers)
}

class NewPlayerInGameClientManager(private val connection: Connection, private val userId: Int, private val connectedUsers: ConcurrentMap[Int, User]) extends CommunicationService{

  private var gson: Gson = _
  private var channel: Channel = _

  override def start(): Unit = {
    channel = connection.createChannel()
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
        gson = new GsonBuilder().registerTypeAdapter(classOf[PlayerMessageImpl], PlayerMessageDeserializer).create()
        val otherPlayer = gson.fromJson(message, classOf[PlayerMessageImpl])

        if (otherPlayer.player.userId != userId) connectedUsers.put(otherPlayer.player.userId, otherPlayer.player)

      }
    }

    channel.basicConsume(userQueue, true, consumer)
  }
}