package distributed.server

import com.google.gson.GsonBuilder
import com.rabbitmq.client.{AMQP, Channel, DefaultConsumer, Envelope}
import distributed.messages.{PlayerPositionMessageDeserializer, PlayerPositionMessageImpl}
import utilities.Settings

object PlayerPositionServerManager {

  private val channel: Channel = ServerConnection.connection.createChannel
  channel.queueDeclare(Settings.PLAYER_POSITION_CHANNEL_QUEUE, false, false, false, null)

  private val consumer = new DefaultConsumer(channel) {

    override def handleDelivery(consumerTag: String,
                                envelope: Envelope,
                                properties: AMQP.BasicProperties,
                                body: Array[Byte]): Unit = {
      println("server: received")
      val gson = new GsonBuilder().registerTypeAdapter(classOf[PlayerPositionMessageImpl], PlayerPositionMessageDeserializer).create()
      val message = gson.fromJson(new String(body, "UTF-8"), classOf[PlayerPositionMessageImpl])
      /* TODO: salvare il messaggio e rispondere */
    }
  }

  channel.basicConsume(Settings.PLAYER_POSITION_CHANNEL_QUEUE, true, consumer)
}
