package distributed.client

import com.google.gson.{Gson, GsonBuilder}
import com.rabbitmq.client._
import distributed.ConnectedPlayers
import distributed.deserializers.PlayerPositionMessageDeserializer
import distributed.messages.{PlayerPositionMessage, PlayerPositionMessageImpl}
import model.environment.Coordinate
import utilities.Settings

trait PlayerPositionClientManager{
  def sendPlayerPosition(userId: Int, position: Coordinate): Unit

  def receiveOtherPlayerPosition(userId: Int, connectedPlayers: ConnectedPlayers): Unit
}

object PlayerPositionClientManager {
  def apply(connection: Connection): PlayerPositionClientManager = new PlayerPositionClientManagerImpl(connection)
}

class PlayerPositionClientManagerImpl(private val connection: Connection) extends PlayerPositionClientManager{

  private var gson: Gson = new Gson()
  private val channel: Channel = connection.createChannel()

  import Settings._
  channel.queueDeclare(Constants.PLAYER_POSITION_CHANNEL_QUEUE, false, false, false, null)

  override def sendPlayerPosition(userId: Int, position: Coordinate): Unit = {
    val playerPositionMessage = PlayerPositionMessage(userId, position)
    channel.basicPublish("", Constants.PLAYER_POSITION_CHANNEL_QUEUE, null, gson.toJson(playerPositionMessage).getBytes("UTF-8"))
    println(" [x] Sent position")
  }

  override def receiveOtherPlayerPosition(userId: Int, connectedPlayers: ConnectedPlayers): Unit = {

    channel.exchangeDeclare(Constants.PLAYER_POSITION_EXCHANGE, "fanout")
    val playerQueue = channel.queueDeclare.getQueue
    channel.queueBind(playerQueue, Constants.PLAYER_POSITION_EXCHANGE, "")

    val consumer = new DefaultConsumer(channel) {

      override def handleDelivery(consumerTag: String,
                                  envelope: Envelope,
                                  properties: AMQP.BasicProperties,
                                  body: Array[Byte]) {
        println(" [x] Received other player position")
        val message = new String(body, "UTF-8")
        gson = new GsonBuilder().registerTypeAdapter(classOf[PlayerPositionMessageImpl], PlayerPositionMessageDeserializer).create()
        val otherPlayerPosition = gson.fromJson(message, classOf[PlayerPositionMessageImpl])

        if (otherPlayerPosition.userId != userId && connectedPlayers.containsPlayer(otherPlayerPosition.userId))
          connectedPlayers.updateTrainerPosition(otherPlayerPosition.userId, otherPlayerPosition.position)
      }
    }

    channel.basicConsume(playerQueue, true, consumer)
  }
}
