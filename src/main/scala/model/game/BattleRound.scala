package model.game

import model.entities.{PokemonBehaviour, PokemonBehaviourImpl, PokemonWithLife}

import scala.util.Random

trait BattleRound {
  def pokeballLaunched(): Boolean

  def myPokemonAttack(idAttack: Int): Unit

  def wildPokemonAttack(attack: Int): Unit

  def updatePokemon(): Unit
}

class BattleRoundImpl(myPokemon: PokemonWithLife, myPokemonIdDB: Int, wildPokemon: PokemonWithLife, battle: Battle) extends  BattleRound{
  var myPokemonBehaviour: PokemonBehaviour = new PokemonBehaviourImpl(myPokemon)
  var wildPokemonBehaviour: PokemonBehaviour = new PokemonBehaviourImpl(wildPokemon)

  override def pokeballLaunched(): Boolean = {
    if((wildPokemon.pokemonLife * wildPokemon.pokemon.experiencePoints) / battle.trainer.level < Random.nextInt(300) + 1500) {
      wildPokemonBehaviour.insertPokemonIntoDB(battle.trainer.id)
      return true
    }
    false
  }

  override def myPokemonAttack(idAttack: Int): Unit = {
    val damage: Int = myPokemonBehaviour.launchAttack(idAttack)
    wildPokemonBehaviour.undergoAttack(damage)
    if(!wildPokemonBehaviour.isAlive){
      myPokemonBehaviour.growExperiencePoints(wildPokemonBehaviour.giveExperiencePoints)
      updatePokemon()
      battle.myPokemonKillsWildPokemon(true)
    }
  }

  override def wildPokemonAttack(attack: Int): Unit ={
    val damage: Int = wildPokemonBehaviour.launchAttack(attack)
    myPokemonBehaviour.undergoAttack(damage)
    if(!myPokemonBehaviour.isAlive){
      updatePokemon()
      battle.myPokemonKillsWildPokemon(false)
    }
  }

  override def updatePokemon(): Unit = {
    myPokemonBehaviour.updatePokemonTrainer(myPokemonIdDB)
  }
}
