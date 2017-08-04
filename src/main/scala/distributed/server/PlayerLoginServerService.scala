package distributed.server

import com.google.gson.GsonBuilder
import com.rabbitmq.client._
import distributed._
import distributed.deserializers.PlayerMessageDeserializer
import distributed.messages.{ConnectedPlayersMessage, PlayerMessageImpl}
import utilities.Settings

object PlayerLoginServerService {
  def apply(connection: Connection, connectedPlayers: ConnectedPlayers): CommunicationService =
    new PlayerLoginServerService(connection, connectedPlayers)
}

/**
  * PlayerLoginServerService receives when a new player is connected to the game, sends to the new player all the connected
  * players, and sends to all clients that a new player is connected
  * @param connection instance of connection to the RabbitMQ Broker
  * @param connectedPlayers all the players currently connected as ConnectedPlayers
  */
class PlayerLoginServerService(private val connection: Connection,
                               private val connectedPlayers: ConnectedPlayers) extends CommunicationService {
  /**
    * @inheritdoc
    */
  override def start(): Unit = {
    val channel = connection.createChannel

    import Settings._
    channel.queueDeclare(Constants.PLAYER_LOGIN_CHANNEL_QUEUE, false, false, false, null)

    val consumer = new DefaultConsumer(channel) {

      override def handleDelivery(consumerTag: String,
                                  envelope: Envelope,
                                  properties: AMQP.BasicProperties,
                                  body: Array[Byte]): Unit = {

        val gson = new GsonBuilder().registerTypeAdapter(classOf[PlayerMessageImpl], PlayerMessageDeserializer).create()
        val playerMessage = gson.fromJson(new String(body, "UTF-8"), classOf[PlayerMessageImpl])
        val player = playerMessage.player

        val response = gson toJson ConnectedPlayersMessage(connectedPlayers.getAll)
        val responseQueue = Constants.PLAYERS_CONNECTED_CHANNEL_QUEUE + player.userId
        channel.queueDeclare(responseQueue, false, false, false, null)
        channel.basicPublish("", responseQueue, null, response.getBytes("UTF-8"))

        connectedPlayers.add(player.userId, player)

        channel.exchangeDeclare(Constants.NEW_PLAYER_EXCHANGE, "fanout")
        val newPlayerResponse = gson toJson playerMessage
        channel.basicPublish(Constants.NEW_PLAYER_EXCHANGE, "", null, newPlayerResponse.getBytes("UTF-8"))
      }
    }

    channel.basicConsume(Constants.PLAYER_LOGIN_CHANNEL_QUEUE, true, consumer)
  }

}
