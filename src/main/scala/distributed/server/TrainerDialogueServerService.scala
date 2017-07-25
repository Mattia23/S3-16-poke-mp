package distributed.server

import com.rabbitmq.client._
import distributed._
import utilities.Settings

object TrainerDialogueServerService {
  def apply(connection: Connection): CommunicationService = new TrainerDialogueServerService(connection)
}

class TrainerDialogueServerService(private val connection: Connection) extends CommunicationService {
  override def start(): Unit = {
    val channel: Channel = connection.createChannel
    channel.queueDeclare(Settings.TRAINER_DIALOGUE_CHANNEL_QUEUE, false, false, false, null)

    val consumer = new DefaultConsumer(channel) {

      override def handleDelivery(consumerTag: String,
                                  envelope: Envelope,
                                  properties: AMQP.BasicProperties,
                                  body: Array[Byte]): Unit = {
        println("server: received new dialogue request")
        println(new String(body, "UTF-8"))

        val response = new String(body, "UTF-8")
        channel.basicPublish("", Settings.TRAINER_DIALOGUE_CHANNEL_QUEUE + response, null, response.getBytes("UTF-8"))
        println("server: send connected player")

        /*channel.exchangeDeclare(Settings.NEW_PLAYER_EXCHANGE, "fanout")
        val newPlayerResponse = gson.toJson(playerMessage)
        channel.basicPublish(Settings.NEW_PLAYER_EXCHANGE, "", null, newPlayerResponse.getBytes("UTF-8"))
        println("server: send new player")*/
      }
    }

    channel.basicConsume(Settings.TRAINER_DIALOGUE_CHANNEL_QUEUE, true, consumer)
  }

}
