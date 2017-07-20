package distributed.messages

trait UserLogoutMessage {
  def userId: Int
}

object UserLogoutMessage {
  def apply(userId: Int): UserLogoutMessage = new UserLogoutMessageImpl(userId)
}

class UserLogoutMessageImpl(override val userId: Int) extends UserLogoutMessage
