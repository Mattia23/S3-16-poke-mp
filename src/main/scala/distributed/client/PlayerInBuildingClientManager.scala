package distributed.client

import com.google.gson.Gson
import com.rabbitmq.client._
import distributed.ConnectedPlayers
import distributed.messages.{PlayerInBuildingMessage, PlayerInBuildingMessageImpl}
import utilities.Settings

trait PlayerInBuildingClientManager {
  def sendPlayerIsInBuilding(userId: Int, isInBuilding: Boolean): Unit

  def receiveOtherPlayerIsInBuilding(userId: Int, connectedPlayers: ConnectedPlayers): Unit
}

object PlayerInBuildingClientManager {
  def apply(connection: Connection): PlayerInBuildingClientManager = new PlayerInBuildingClientManagerImpl(connection)
}

class PlayerInBuildingClientManagerImpl(private val connection: Connection) extends PlayerInBuildingClientManager {

  private val gson: Gson = new Gson()
  private val channel: Channel = connection.createChannel()

  private val playerQueue = channel.queueDeclare.getQueue

  import Settings._
  channel.queueDeclare(Constants.PLAYER_IN_BUILDING_CHANNEL_QUEUE, false, false, false, null)

  channel.exchangeDeclare(Constants.PLAYER_IN_BUILDING_EXCHANGE, "fanout")
  channel.queueBind(playerQueue, Constants.PLAYER_IN_BUILDING_EXCHANGE, "")

  override def sendPlayerIsInBuilding(userId: Int, isInBuilding: Boolean): Unit = {
    val playerInBuildingMessage = PlayerInBuildingMessage(userId, isInBuilding)
    channel.basicPublish("", Constants.PLAYER_IN_BUILDING_CHANNEL_QUEUE, null, gson.toJson(playerInBuildingMessage).getBytes("UTF-8"))
    println(" [x] Sent player is in building message")
  }

  override def receiveOtherPlayerIsInBuilding(userId: Int, connectedPlayers: ConnectedPlayers): Unit = {
    val consumer = new DefaultConsumer(channel) {

      override def handleDelivery(consumerTag: String,
                                  envelope: Envelope,
                                  properties: AMQP.BasicProperties,
                                  body: Array[Byte]) {
        println(" [x] Received other player in building")
        val playerInBuildingMessage = gson.fromJson(new String(body, "UTF-8"), classOf[PlayerInBuildingMessageImpl])

        if (playerInBuildingMessage.userId != userId && connectedPlayers.containsPlayer(playerInBuildingMessage.userId))
          connectedPlayers.get(playerInBuildingMessage.userId).isVisible = playerInBuildingMessage.isInBuilding
      }

    }

    channel.basicConsume(playerQueue, true, consumer)
  }
}
