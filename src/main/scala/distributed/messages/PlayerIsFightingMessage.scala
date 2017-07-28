package distributed.messages

trait PlayerIsFightingMessage{
  def userId: Int

  def isFighting: Boolean
}

object PlayerIsFightingMessage {
  def apply(userId: Int, isFighting: Boolean): PlayerIsFightingMessage = new PlayerIsFightingMessageImpl(userId, isFighting)
}

class PlayerIsFightingMessageImpl(override val userId: Int, override val isFighting: Boolean) extends PlayerIsFightingMessage