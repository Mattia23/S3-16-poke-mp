package distributed.messages

trait PlayerLogoutMessage {
  def userId: Int
}

object PlayerLogoutMessage {
  def apply(userId: Int): PlayerLogoutMessage = new PlayerLogoutMessageImpl(userId)
}

class PlayerLogoutMessageImpl(override val userId: Int) extends PlayerLogoutMessage
