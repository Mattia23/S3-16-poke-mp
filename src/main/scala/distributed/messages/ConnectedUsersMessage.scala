package distributed.messages

import java.util.concurrent.ConcurrentMap

import distributed.User

trait ConnectedUsersMessage {
  def connectedUsers: ConcurrentMap[Int, User]
}

object ConnectedUsersMessage {
  def apply(connectedUsers: ConcurrentMap[Int, User]): ConnectedUsersMessage = new ConnectedUsersMessageImpl(connectedUsers)
}

class ConnectedUsersMessageImpl(override val connectedUsers: ConcurrentMap[Int, User]) extends ConnectedUsersMessage