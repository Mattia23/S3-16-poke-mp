package model.entities

trait PokemonBehaviour {
  def launchAttack(attackId: Int, pokemonLevel: Int): Int

  def undergoAttack(damage: Int): Unit

  def isAlive: Boolean

  def giveExperiencePoints: Int

  def growExperiencePoints(points: Int): Unit

  def checkExperiencePoint: Unit

  def checkEvolutionLevel: Unit

  def updatePokemonTrainer: Boolean

  def insertPokemonIntoDB: Boolean
}
