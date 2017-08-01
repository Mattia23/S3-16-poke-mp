package distributed.server

import com.google.gson.Gson
import com.rabbitmq.client._
import distributed.messages.PlayerIsFightingMessageImpl
import distributed.ConnectedPlayers
import utilities.Settings

object PlayerIsFightingServerService {
  def apply(connection: Connection, connectedPlayers: ConnectedPlayers): PlayerIsFightingServerService =
    new PlayerIsFightingServerService(connection, connectedPlayers)
}

/**
  *
  * @param connection Instance of the connection with RabbitMQ
  * @param connectedPlayers local connected players in the map
  */
class PlayerIsFightingServerService (private val connection: Connection,
                                     private val connectedPlayers: ConnectedPlayers) extends CommunicationService{
  override def start(): Unit = {
    val channel: Channel = connection.createChannel
    channel.queueDeclare(Settings.PLAYER_IS_FIGHTING_CHANNEL_QUEUE, false, false, false, null)

    val consumer = new DefaultConsumer(channel) {

      override def handleDelivery(consumerTag: String,
                                  envelope: Envelope,
                                  properties: AMQP.BasicProperties,
                                  body: Array[Byte]): Unit = {
        val gson = new Gson()
        val playerIsFightingMessage = gson.fromJson(new String(body, "UTF-8"), classOf[PlayerIsFightingMessageImpl])

        connectedPlayers.get(playerIsFightingMessage.userId).isFighting = playerIsFightingMessage.isFighting

        channel.exchangeDeclare(Settings.PLAYER_IS_FIGHTING_EXCHANGE, "fanout")
        val response = gson.toJson(playerIsFightingMessage)
        channel.basicPublish(Settings.PLAYER_IS_FIGHTING_EXCHANGE, "", null, response.getBytes("UTF-8"))
      }
    }

    channel.basicConsume(Settings.PLAYER_IS_FIGHTING_CHANNEL_QUEUE, true, consumer)
  }
}

