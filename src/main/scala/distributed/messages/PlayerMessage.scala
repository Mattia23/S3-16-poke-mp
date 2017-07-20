package distributed.messages

import distributed.User

trait PlayerMessage {
  def player: User
}

object PlayerMessage {
  def apply(player: User): PlayerMessage = new PlayerMessageImpl(player)
}

class PlayerMessageImpl(override val player: User) extends PlayerMessage