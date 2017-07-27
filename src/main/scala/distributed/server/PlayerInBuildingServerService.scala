package distributed.server

import java.util.concurrent.ConcurrentMap

import com.google.gson.Gson
import com.rabbitmq.client._
import distributed.messages.PlayerInBuildingMessageImpl
import distributed.{CommunicationService, Player}
import utilities.Settings

object PlayerInBuildingServerService {
  def apply(connection: Connection, connectedPlayers: ConcurrentMap[Int, Player]): PlayerInBuildingServerService = new PlayerInBuildingServerService(connection, connectedPlayers)
}

class PlayerInBuildingServerService (private val connection: Connection, private val connectedPlayers: ConcurrentMap[Int, Player]) extends CommunicationService{
  override def start(): Unit = {
    val channel: Channel = connection.createChannel
    channel.queueDeclare(Settings.PLAYER_IN_BUILDING_CHANNEL_QUEUE, false, false, false, null)

    val consumer = new DefaultConsumer(channel) {

      override def handleDelivery(consumerTag: String,
                                  envelope: Envelope,
                                  properties: AMQP.BasicProperties,
                                  body: Array[Byte]): Unit = {

        val gson = new Gson()
        val playerInBuildingMessage = gson.fromJson(new String(body, "UTF-8"), classOf[PlayerInBuildingMessageImpl])

        connectedPlayers.get(playerInBuildingMessage.userId).isVisible = playerInBuildingMessage.isInBuilding

        channel.exchangeDeclare(Settings.PLAYER_IN_BUILDING_EXCHANGE, "fanout")
        val response = gson.toJson(playerInBuildingMessage)
        channel.basicPublish(Settings.PLAYER_IN_BUILDING_EXCHANGE, "", null, response.getBytes("UTF-8"))
      }
    }

    channel.basicConsume(Settings.PLAYER_IN_BUILDING_CHANNEL_QUEUE, true, consumer)
  }
}
