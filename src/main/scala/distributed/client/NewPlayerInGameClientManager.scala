package distributed.client

import java.util.concurrent.ConcurrentMap

import com.google.gson.GsonBuilder
import com.rabbitmq.client._
import distributed.Player
import distributed.deserializers.UserMessageDeserializer
import distributed.messages.UserMessageImpl
import utilities.Settings

trait NewPlayerInGameClientManager{
  def receiveNewPlayerInGame(userId: Int, connectedPlayers: ConcurrentMap[Int, Player]): Unit
}

object NewPlayerInGameClientManagerImpl {
  def apply(connection: Connection): NewPlayerInGameClientManager =
    new NewPlayerInGameClientManagerImpl(connection)
}

class NewPlayerInGameClientManagerImpl(private val connection: Connection) extends NewPlayerInGameClientManager{

  private var channel: Channel = connection.createChannel()
  private val playerQueue = channel.queueDeclare.getQueue

  channel.exchangeDeclare(Settings.NEW_PLAYER_EXCHANGE, "fanout")
  channel.queueBind(playerQueue, Settings.NEW_PLAYER_EXCHANGE, "")

  override def receiveNewPlayerInGame(userId: Int, connectedPlayers: ConcurrentMap[Int, Player]): Unit = {

    val consumer = new DefaultConsumer(channel) {

      override def handleDelivery(consumerTag: String,
                                  envelope: Envelope,
                                  properties: AMQP.BasicProperties,
                                  body: Array[Byte]) {
        println(" [x] Received other player in game")
        val message = new String(body, "UTF-8")
        val gson = new GsonBuilder().registerTypeAdapter(classOf[UserMessageImpl], UserMessageDeserializer).create()
        val otherPlayer = gson.fromJson(message, classOf[UserMessageImpl])

        if (otherPlayer.user.userId != userId) connectedPlayers.put(otherPlayer.user.userId, otherPlayer.user)
      }

    }

    channel.basicConsume(playerQueue, true, consumer)
  }
}