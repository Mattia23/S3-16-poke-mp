package distributed.client

import com.google.gson.Gson
import com.rabbitmq.client._
import distributed.ConnectedPlayers
import distributed.messages.{PlayerIsBusyMessage, PlayerIsBusyMessageImpl}
import utilities.Settings

trait PlayerIsBusyClientManager {
  def sendPlayerIsBusy(userId: Int, isInBuilding: Boolean): Unit

  def receiveOtherPlayerIsBusy(userId: Int, connectedPlayers: ConnectedPlayers): Unit
}

object PlayerIsBusyClientManager {
  def apply(connection: Connection): PlayerIsBusyClientManager = new PlayerIsBusyClientManagerImpl(connection)
}

class PlayerIsBusyClientManagerImpl(private val connection: Connection) extends PlayerIsBusyClientManager {

  private val gson: Gson = new Gson()
  private val channel: Channel = connection.createChannel()

  private val playerQueue = channel.queueDeclare.getQueue

  import Settings._
  channel.queueDeclare(Constants.PLAYER_IS_BUSY_CHANNEL_QUEUE, false, false, false, null)

  channel.exchangeDeclare(Constants.PLAYER_IS_BUSY_EXCHANGE, "fanout")
  channel.queueBind(playerQueue, Constants.PLAYER_IS_BUSY_EXCHANGE, "")

  override def sendPlayerIsBusy(userId: Int, isInBuilding: Boolean): Unit = {
    val playerIsBusyMessage = PlayerIsBusyMessage(userId, isInBuilding)
    channel.basicPublish("", Constants.PLAYER_IS_BUSY_CHANNEL_QUEUE, null, gson.toJson(playerIsBusyMessage).getBytes("UTF-8"))
    println(" [x] Sent player is busy message")
  }

  override def receiveOtherPlayerIsBusy(userId: Int, connectedPlayers: ConnectedPlayers): Unit = {
    val consumer = new DefaultConsumer(channel) {

      override def handleDelivery(consumerTag: String,
                                  envelope: Envelope,
                                  properties: AMQP.BasicProperties,
                                  body: Array[Byte]) {
        println(" [x] Received other player is fighting")
        val playerIsBusyMessage = gson.fromJson(new String(body, "UTF-8"), classOf[PlayerIsBusyMessageImpl])

        if (playerIsBusyMessage.userId != userId && connectedPlayers.containsPlayer(playerIsBusyMessage.userId))
          connectedPlayers.get(playerIsBusyMessage.userId).isBusy = playerIsBusyMessage.isBusy
      }

    }

    channel.basicConsume(playerQueue, true, consumer)
  }
}
