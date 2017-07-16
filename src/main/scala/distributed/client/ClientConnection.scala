package distributed.client

import com.rabbitmq.client.{Connection, ConnectionFactory}
import distributed.DistributedConnection
import utilities.Settings

object ClientConnection extends DistributedConnection{
  private var _connection: Connection = _

  create()

  private def create() = {
    val factory = new ConnectionFactory()
    factory.setHost(Settings.HOST_ADDRESS)
    _connection = factory.newConnection()
  }

  override val connection: Connection = _connection

  override def close(): Unit = _connection.close()
}
