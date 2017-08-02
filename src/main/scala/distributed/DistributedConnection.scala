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
    import Settings._
    val factory = new ConnectionFactory
    factory.setHost(Constants.REMOTE_HOST_ADDRESS)
    factory.setPort(Constants.REMOTE_HOST_PORT)
    factory.setUsername(Constants.REMOTE_HOST_USERNAME)
    factory.setPassword(Constants.REMOTE_HOST_PASSWORD)
    //factory.setHost(Constants.LOCAL_HOST_ADDRESS)
    _connection = factory.newConnection
  }

  override val connection: Connection = _connection

  override def close(): Unit = _connection.close()
}