package distributed.messages

import distributed.User

trait UserMessage {
  def user: User
}

object UserMessage {
  def apply(user: User): UserMessage = new UserMessageImpl(user)
}

class UserMessageImpl(override val user: User) extends UserMessage