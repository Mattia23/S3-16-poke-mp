package model.game

import database.remote.DBConnect
import model.entities.{PokemonBehaviour, PokemonBehaviourImpl, PokemonWithLife}

import scala.util.Random

trait BattleRound {
  def pokeballLaunched(): Boolean

  def myPokemonAttack(idAttack: Int): Unit

  def otherPokemonAttack(attack: Int): Unit

  def updatePokemon(): Unit

  def updateOtherPokemon(): Unit
}

class BattleRoundImpl(myPokemon: PokemonWithLife, myPokemonIdDB: Int, otherPokemon: PokemonWithLife, battle: Battle) extends  BattleRound{
  var myPokemonBehaviour: PokemonBehaviour = new PokemonBehaviourImpl(myPokemon)
  var otherPokemonBehaviour: PokemonBehaviour = new PokemonBehaviourImpl(otherPokemon)

  override def pokeballLaunched(): Boolean = {
    if((otherPokemon.pokemonLife * otherPokemon.pokemon.experiencePoints) / battle.trainer.level < Random.nextInt(300) + 1500) {
      otherPokemonBehaviour.insertPokemonIntoDB(battle.trainer.id)
      return true
    }
    false
  }

  override def myPokemonAttack(idAttack: Int): Unit = {
    val damage: Int = myPokemonBehaviour.launchAttack(idAttack)
    otherPokemonBehaviour.undergoAttack(damage)
    if(!otherPokemonBehaviour.isAlive){
      myPokemonBehaviour.growExperiencePoints(otherPokemonBehaviour.giveExperiencePoints)
      updatePokemon()
      battle.myPokemonKillsOtherPokemon(true)
    }
  }

  override def otherPokemonAttack(attack: Int): Unit ={
    val damage: Int = otherPokemonBehaviour.launchAttack(attack)
    myPokemonBehaviour.undergoAttack(damage)
    if(!myPokemonBehaviour.isAlive){
      updatePokemon()
      battle.myPokemonKillsOtherPokemon(false)
    }
  }

  override def updatePokemon(): Unit = {
    myPokemonBehaviour.updatePokemonTrainer(myPokemonIdDB)
  }

  override def updateOtherPokemon(): Unit = {
    otherPokemonBehaviour.updatePokemonTrainer(battle.getOtherPokemonId)
  }
}
