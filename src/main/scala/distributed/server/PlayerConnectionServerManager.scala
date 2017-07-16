package distributed.server

import com.google.gson.Gson
import com.rabbitmq.client.{AMQP, Channel, DefaultConsumer, Envelope}
import distributed.{ConnectedUsersImpl, User}
import model.entities.TrainerSprites
import model.environment.{Coordinate, CoordinateImpl}
import utilities.Settings

object PlayerConnectionServerManager {

  val channel: Channel = ServerConnection.connection.createChannel
  channel.queueDeclare(Settings.PLAYER_CONNECTION_CHANNEL_QUEUE, false, false, false, null)

  ConnectedUsersImpl.map.put(162646, User(162646, "GiuliaGrossa", TrainerSprites.selectTrainerSprite(3), CoordinateImpl(0,0)))

  val consumer = new DefaultConsumer(channel) {

    override def handleDelivery(consumerTag: String,
                                envelope: Envelope,
                                properties: AMQP.BasicProperties,
                                body: Array[Byte]): Unit = {
      println("server: received")
      val gson = new Gson()
      val message = gson.fromJson(new String(body, "UTF-8"), classOf[User])

      val response = gson.toJson(ConnectedUsersImpl.map)
      channel.basicPublish("", Settings.PLAYERS_CONNECTED_CHANNEL_QUEUE + message.userId, null, response.getBytes("UTF-8"))

      ConnectedUsersImpl.map.put(message.userId, message)

    }
  }

  channel.basicConsume(Settings.PLAYER_CONNECTION_CHANNEL_QUEUE, true, consumer)

}
