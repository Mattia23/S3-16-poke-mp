package distributed.messages

import java.util.concurrent.ConcurrentMap

import distributed.Player

trait ConnectedPlayersMessage {
  def connectedPlayers: ConcurrentMap[Int, Player]
}

object ConnectedPlayersMessage {
  def apply(connectedPlayers: ConcurrentMap[Int, Player]): ConnectedPlayersMessage = new ConnectedPlayersMessageImpl(connectedPlayers)
}

class ConnectedPlayersMessageImpl(override val connectedPlayers: ConcurrentMap[Int, Player]) extends ConnectedPlayersMessage