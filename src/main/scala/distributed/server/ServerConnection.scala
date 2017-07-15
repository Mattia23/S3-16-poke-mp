package distributed.server

import com.rabbitmq.client.Connection
import distributed.DistributedConnection
import com.rabbitmq.client.ConnectionFactory
import utilities.Settings

object ServerConnection extends DistributedConnection{
  private var _connection: Connection = _

  create()

  private def create() = {
    val factory = new ConnectionFactory
    factory.setHost(Settings.CLIENT_HOST)
    _connection = factory.newConnection
  }

  override val connection: Connection = _connection

  override def close(): Unit = _connection.close()
}
