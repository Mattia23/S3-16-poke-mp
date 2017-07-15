package distributed.server

import com.google.gson.Gson
import com.rabbitmq.client.{AMQP, Channel, DefaultConsumer, Envelope}
import distributed.{ConnectedUsersImpl, User}
import utilities.Settings

object PlayerConnectionServerManager {

  val channel: Channel = ServerConnection.connection.createChannel

  channel.queueDeclare(Settings.PLAYER_CONNECTION_CHANNEL_QUEUE, false, false, false, null)

  val consumer = new DefaultConsumer(channel) {

    override def handleDelivery(consumerTag: String,
                                envelope: Envelope,
                                properties: AMQP.BasicProperties,
                                body: Array[Byte]): Unit = {
      val gson = new Gson()
      val message = gson.fromJson(new String(body, "UTF-8"), classOf[User])
      ConnectedUsersImpl.map.put(message.userId, message)
      
    }
  }

  channel.basicConsume(Settings.PLAYER_CONNECTION_CHANNEL_QUEUE, true, consumer)

}
