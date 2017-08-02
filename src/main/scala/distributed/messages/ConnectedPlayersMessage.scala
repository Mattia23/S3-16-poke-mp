package distributed.messages

import java.util
import distributed.Player

/**
  * A ConnectedPlayersMessage is used in order to send a map of all connected players to clients from the server
  */
trait ConnectedPlayersMessage {
  /**
    * @return The map containing all the current connected players as util.Map
    */
  def connectedPlayers: util.Map[Int, Player]
}

object ConnectedPlayersMessage {
  def apply(connectedPlayers: util.Map[Int, Player]): ConnectedPlayersMessage =
    new ConnectedPlayersMessageImpl(connectedPlayers)
}

/**
  * @inheritdoc
  * @param connectedPlayers The map containing all the current connected players
  */
class ConnectedPlayersMessageImpl(override val connectedPlayers: util.Map[Int, Player]) extends ConnectedPlayersMessage