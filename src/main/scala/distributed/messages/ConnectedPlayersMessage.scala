package distributed.messages

import java.util
import distributed.Player

trait ConnectedPlayersMessage {
  def connectedPlayers: util.Map[Int, Player]
}

object ConnectedPlayersMessage {
  def apply(connectedPlayers: util.Map[Int, Player]): ConnectedPlayersMessage =
    new ConnectedPlayersMessageImpl(connectedPlayers)
}

class ConnectedPlayersMessageImpl(override val connectedPlayers: util.Map[Int, Player]) extends ConnectedPlayersMessage