package distributed.messages

trait TrainerDialogueMessage {
  def trainerId: Int

  def otherTrainerId: Int

  def wantToFight: Boolean
}

object TrainerDialogueMessage {
  def apply(trainerId: Int, otherTrainerId: Int, wantToFight: Boolean): TrainerDialogueMessage =
    new TrainerDialogueMessageImpl(trainerId, otherTrainerId, wantToFight)
}

class TrainerDialogueMessageImpl(override val trainerId: Int,
                                 override val otherTrainerId: Int,
                                 override val wantToFight: Boolean) extends TrainerDialogueMessage