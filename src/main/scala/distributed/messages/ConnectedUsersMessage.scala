package distributed.messages

import java.util.concurrent.ConcurrentMap

import distributed.Player

trait ConnectedUsersMessage {
  def connectedUsers: ConcurrentMap[Int, Player]
}

object ConnectedUsersMessage {
  def apply(connectedUsers: ConcurrentMap[Int, Player]): ConnectedUsersMessage = new ConnectedUsersMessageImpl(connectedUsers)
}

class ConnectedUsersMessageImpl(override val connectedUsers: ConcurrentMap[Int, Player]) extends ConnectedUsersMessage