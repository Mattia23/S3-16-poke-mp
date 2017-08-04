package distributed.messages

import distributed.Player

/**
  * A PlayerMessage is used to communicate from a client to the server that a new player has connected
  */
trait PlayerMessage {
  /**
    * @return the new player connected as a Player
    */
  def player: Player
}

object PlayerMessage {
  def apply(player: Player): PlayerMessage = new PlayerMessageImpl(player)
}

/**
  * @inheritdoc
  * @param player the new player connected as a Player
  */
class PlayerMessageImpl(override val player: Player) extends PlayerMessage