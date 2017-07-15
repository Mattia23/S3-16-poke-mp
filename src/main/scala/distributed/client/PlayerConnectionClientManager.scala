package distributed.client

import com.rabbitmq.client.Channel
import utilities.Settings

object PlayerConnectionClientManager {

  val channel: Channel = ClientConnection.connection.createChannel()
  channel.queueDeclare(Settings.PLAYER_CONNECTION_CHANNEL_QUEUE, false, false, false, null)
}
