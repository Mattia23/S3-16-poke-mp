package distributed.messages

import distributed.Player

trait TrainerDialogueMessage {
  def firstPlayerId: Int

  def firstPlayerName: String

  def secondPlayerId: Int

  def wantToFight: Boolean

  def isFirst: Boolean
}

object TrainerDialogueMessage {
  def apply(firstPlayerId: Int, firstPlayerName: String, secondPlayerId: Int, wantToFight: Boolean, isFirst: Boolean): TrainerDialogueMessage =
    new TrainerDialogueMessageImpl(firstPlayerId, firstPlayerName, secondPlayerId, wantToFight, isFirst)
}

class TrainerDialogueMessageImpl(override val firstPlayerId: Int,
                                 override val firstPlayerName: String,
                                 override val secondPlayerId: Int,
                                 override val wantToFight: Boolean,
                                 override val isFirst: Boolean) extends TrainerDialogueMessage