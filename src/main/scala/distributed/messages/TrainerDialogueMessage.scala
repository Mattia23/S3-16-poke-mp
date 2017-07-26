package distributed.messages

import distributed.Player

trait TrainerDialogueMessage {
  def player: Player

  def otherPlayer: Player

  def wantToFight: Boolean
}

object TrainerDialogueMessage {
  def apply(player: Player, otherPlayer: Player, wantToFight: Boolean): TrainerDialogueMessage =
    new TrainerDialogueMessageImpl(player, otherPlayer, wantToFight)
}

class TrainerDialogueMessageImpl(override val player: Player,
                                 override val otherPlayer: Player,
                                 override val wantToFight: Boolean) extends TrainerDialogueMessage