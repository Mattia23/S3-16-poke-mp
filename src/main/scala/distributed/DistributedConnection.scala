package distributed

import com.rabbitmq.client.{Connection, ConnectionFactory}
import utilities.Settings

trait DistributedConnection {
  def connection: Connection

  def close(): Unit
}

object DistributedConnectionImpl {
  def apply(): DistributedConnection = new DistributedConnectionImpl()
}

class DistributedConnectionImpl extends DistributedConnection{
  private var _connection: Connection = _

  create()

  private def create() = {
    val factory = new ConnectionFactory
    factory.setHost(Settings.HOST_ADDRESS)
    _connection = factory.newConnection
  }

  override val connection: Connection = _connection

  override def close(): Unit = _connection.close
}