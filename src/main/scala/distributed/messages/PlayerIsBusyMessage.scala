package distributed.messages

trait PlayerIsBusyMessage{
  def userId: Int

  def isBusy: Boolean
}

object PlayerIsBusyMessage {
  def apply(userId: Int, isBusy: Boolean): PlayerIsBusyMessage = new PlayerIsBusyMessageImpl(userId, isBusy)
}

class PlayerIsBusyMessageImpl(override val userId: Int, override val isBusy: Boolean) extends PlayerIsBusyMessage