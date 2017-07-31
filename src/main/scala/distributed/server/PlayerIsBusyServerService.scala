package distributed.server

import com.google.gson.Gson
import com.rabbitmq.client._
import distributed.ConnectedPlayers
import distributed.messages.PlayerIsBusyMessageImpl
import utilities.Settings

object PlayerIsBusyServerService {
  def apply(connection: Connection, connectedPlayers: ConnectedPlayers): PlayerIsBusyServerService = new PlayerIsBusyServerService(connection, connectedPlayers)
}

class PlayerIsBusyServerService(private val connection: Connection, private val connectedPlayers: ConnectedPlayers) extends CommunicationService{
  override def start(): Unit = {
    val channel: Channel = connection.createChannel
    channel.queueDeclare(Settings.PLAYER_IS_BUSY_CHANNEL_QUEUE, false, false, false, null)

    val consumer = new DefaultConsumer(channel) {

      override def handleDelivery(consumerTag: String,
                                  envelope: Envelope,
                                  properties: AMQP.BasicProperties,
                                  body: Array[Byte]): Unit = {
        println("server: received player is busy")
        val gson = new Gson()
        val playerIsBusyMessage = gson.fromJson(new String(body, "UTF-8"), classOf[PlayerIsBusyMessageImpl])

        connectedPlayers.get(playerIsBusyMessage.userId).isBusy = playerIsBusyMessage.isBusy

        channel.exchangeDeclare(Settings.PLAYER_IS_BUSY_EXCHANGE, "fanout")
        val response = gson.toJson(playerIsBusyMessage)
        channel.basicPublish(Settings.PLAYER_IS_BUSY_EXCHANGE, "", null, response.getBytes("UTF-8"))
        println("server: send player is fighting")
      }
    }

    channel.basicConsume(Settings.PLAYER_IS_BUSY_CHANNEL_QUEUE, true, consumer)
  }
}

