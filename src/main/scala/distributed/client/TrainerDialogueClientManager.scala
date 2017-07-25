package distributed.client

import com.rabbitmq.client._
import utilities.Settings

trait TrainerDialogueClientManager {
  def sendDialogueRequest(userId: Int): Unit

  //def sendFightResponse(userId: Int, accepted: Boolean): Unit

  def receiveResponse(trainerId: Int, trainerChallengerId: Int): Unit
}

object TrainerDialogueClientManager {
  def apply(connection: Connection, trainerId: Int): TrainerDialogueClientManager = new TrainerDialogueClientManagerImpl(connection)
}

class TrainerDialogueClientManagerImpl(private val connection: Connection) extends TrainerDialogueClientManager {

  private val channel: Channel = connection.createChannel()
  private val playerQueue = channel.queueDeclare.getQueue

  channel.queueDeclare(Settings.TRAINER_DIALOGUE_CHANNEL_QUEUE, false, false, false, null)

  /*channel.exchangeDeclare(Settings.TRAINER_DIALOGUE_EXCHANGE, "fanout")
  channel.queueBind(playerQueue, Settings.TRAINER_DIALOGUE_EXCHANGE, "")*/

  override def sendDialogueRequest(userId: Int): Unit = {
    channel.basicPublish("", Settings.TRAINER_DIALOGUE_CHANNEL_QUEUE, null, userId.toString.getBytes("UTF-8"))
    println(" [x] sent fight request to "+userId)
  }

  /*override def sendFightResponse(userId: Int, accepted: Boolean): Unit = {
    channel.basicPublish("", Settings.TRAINER_DIALOGUE_CHANNEL_QUEUE+userId, null, accepted.toString.getBytes("UTF-8"))
    println(" [x] answer to "+userId)
  }*/

  override def receiveResponse(trainerId: Int, trainerChallengerId: Int): Unit = {
    val playerQueue = Settings.TRAINER_DIALOGUE_CHANNEL_QUEUE + trainerId
    channel.queueDeclare(playerQueue, false, false, false, null)

    val consumer = new DefaultConsumer(channel) {

      override def handleDelivery(consumerTag: String,
                                  envelope: Envelope,
                                  properties: AMQP.BasicProperties,
                                  body: Array[Byte]) {
        println(" [x] Received fight response")
        if(body != null && trainerId != 0){

        }
        val accepted: Boolean = new String(body, "UFT-8").toBoolean
        if(!accepted){
          //trainerChallengerId = 0
        }else{
          println("sfida accettata")
        }

        channel.close()
      }
    }
    channel.basicConsume(playerQueue, true, consumer)
  }
}
