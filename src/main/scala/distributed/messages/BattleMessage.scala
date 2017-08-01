package distributed.messages

/**
  * A BattleMessage is used during a battle between two trainers to send messages from one trainer to an other. The
  * types of message that a trainer can send (or receive) are an attack from the Pokemon, a Pokemon change or the
  * end of the battle.
  */
trait BattleMessage {
  def trainerId: Int

  def pokemonId: Int

  def attackId: Int
}

object BattleMessage {
  def apply(trainerId: Int, pokemonId: Int, attackId: Int): BattleMessage = new BattleMessageImpl(trainerId, pokemonId, attackId)
}

/**
  * @inheritdoc
  * @param trainerId the id of the trainer id of the trainer whom the message is directed to
  * @param pokemonId the current Pokemon id that is fighting
  * @param attackId the attack id from the fighting Pokemon
  */
class BattleMessageImpl(override val trainerId: Int,
                        override val pokemonId: Int,
                        override val attackId: Int) extends BattleMessage