package distributed.client

/**
  * ClosableManager permits to close queues and the channel of a Manager
  */
trait ClosableManager {
  /**
    * Closes queues and the channel of the manager
    */
  def close(): Unit
}
