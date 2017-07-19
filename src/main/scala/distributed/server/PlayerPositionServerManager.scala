package distributed.server

import java.util.concurrent.ConcurrentMap

import com.google.gson.GsonBuilder
import com.rabbitmq.client.{AMQP, Channel, DefaultConsumer, Envelope}
import distributed.deserializers.PlayerPositionMessageDeserializer
import distributed.{CommunicationManager, DistributedConnectionImpl, User}
import distributed.messages.PlayerPositionMessageImpl
import utilities.Settings

object PlayerPositionServerManager {
  def apply(connectedUsers: ConcurrentMap[Int, User]): CommunicationManager = new PlayerPositionServerManager(connectedUsers)
}

class PlayerPositionServerManager(private val connectedUsers: ConcurrentMap[Int, User]) extends CommunicationManager {
  override def start(): Unit = {
    val channel: Channel = DistributedConnectionImpl().connection.createChannel
    channel.queueDeclare(Settings.PLAYER_POSITION_CHANNEL_QUEUE, false, false, false, null)

    val consumer = new DefaultConsumer(channel) {

      override def handleDelivery(consumerTag: String,
                                  envelope: Envelope,
                                  properties: AMQP.BasicProperties,
                                  body: Array[Byte]): Unit = {
        println("server: received")
        val gson = new GsonBuilder().registerTypeAdapter(classOf[PlayerPositionMessageImpl], PlayerPositionMessageDeserializer).create()
        val positionMessage = gson.fromJson(new String(body, "UTF-8"), classOf[PlayerPositionMessageImpl])

        connectedUsers.get(positionMessage.userId).position = positionMessage.position

        connectedUsers.values() forEach (user => println(""+user.userId+ " "+user.position.x+" "+user.position.y))

        /* TODO: inviare la posizione a tutti (diverso modo di gestire le code product/subscribe) */
      }
    }

    channel.basicConsume(Settings.PLAYER_POSITION_CHANNEL_QUEUE, true, consumer)
  }
}
