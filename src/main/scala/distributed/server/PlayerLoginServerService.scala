package distributed.server

import java.util.concurrent.ConcurrentMap

import com.google.gson.GsonBuilder
import com.rabbitmq.client._
import distributed._
import distributed.deserializers.PlayerMessageDeserializer
import distributed.messages.{ConnectedPlayersMessage, PlayerMessageImpl}
import utilities.Settings

object PlayerLoginServerService {
  def apply(connection: Connection, connectedPlayers: ConcurrentMap[Int, Player]): CommunicationService = new PlayerLoginServerService(connection, connectedPlayers)
}

class PlayerLoginServerService(private val connection: Connection, private val connectedPlayers: ConcurrentMap[Int, Player]) extends CommunicationService {
  override def start(): Unit = {
    val channel: Channel = connection.createChannel
    channel.queueDeclare(Settings.PLAYER_CONNECTION_CHANNEL_QUEUE, false, false, false, null)

    val consumer = new DefaultConsumer(channel) {

      override def handleDelivery(consumerTag: String,
                                  envelope: Envelope,
                                  properties: AMQP.BasicProperties,
                                  body: Array[Byte]): Unit = {
        println("server: received new player")
        println(new String(body, "UTF-8"))
        val gson = new GsonBuilder().registerTypeAdapter(classOf[PlayerMessageImpl], PlayerMessageDeserializer).create()
        val playerMessage = gson.fromJson(new String(body, "UTF-8"), classOf[PlayerMessageImpl])
        val player = playerMessage.player

        val response = gson.toJson(ConnectedPlayersMessage(connectedPlayers))
        channel.basicPublish("", Settings.PLAYERS_CONNECTED_CHANNEL_QUEUE + player.userId, null, response.getBytes("UTF-8"))
        println("server: send connected player")
        connectedPlayers.put(player.userId, player)

        channel.exchangeDeclare(Settings.NEW_PLAYER_EXCHANGE, "fanout")
        val newPlayerResponse = gson.toJson(playerMessage)
        channel.basicPublish(Settings.NEW_PLAYER_EXCHANGE, "", null, newPlayerResponse.getBytes("UTF-8"))
        println("server: send new player")
      }
    }

    channel.basicConsume(Settings.PLAYER_CONNECTION_CHANNEL_QUEUE, true, consumer)
  }

}
