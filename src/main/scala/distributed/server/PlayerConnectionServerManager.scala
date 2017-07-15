package distributed.server

import com.rabbitmq.client.{AMQP, Channel, DefaultConsumer, Envelope}
import utilities.Settings

object PlayerConnectionServerManager {

  val channel: Channel = ServerConnection.connection.createChannel

  channel.queueDeclare(Settings.PLAYER_CONNECTION_CHANNEL_QUEUE, false, false, false, null)

  val consumer = new DefaultConsumer(channel) {

    override def handleDelivery(consumerTag: String,
                                envelope: Envelope,
                                properties: AMQP.BasicProperties,
                                body: Array[Byte]): Unit = {

    }
  }

  channel.basicConsume(Settings.PLAYER_CONNECTION_CHANNEL_QUEUE, true, consumer)

}
