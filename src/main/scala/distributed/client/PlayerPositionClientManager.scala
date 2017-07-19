package distributed.client

import java.util.concurrent.ConcurrentMap

import com.google.gson.{Gson, GsonBuilder}
import com.rabbitmq.client.{AMQP, Channel, DefaultConsumer, Envelope}
import distributed.deserializers.PlayerPositionMessageDeserializer
import distributed.messages.PlayerPositionMessageImpl
import distributed.{DistributedConnectionImpl, User}
import model.environment.Coordinate
import utilities.Settings

trait PlayerPositionClientManager{
  def sendPlayerPosition(userId: Int, position: Coordinate): Unit

  def receiveOtherPlayerPosition(connectedUsers: ConcurrentMap[Int, User]): Unit
}

object PlayerPositionClientManagerImpl {
  def apply(): PlayerPositionClientManager = new PlayerPositionClientManagerImpl()
}

class PlayerPositionClientManagerImpl extends PlayerPositionClientManager{

  private var gson: Gson = new Gson()
  private var channel: Channel = _

  channel = DistributedConnectionImpl().connection.createChannel()

  override def sendPlayerPosition(userId: Int, position: Coordinate): Unit = {
    channel.queueDeclare(Settings.PLAYER_POSITION_CHANNEL_QUEUE, false, false, false, null)
    val playerPositionMessage = PlayerPositionMessageImpl(userId, position)
    channel.basicPublish("", Settings.PLAYER_POSITION_CHANNEL_QUEUE, null, gson.toJson(playerPositionMessage).getBytes("UTF-8"))
    println(" [x] Sent message")
  }

  override def receiveOtherPlayerPosition(connectedUsers: ConcurrentMap[Int, User]): Unit = {

    channel.exchangeDeclare(Settings.PLAYER_POSITION_EXCHANGE, "fanout")
    val userQueue = channel.queueDeclare.getQueue
    channel.queueBind(userQueue, Settings.PLAYER_POSITION_EXCHANGE, "")

    val consumer = new DefaultConsumer(channel) {

      override def handleDelivery(consumerTag: String,
                                  envelope: Envelope,
                                  properties: AMQP.BasicProperties,
                                  body: Array[Byte]) {
        println(" [x] Received message")
        val message = new String(body, "UTF-8")
        gson = new GsonBuilder().registerTypeAdapter(classOf[PlayerPositionMessageImpl], PlayerPositionMessageDeserializer).create()
        val otherPlayerPosition = gson.fromJson(message, classOf[PlayerPositionMessageImpl])

        connectedUsers.get(otherPlayerPosition.userId).position = otherPlayerPosition.position
        /* TODO Aggiornare lo sprite in usersTrainerSprite in base alla direzione,
        * fare il movimento sulla mappa, fare la receive in DistributedAgent, executor?
        */
      }
    }
    channel.basicConsume(userQueue, true, consumer)
  }
}
