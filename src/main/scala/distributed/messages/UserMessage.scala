package distributed.messages

import distributed.Player

trait UserMessage {
  def user: Player
}

object UserMessage {
  def apply(user: Player): UserMessage = new UserMessageImpl(user)
}

class UserMessageImpl(override val user: Player) extends UserMessage