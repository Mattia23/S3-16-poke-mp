package distributed.client

import java.util.concurrent.ConcurrentMap

import com.google.gson.{Gson, GsonBuilder}
import com.rabbitmq.client._
import distributed.deserializers.PlayerPositionMessageDeserializer
import distributed.messages.PlayerPositionMessageImpl
import distributed.{DistributedConnectionImpl, Player}
import model.environment.Coordinate
import utilities.Settings

trait PlayerPositionClientManager{
  def sendPlayerPosition(userId: Int, position: Coordinate): Unit

  def receiveOtherPlayerPosition(userId: Int, connectedPlayers: ConcurrentMap[Int, Player]): Unit
}

object PlayerPositionClientManagerImpl {
  def apply(connection: Connection): PlayerPositionClientManager = new PlayerPositionClientManagerImpl(connection)
}

class PlayerPositionClientManagerImpl(private val connection: Connection) extends PlayerPositionClientManager{

  private var gson: Gson = new Gson()
  private var channel: Channel = connection.createChannel()

  channel.queueDeclare(Settings.PLAYER_POSITION_CHANNEL_QUEUE, false, false, false, null)

  override def sendPlayerPosition(userId: Int, position: Coordinate): Unit = {
    val playerPositionMessage = PlayerPositionMessageImpl(userId, position)
    channel.basicPublish("", Settings.PLAYER_POSITION_CHANNEL_QUEUE, null, gson.toJson(playerPositionMessage).getBytes("UTF-8"))
    println(" [x] Sent position")
  }

  override def receiveOtherPlayerPosition(userId: Int, connectedPlayers: ConcurrentMap[Int, Player]): Unit = {

    channel.exchangeDeclare(Settings.PLAYER_POSITION_EXCHANGE, "fanout")
    val playerQueue = channel.queueDeclare.getQueue
    channel.queueBind(playerQueue, Settings.PLAYER_POSITION_EXCHANGE, "")

    val consumer = new DefaultConsumer(channel) {

      override def handleDelivery(consumerTag: String,
                                  envelope: Envelope,
                                  properties: AMQP.BasicProperties,
                                  body: Array[Byte]) {
        println(" [x] Received other player position")
        val message = new String(body, "UTF-8")
        gson = new GsonBuilder().registerTypeAdapter(classOf[PlayerPositionMessageImpl], PlayerPositionMessageDeserializer).create()
        val otherPlayerPosition = gson.fromJson(message, classOf[PlayerPositionMessageImpl])

        if (otherPlayerPosition.userId != userId) connectedPlayers.get(otherPlayerPosition.userId).position = otherPlayerPosition.position

        /* TODO Aggiornare lo sprite in usersTrainerSprite in base alla direzione,
        * fare il movimento sulla mappa, fare la receive in DistributedAgent, executor?
        */
      }
    }

    channel.basicConsume(playerQueue, true, consumer)
  }
}
