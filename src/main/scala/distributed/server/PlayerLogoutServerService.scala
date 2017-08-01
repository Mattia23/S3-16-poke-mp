package distributed.server

import com.google.gson.Gson
import com.rabbitmq.client._
import distributed.ConnectedPlayers
import distributed.messages.PlayerLogoutMessageImpl
import utilities.Settings

object PlayerLogoutServerService {
  def apply(connection: Connection, connectedPlayers: ConnectedPlayers): CommunicationService = new PlayerLogoutServerService(connection, connectedPlayers)
}

class PlayerLogoutServerService(private val connection: Connection, private val connectedPlayers: ConnectedPlayers) extends CommunicationService{
  override def start(): Unit = {
    val channel: Channel = connection.createChannel

    import Settings._
    channel.queueDeclare(Constants.PLAYER_LOGOUT_CHANNEL_QUEUE, false, false, false, null)

    val consumer = new DefaultConsumer(channel) {

      override def handleDelivery(consumerTag: String,
                                  envelope: Envelope,
                                  properties: AMQP.BasicProperties,
                                  body: Array[Byte]): Unit = {
        println("server: received player logout")
        val gson = new Gson()
        val logoutMessage = gson.fromJson(new String(body, "UTF-8"), classOf[PlayerLogoutMessageImpl])

        if (connectedPlayers.containsPlayer(logoutMessage.userId)) {
          connectedPlayers.remove(logoutMessage.userId)

          channel.exchangeDeclare(Constants.PLAYER_LOGOUT_EXCHANGE, "fanout")
          val response = gson.toJson(logoutMessage)
          channel.basicPublish(Constants.PLAYER_LOGOUT_EXCHANGE, "", null, response.getBytes("UTF-8"))
          println("server: send player logout")
        }
      }
    }

    channel.basicConsume(Constants.PLAYER_LOGOUT_CHANNEL_QUEUE, true, consumer)
  }
}
