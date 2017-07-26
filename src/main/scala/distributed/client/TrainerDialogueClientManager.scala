package distributed.client

import com.google.gson.Gson
import com.rabbitmq.client._
import controller.{GameController, MapController}
import distributed.messages.{TrainerDialogueMessage, TrainerDialogueMessageImpl}
import utilities.Settings

trait TrainerDialogueClientManager {
  def sendDialogueRequest(otherTrainerId: Int, wantToFight: Boolean): Unit

  def receiveResponse(): Unit
}

object TrainerDialogueClientManager {
  def apply(connection: Connection, mapController: GameController): TrainerDialogueClientManager = new TrainerDialogueClientManagerImpl(connection, mapController)
}

class TrainerDialogueClientManagerImpl(private val connection: Connection, private val mapController: GameController) extends TrainerDialogueClientManager {

  private val trainerId: Int = mapController.trainer.id
  private var gson: Gson = new Gson()
  private val channel: Channel = connection.createChannel()
  private val playerQueue = Settings.TRAINER_DIALOGUE_CHANNEL_QUEUE + trainerId

  channel.queueDeclare(Settings.TRAINER_DIALOGUE_CHANNEL_QUEUE + trainerId, false, false, false, null)

  override def sendDialogueRequest(otherTrainerId: Int, wantToFight: Boolean): Unit = {
    channel.queueDeclare(Settings.TRAINER_DIALOGUE_CHANNEL_QUEUE + otherTrainerId, false, false, false, null)
    val trainerDialogueMessage = TrainerDialogueMessage(trainerId, otherTrainerId, wantToFight)
    channel.basicPublish("", Settings.TRAINER_DIALOGUE_CHANNEL_QUEUE + otherTrainerId, null, gson.toJson(trainerDialogueMessage).getBytes("UTF-8"))
    println(" [x] sent fight request to "+otherTrainerId)
  }

  override def receiveResponse(): Unit = {

    val consumer = new DefaultConsumer(channel) {

      override def handleDelivery(consumerTag: String,
                                  envelope: Envelope,
                                  properties: AMQP.BasicProperties,
                                  body: Array[Byte]) {
        println(" [x] Received fight response")
        val trainerDialogueMessage = gson.fromJson(new String(body, "UTF-8"), classOf[TrainerDialogueMessageImpl])
        println(trainerDialogueMessage.trainerId, trainerDialogueMessage.otherTrainerId, trainerDialogueMessage.wantToFight)
        if(trainerDialogueMessage.wantToFight && trainerDialogueMessage.otherTrainerId == trainerId)
          println("mi hanno sfidato")
        else if(trainerDialogueMessage.wantToFight)
          println("hanno accettato la sfida, si parteeeeee")
        else
          println("il tipo ha rifiutato la sfida, stronzommerda")
        //channel.close()
      }
    }
    channel.basicConsume(playerQueue, true, consumer)
  }
}
