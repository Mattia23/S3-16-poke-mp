package distributed.messages

trait BattleMessage {
  def trainerId: Int

  def pokemonId: Int

  def attackId: Int
}

object BattleMessage {
  def apply(trainerId: Int, pokemonId: Int, attackId: Int): BattleMessage = new BattleMessageImpl(trainerId, pokemonId, attackId)
}

class BattleMessageImpl(override val trainerId: Int, override val pokemonId: Int, override val attackId: Int) extends BattleMessage