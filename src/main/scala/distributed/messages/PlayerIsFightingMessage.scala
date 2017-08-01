package distributed.messages

/**
  * A PlayerIsFightingMessage is used to inform all the connected users that the trainer sending the message is fighting
  * (or busy), so he can't fight against an other trainer
  */
trait PlayerIsFightingMessage{
  def userId: Int

  def isFighting: Boolean
}

object PlayerIsFightingMessage {
  def apply(userId: Int, isFighting: Boolean): PlayerIsFightingMessage = new PlayerIsFightingMessageImpl(userId, isFighting)
}

/**
  * @inheritdoc
  * @param userId is of the user sending the message
  * @param isFighting true if the trainer is busy, false in the opposite case
  */
class PlayerIsFightingMessageImpl(override val userId: Int, override val isFighting: Boolean) extends PlayerIsFightingMessage