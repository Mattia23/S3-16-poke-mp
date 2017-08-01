package distributed.client

import com.google.gson.Gson
import com.rabbitmq.client._
import distributed.ConnectedPlayers
import distributed.messages.{PlayerIsFightingMessage, PlayerIsFightingMessageImpl}
import utilities.Settings

/**
  * PlayerIsFightingManager sends and receives PlayerIsFightingMessages and update the related attributed for the
  * local variable of the connected user.
  */
trait PlayerIsFightingClientManager {
  /**
    * Send a new message updating the attribute isFighting for a specific user id.
    * @param userId id of the user to update
    * @param isFighting boolean that indicates if the player is fighting (true) or not (false)
    */
  def sendPlayerIsFighting(userId: Int, isFighting: Boolean): Unit

  /**
    * Receive a new IsFightingMessage and update the local connected players.
    * @param userId id of the playing trainer
    * @param connectedPlayers connected players in the current map
    */
  def receiveOtherPlayerIsFighting(userId: Int, connectedPlayers: ConnectedPlayers): Unit
}

object PlayerIsFightingClientManager {
  def apply(connection: Connection): PlayerIsFightingClientManager = new PlayerIsFightingClientManagerImpl(connection)
}

/**
  * @inheritdoc
  * @param connection instance of Connection with RabbitMQ
  */
class PlayerIsFightingClientManagerImpl(private val connection: Connection) extends PlayerIsFightingClientManager {

  private val gson: Gson = new Gson()
  private val channel: Channel = connection.createChannel()

  private val playerQueue = channel.queueDeclare.getQueue

  channel.queueDeclare(Settings.PLAYER_IS_FIGHTING_CHANNEL_QUEUE, false, false, false, null)

  channel.exchangeDeclare(Settings.PLAYER_IS_FIGHTING_EXCHANGE, "fanout")
  channel.queueBind(playerQueue, Settings.PLAYER_IS_FIGHTING_EXCHANGE, "")

  /**
    * @inheritdoc
    */
  override def sendPlayerIsFighting(userId: Int, isFighting: Boolean): Unit = {
    val playerIsFightingMessage = PlayerIsFightingMessage(userId, isFighting)
    channel.basicPublish("", Settings.PLAYER_IS_FIGHTING_CHANNEL_QUEUE, null, gson.toJson(playerIsFightingMessage).getBytes("UTF-8"))
  }

  /**
    * @inheritdoc
    */
  override def receiveOtherPlayerIsFighting(userId: Int, connectedPlayers: ConnectedPlayers): Unit = {
    val consumer = new DefaultConsumer(channel) {

      override def handleDelivery(consumerTag: String,
                                  envelope: Envelope,
                                  properties: AMQP.BasicProperties,
                                  body: Array[Byte]) {

        val playerIsFightingMessage = gson.fromJson(new String(body, "UTF-8"), classOf[PlayerIsFightingMessageImpl])

        if (playerIsFightingMessage.userId != userId && connectedPlayers.containsPlayer(playerIsFightingMessage.userId))
          connectedPlayers.get(playerIsFightingMessage.userId).isFighting = playerIsFightingMessage.isFighting
      }

    }

    channel.basicConsume(playerQueue, true, consumer)
  }
}
