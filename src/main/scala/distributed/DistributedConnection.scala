package distributed

import com.rabbitmq.client.Connection

trait DistributedConnection {
  def connection: Connection

  def close(): Unit
}
