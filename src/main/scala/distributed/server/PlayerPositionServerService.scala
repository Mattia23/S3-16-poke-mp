package distributed.server

import com.google.gson.GsonBuilder
import com.rabbitmq.client._
import distributed.ConnectedPlayers
import distributed.deserializers.PlayerPositionMessageDeserializer
import distributed.messages.PlayerPositionMessageImpl
import utilities.Settings

object PlayerPositionServerService {
  def apply(connection: Connection, connectedUsers: ConnectedPlayers): CommunicationService =
    new PlayerPositionServerService(connection, connectedUsers)
}

class PlayerPositionServerService(private val connection: Connection,
                                  private val connectedPlayers: ConnectedPlayers) extends CommunicationService {
  override def start(): Unit = {
    val channel = connection.createChannel

    import Settings._
    channel.queueDeclare(Constants.PLAYER_POSITION_CHANNEL_QUEUE, false, false, false, null)

    val consumer = new DefaultConsumer(channel) {

      override def handleDelivery(consumerTag: String,
                                  envelope: Envelope,
                                  properties: AMQP.BasicProperties,
                                  body: Array[Byte]): Unit = {

        val gson = new GsonBuilder().registerTypeAdapter(classOf[PlayerPositionMessageImpl], PlayerPositionMessageDeserializer).create()
        val positionMessage = gson.fromJson(new String(body, "UTF-8"), classOf[PlayerPositionMessageImpl])

        if (connectedPlayers containsPlayer positionMessage.userId) {
          connectedPlayers.get(positionMessage.userId).position = positionMessage.position

          channel.exchangeDeclare(Constants.PLAYER_POSITION_EXCHANGE, "fanout")
          val response = gson toJson positionMessage
          channel.basicPublish(Constants.PLAYER_POSITION_EXCHANGE, "", null, response.getBytes("UTF-8"))
        }
      }
    }

    channel.basicConsume(Constants.PLAYER_POSITION_CHANNEL_QUEUE, true, consumer)
  }
}
