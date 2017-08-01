package distributed.client

import com.google.gson.GsonBuilder
import com.rabbitmq.client._
import distributed.ConnectedPlayers
import distributed.deserializers.PlayerMessageDeserializer
import distributed.messages.PlayerMessageImpl
import utilities.Settings

trait NewPlayerInGameClientManager{
  def receiveNewPlayerInGame(userId: Int, connectedPlayers: ConnectedPlayers): Unit
}

object NewPlayerInGameClientManager {
  def apply(connection: Connection): NewPlayerInGameClientManager =
    new NewPlayerInGameClientManagerImpl(connection)
}

class NewPlayerInGameClientManagerImpl(private val connection: Connection) extends NewPlayerInGameClientManager{

  private val channel: Channel = connection.createChannel()
  private val playerQueue = channel.queueDeclare.getQueue

  import Settings._
  channel.exchangeDeclare(Constants.NEW_PLAYER_EXCHANGE, "fanout")
  channel.queueBind(playerQueue, Constants.NEW_PLAYER_EXCHANGE, "")

  override def receiveNewPlayerInGame(userId: Int, connectedPlayers: ConnectedPlayers): Unit = {

    val consumer = new DefaultConsumer(channel) {

      override def handleDelivery(consumerTag: String,
                                  envelope: Envelope,
                                  properties: AMQP.BasicProperties,
                                  body: Array[Byte]) {
        println(" [x] Received other player in game")
        val message = new String(body, "UTF-8")
        val gson = new GsonBuilder().registerTypeAdapter(classOf[PlayerMessageImpl], PlayerMessageDeserializer).create()
        val otherPlayer = gson.fromJson(message, classOf[PlayerMessageImpl])

        if (otherPlayer.player.userId != userId) connectedPlayers.add(otherPlayer.player.userId, otherPlayer.player)
      }

    }

    channel.basicConsume(playerQueue, true, consumer)
  }
}