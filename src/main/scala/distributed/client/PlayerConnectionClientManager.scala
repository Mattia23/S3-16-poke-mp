package distributed.client

import java.util.concurrent.ConcurrentHashMap
import com.google.gson.{Gson, GsonBuilder}
import com.rabbitmq.client._
import distributed._
import distributed.deserializers.ConnectedUsersDeserializer
import model.environment.Coordinate
import utilities.Settings

trait PlayerConnectionClientManager{
  def sendUserInformation(userId: Int, username: String, sprites: Int, position: Coordinate): Unit

  def receivePlayersConnected(userId: Int, connectedUsers: ConcurrentHashMap[Int, User]): Unit
}

object PlayerConnectionClientManagerImpl {
  def apply(): PlayerConnectionClientManager = new PlayerConnectionClientManagerImpl()
}

class PlayerConnectionClientManagerImpl extends PlayerConnectionClientManager {

  private var gson: Gson = new Gson()
  private var channel: Channel = _

  channel = DistributedConnectionImpl().connection.createChannel()
  channel.queueDeclare(Settings.PLAYER_CONNECTION_CHANNEL_QUEUE, false, false, false, null)


  override def sendUserInformation(userId: Int, username: String, sprites: Int, position: Coordinate): Unit = {
    val user = User(userId, username, sprites, position)
    channel.basicPublish("", Settings.PLAYER_CONNECTION_CHANNEL_QUEUE, null, gson.toJson(user).getBytes("UTF-8"))
    println(" [x] Sent message")
  }

  override def receivePlayersConnected(userId: Int, connectedUsers: ConcurrentHashMap[Int, User]): Unit = {
    val userQueue = Settings.PLAYERS_CONNECTED_CHANNEL_QUEUE + userId
    channel.queueDeclare(userQueue, false, false, false, null)

    val consumer = new DefaultConsumer(channel) {

      override def handleDelivery(consumerTag: String,
                                  envelope: Envelope,
                                  properties: AMQP.BasicProperties,
                                  body: Array[Byte]) {
        println(" [x] Received message")
        val message = new String(body, "UTF-8")
        gson = new GsonBuilder().registerTypeAdapter(classOf[ConcurrentHashMap[Int, User]], ConnectedUsersDeserializer).create()
        val serverUsers = gson.fromJson(message, classOf[ConcurrentHashMap[Int, User]])
        connectedUsers.putAll(serverUsers)
        connectedUsers.values() forEach (user => println(""+user.userId+ " "+user.username))

        channel.close()
      }
    }
    channel.basicConsume(userQueue, true, consumer)
  }

}
