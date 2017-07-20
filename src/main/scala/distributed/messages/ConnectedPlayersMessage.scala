package distributed.messages

import java.util.concurrent.ConcurrentMap

import distributed.User

trait ConnectedPlayersMessage {
  def connectedPlayers: ConcurrentMap[Int, User]
}

object ConnectedPlayersMessage {
  def apply(connectedPlayers: ConcurrentMap[Int, User]): ConnectedPlayersMessage = new ConnectedPlayersMessageImpl(connectedPlayers)
}

class ConnectedPlayersMessageImpl(override val connectedPlayers: ConcurrentMap[Int, User]) extends ConnectedPlayersMessage