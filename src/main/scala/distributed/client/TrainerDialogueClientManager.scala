package distributed.client

import com.rabbitmq.client._
import utilities.Settings

trait TrainerDialogueClientManager {
  def sendDialogueRequest(userId: Int): Unit

  def sendFightResponse(userId: Int, accepted: Boolean): Unit

  def receiveResponse(trainerChallengerId: Int): Unit
}

object TrainerDialogueClientManager {
  def apply(connection: Connection, trainerId: Int): TrainerDialogueClientManager = new TrainerDialogueClientManagerImpl(connection, trainerId)
}

class TrainerDialogueClientManagerImpl(private val connection: Connection, final val trainerId: Int) extends TrainerDialogueClientManager {

  private val channel: Channel = connection.createChannel()
  private val playerQueue = channel.queueDeclare.getQueue

  channel.queueDeclare(Settings.PLAYER_LOGOUT_CHANNEL_QUEUE+trainerId, false, false, false, null)

  /*channel.exchangeDeclare(Settings.PLAYER_LOGOUT_EXCHANGE, "fanout")
  channel.queueBind(playerQueue, Settings.PLAYER_LOGOUT_EXCHANGE, "")*/

  override def sendDialogueRequest(userId: Int): Unit = {
    channel.basicPublish("", Settings.PLAYER_LOGOUT_CHANNEL_QUEUE+userId, null, null)
    println(" [x] "+trainerId+" has sent fight request to "+userId)
  }

  override def sendFightResponse(userId: Int, accepted: Boolean): Unit = {
    channel.basicPublish("", Settings.PLAYER_LOGOUT_CHANNEL_QUEUE+userId, null, accepted.toString.getBytes("UTF-8"))
    println(" [x] "+trainerId+" has answered to "+userId)
  }

  override def receiveResponse(trainerChallengerId: Int): Unit = {
    val consumer = new DefaultConsumer(channel) {

      override def handleDelivery(consumerTag: String,
                                  envelope: Envelope,
                                  properties: AMQP.BasicProperties,
                                  body: Array[Byte]) {
        println(" [x] Received fight response")
        if(body != null && trainerChallengerId != 0){

        }
        val accepted: Boolean = new String(body, "UFT-8").toBoolean
        if(accepted){

        }


      }

    }

    channel.basicConsume(playerQueue, true, consumer)
  }
}
