package distributed.messages

/**
  * A TrainerDialogueMessage is used when two trainers try to fight against each other. A TrainerDialogueMessage
  * could be used to ask to an other trainer to fight or to answer to a fight request.
  */
trait TrainerDialogueMessage {
  def firstPlayerId: Int

  def firstPlayerName: String

  def secondPlayerId: Int

  def wantToFight: Boolean

  def isFirst: Boolean
}

object TrainerDialogueMessage {
  def apply(firstPlayerId: Int,
            firstPlayerName: String,
            secondPlayerId: Int,
            wantToFight: Boolean,
            isFirst: Boolean): TrainerDialogueMessage =
    new TrainerDialogueMessageImpl(firstPlayerId, firstPlayerName, secondPlayerId, wantToFight, isFirst)
}

/**
  * @inheritdoc
  * @param firstPlayerId id of the trainer that asks to fight
  * @param firstPlayerName username of the trainer tha asks to fight
  * @param secondPlayerId id of the trainer whom the request is directed to
  * @param wantToFight true if the trainer sending the message want to fight, false in the opposite case
  * @param isFirst true if the trainer is asking to fight, false if the trainer answers to a fight request
  */
class TrainerDialogueMessageImpl(override val firstPlayerId: Int,
                                 override val firstPlayerName: String,
                                 override val secondPlayerId: Int,
                                 override val wantToFight: Boolean,
                                 override val isFirst: Boolean) extends TrainerDialogueMessage