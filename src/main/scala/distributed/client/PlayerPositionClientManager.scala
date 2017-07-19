package distributed.client

import java.util.concurrent.ConcurrentHashMap

import com.google.gson.{Gson, GsonBuilder}
import com.rabbitmq.client.{AMQP, Channel, DefaultConsumer, Envelope}
import distributed.deserializers.ConnectedUsersDeserializer
import distributed.{DistributedConnectionImpl, User}
import distributed.messages.{PlayerPositionMessage, PlayerPositionMessageDeserializer, PlayerPositionMessageImpl}
import model.environment.Coordinate
import utilities.Settings

trait PlayerPositionClientManager{
  def sendPlayerPosition(userId: Int, position: Coordinate): Unit

  def receiveOtherPlayerPosition(userId: Int, connectedUsers: ConcurrentHashMap[Int, User]): Unit
}

object PlayerPositionClientManagerImpl {
  def apply(): PlayerPositionClientManager = new PlayerPositionClientManagerImpl()
}

class PlayerPositionClientManagerImpl extends PlayerPositionClientManager{

  private var gson: Gson = new Gson()
  private var channel: Channel = _

  channel = DistributedConnectionImpl().connection.createChannel()
  channel.queueDeclare(Settings.PLAYER_POSITION_CHANNEL_QUEUE, false, false, false, null)

  override def sendPlayerPosition(userId: Int, position: Coordinate): Unit = {
    val playerPositionMessage = PlayerPositionMessageImpl(userId, position)
    channel.basicPublish("", Settings.PLAYER_POSITION_CHANNEL_QUEUE, null, gson.toJson(playerPositionMessage).getBytes("UTF-8"))
    println(" [x] Sent message")
  }

  override def receiveOtherPlayerPosition(userId: Int, connectedUsers: ConcurrentHashMap[Int, User]): Unit = {
    val userQueue = Settings.OTHER_PLAYER_POSITION_CHANNEL_QUEUE + userId
    channel.queueDeclare(Settings.OTHER_PLAYER_POSITION_CHANNEL_QUEUE, false, false, false, null)
    channel.queueDeclare(userQueue, false, false, false, null)

    val consumer = new DefaultConsumer(channel) {

      override def handleDelivery(consumerTag: String,
                                  envelope: Envelope,
                                  properties: AMQP.BasicProperties,
                                  body: Array[Byte]) {
        println(" [x] Received message")
        val message = new String(body, "UTF-8")
        gson = new GsonBuilder().registerTypeAdapter(classOf[PlayerPositionMessageImpl], PlayerPositionMessageDeserializer).create()
        val otherPlayerPosition = gson.fromJson(message, classOf[PlayerPositionMessageImpl])
        /* TODO Aggiornare posizione del player in ConnectedUser e lo sprite in usersTrainerSprite in base alla direzione,
        * fare il movimento sulla mappa, fare la receive in DistributedAgent, executor?
        */

        channel.close()
      }
    }
    channel.basicConsume(userQueue, true, consumer)
  }
}
