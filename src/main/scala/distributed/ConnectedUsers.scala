package distributed

import java.util.concurrent.ConcurrentHashMap

trait ConnectedUsers {
  def map: ConcurrentHashMap[Int, User]
}

object ConnectedUsersImpl extends ConnectedUsers{

  private var _map = new ConcurrentHashMap[Int, User]()

  override val map: ConcurrentHashMap[Int, User] = _map
}
