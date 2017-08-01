package distributed.messages

/**
  * A PlayerIsBusyMessage is used to inform all the connected users that the trainer sending the message is busy,
  * so he can't fight against an other trainer
  */
trait PlayerIsBusyMessage{
  def userId: Int

  def isBusy: Boolean
}

object PlayerIsBusyMessage {
  def apply(userId: Int, isBusy: Boolean): PlayerIsBusyMessage = new PlayerIsBusyMessageImpl(userId, isBusy)
}

/**
  * @inheritdoc
  * @param userId is of the user sending the message
  * @param isBusy true if the trainer is busy, false in the opposite case
  */
class PlayerIsBusyMessageImpl(override val userId: Int, override val isBusy: Boolean) extends PlayerIsBusyMessage