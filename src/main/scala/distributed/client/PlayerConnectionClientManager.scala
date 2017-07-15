package distributed.client

import com.google.gson.Gson
import com.rabbitmq.client.{AMQP, Channel, DefaultConsumer, Envelope}
import distributed.User
import model.environment.Coordinate
import utilities.Settings

object PlayerConnectionClientManager {

  private val gson: Gson = new Gson()

  private val channel: Channel = ClientConnection.connection.createChannel()
  channel.queueDeclare(Settings.PLAYER_CONNECTION_CHANNEL_QUEUE, false, false, false, null)

  val consumer = new DefaultConsumer(channel) {

    override def handleDelivery(consumerTag: String,
                                envelope: Envelope,
                                properties: AMQP.BasicProperties,
                                body: Array[Byte]) {
      val message = new String(body, "UTF-8")
      println(" [x] Received message")
    }
  }
  channel.basicConsume(Settings.PLAYER_CONNECTION_CHANNEL_QUEUE, true, consumer)

  def sendUserInformation(userId: Int, username: String, idImage: Int, position: Coordinate): Unit = {
    val user = User(userId, username, idImage, position)
    channel.basicPublish("", Settings.PLAYER_CONNECTION_CHANNEL_QUEUE, null, gson.toJson(user).getBytes("UTF-8"))
    println(" [x] Sent message")
  }

}
