package distributed.client

import com.google.gson.{Gson, GsonBuilder}
import com.rabbitmq.client._
import distributed._
import distributed.deserializers.ConnectedPlayersMessageDeserializer
import distributed.messages.{ConnectedPlayersMessageImpl, PlayerMessage}
import utilities.Settings

trait PlayerLoginClientManager{
  def sendPlayer(player: Player): Unit

  def receivePlayersConnected(userId: Int, connectedPlayers: ConnectedPlayers): Unit
}

object PlayerLoginClientManager {
  def apply(connection: Connection): PlayerLoginClientManager = new PlayerLoginClientManagerImpl(connection)
}

class PlayerLoginClientManagerImpl(private val connection: Connection) extends PlayerLoginClientManager {

  private var gson: Gson = new Gson()
  private val channel: Channel = connection.createChannel()

  import Settings._
  channel.queueDeclare(Constants.PLAYER_LOGIN_CHANNEL_QUEUE, false, false, false, null)

  override def sendPlayer(player: Player): Unit = {
    val playerMessage = PlayerMessage(player)
    channel.basicPublish("", Constants.PLAYER_LOGIN_CHANNEL_QUEUE, null, gson.toJson(playerMessage).getBytes("UTF-8"))
    println(" [x] Sent message")
  }

  override def receivePlayersConnected(userId: Int, connectedPlayers: ConnectedPlayers): Unit = {
    val playerQueue = Constants.PLAYERS_CONNECTED_CHANNEL_QUEUE + userId
    channel.queueDeclare(playerQueue, false, false, false, null)

    val consumer = new DefaultConsumer(channel) {

      override def handleDelivery(consumerTag: String,
                                  envelope: Envelope,
                                  properties: AMQP.BasicProperties,
                                  body: Array[Byte]) {
        println(" [x] Received message")
        val message = new String(body, "UTF-8")
        gson = new GsonBuilder().registerTypeAdapter(classOf[ConnectedPlayersMessageImpl], ConnectedPlayersMessageDeserializer).create()
        val serverPlayersMessage = gson.fromJson(message, classOf[ConnectedPlayersMessageImpl])
        connectedPlayers.addAll(serverPlayersMessage.connectedPlayers)

        channel.close()
      }
    }
    channel.basicConsume(playerQueue, true, consumer)
  }

}
