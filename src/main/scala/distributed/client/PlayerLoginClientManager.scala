package distributed.client

import java.util.concurrent.ConcurrentHashMap

import com.google.gson.{Gson, GsonBuilder}
import com.rabbitmq.client._
import distributed._
import distributed.deserializers.ConnectedUsersMessageDeserializer
import distributed.messages.{ConnectedUsersMessageImpl, UserMessage}
import model.environment.Coordinate
import utilities.Settings

trait PlayerLoginClientManager{
  def sendPlayerInformation(userId: Int, username: String, sprites: Int, position: Coordinate): Unit

  def receivePlayersConnected(userId: Int, connectedPlayers: ConcurrentHashMap[Int, Player]): Unit
}

object PlayerLoginClientManagerImpl {
  def apply(connection: Connection): PlayerLoginClientManager = new PlayerLoginClientManagerImpl(connection)
}

class PlayerLoginClientManagerImpl(private val connection: Connection) extends PlayerLoginClientManager {

  private var gson: Gson = new Gson()
  private var channel: Channel = connection.createChannel()

  channel.queueDeclare(Settings.PLAYER_CONNECTION_CHANNEL_QUEUE, false, false, false, null)

  override def sendPlayerInformation(userId: Int, username: String, sprites: Int, position: Coordinate): Unit = {
    val user = Player(userId, username, sprites, position)
    val userMessage = UserMessage(user)
    channel.basicPublish("", Settings.PLAYER_CONNECTION_CHANNEL_QUEUE, null, gson.toJson(userMessage).getBytes("UTF-8"))
    println(" [x] Sent message")
  }

  override def receivePlayersConnected(userId: Int, connectedPlayers: ConcurrentHashMap[Int, Player]): Unit = {
    val playerQueue = Settings.PLAYERS_CONNECTED_CHANNEL_QUEUE + userId
    channel.queueDeclare(playerQueue, false, false, false, null)

    val consumer = new DefaultConsumer(channel) {

      override def handleDelivery(consumerTag: String,
                                  envelope: Envelope,
                                  properties: AMQP.BasicProperties,
                                  body: Array[Byte]) {
        println(" [x] Received message")
        val message = new String(body, "UTF-8")
        gson = new GsonBuilder().registerTypeAdapter(classOf[ConnectedUsersMessageImpl], ConnectedUsersMessageDeserializer).create()
        val serverUsersMessage = gson.fromJson(message, classOf[ConnectedUsersMessageImpl])
        connectedPlayers.putAll(serverUsersMessage.connectedUsers)
        //connectedUsers.values() forEach (user => println(""+user.userId+ " "+user.username))

        channel.close()
      }
    }
    channel.basicConsume(playerQueue, true, consumer)
  }

}
