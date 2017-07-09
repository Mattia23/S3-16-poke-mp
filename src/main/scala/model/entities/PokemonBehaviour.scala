package model.entities

import database.local.PokedexConnect
import database.remote.DBConnect

import scala.collection.immutable.HashMap

trait PokemonBehaviour {

  def launchAttack(attackId: Int): Int

  def undergoAttack(damage: Int): Unit

  def isAlive: Boolean

  def giveExperiencePoints: Int

  def growExperiencePoints(points: Int): Unit

  def updatePokemonTrainer(databaseId: Int): Unit

  def insertPokemonIntoDB(trainerId: Int): Unit
}

class PokemonBehaviourImpl(var pokemonWithLife: PokemonWithLife) extends PokemonBehaviour {
  private val ATTACK_PERCENTAGE: Double = 0.1
  private val EXPERIENCE_PERCENTAGE: Double = 0.75
  private var _isAlive: Boolean = true
  private var pokemonExperienceGrown = 0

  override def launchAttack(attackId: Int): Int = {
    val attack: (String,Integer) = PokedexConnect.getPokemonAttack(attackId).get()
    (attack._2 * pokemonWithLife.pokemon.level * ATTACK_PERCENTAGE).toInt
  }

  override def undergoAttack(damage: Int): Unit = {
    val pokemonStatus: PokemonStatus.Value = pokemonWithLife.loseLifePoints(damage)
    if(pokemonStatus == PokemonStatus.DEATH) { _isAlive = false }
  }

  override def isAlive = this._isAlive

  override def giveExperiencePoints: Int = {
    (pokemonWithLife.pokemon.experiencePoints * EXPERIENCE_PERCENTAGE).toInt
  }

  override def growExperiencePoints(points: Int): Unit = {
    this.pokemonExperienceGrown = points //DA MODIFICARE (TENERE CONTO DEL LIVELLO)
  }

  private def checkIfLevelGrows: (Int,Int,Int,Int) = {
    if(this.pokemonExperienceGrown > 0) {
      val expPoints = this.pokemonWithLife.pokemon.experiencePoints
      var newExpPoints = expPoints + this.pokemonExperienceGrown
      val levelExp = this.pokemonWithLife.pokemon.levelExperience + expPoints
      if (newExpPoints >= levelExp+4) {
        (checkEvolution(this.pokemonWithLife.pokemon.level+1),
          this.pokemonWithLife.pokemon.level+1,
          this.pokemonWithLife.pokemon.experiencePoints+4,
          newExpPoints - levelExp + 4)
      } else {
        (this.pokemonWithLife.pokemon.id,this.pokemonWithLife.pokemon.level,this.pokemonWithLife.pokemon.experiencePoints,newExpPoints)
      }
    } else {
      (this.pokemonWithLife.pokemon.id, this.pokemonWithLife.pokemon.level, this.pokemonWithLife.pokemon.experiencePoints,
        this.pokemonWithLife.pokemon.levelExperience)
    }

  }

  private def checkEvolution(newLevel: Int): Int = {
    val result = PokedexConnect.pokemonHasToEvolve(this.pokemonWithLife.pokemon.id ,newLevel)
    if(result._1) {
      result._2.get()
    } else {
      this.pokemonWithLife.pokemon.id
    }
  }

  override def updatePokemonTrainer(databaseId: Int): Unit = {
    val pokemonData = checkIfLevelGrows
    val pokemon = HashMap(
      "id" -> pokemonData._1.toString,
      "level" -> pokemonData._2.toString,
      "points" -> pokemonData._3.toString,
      "level_exp" -> pokemonData._4.toString,
      "life" -> this.pokemonWithLife.pokemonLife.toString
    )
    DBConnect.updatePokemon(pokemon,databaseId)
  }

  override def insertPokemonIntoDB(trainerId: Int): Unit = {
    DBConnect.insertWildPokemonIntoDB(pokemonWithLife: PokemonWithLife, trainerId: Int)
  }
}
