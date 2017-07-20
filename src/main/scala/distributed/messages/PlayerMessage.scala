package distributed.messages

import distributed.Player

trait PlayerMessage {
  def player: Player
}

object PlayerMessage {
  def apply(player: Player): PlayerMessage = new PlayerMessageImpl(player)
}

class PlayerMessageImpl(override val player: Player) extends PlayerMessage