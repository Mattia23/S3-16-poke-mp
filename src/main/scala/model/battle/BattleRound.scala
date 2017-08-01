package model.battle

import model.entities.{PokemonBehaviour, PokemonBehaviourImpl, PokemonWithLife}
import scala.util.Random

/**
  * A BattleRound represents a part of a battle between two Pokemons. Every time two different Pokemon start fighting
  * (trainer vs wild pokemon or trainer vs trainer) a new BattleRound must be created to manage both Pokemons' attacks.
  */
trait BattleRound {
  /**
    * Return true if the trainer succeded in capturing the wild Pokemon in the round
    * @return true if the wild Pokemon is captured, false in the opposite case
    */
  def pokemonIsCaptured(): Boolean

  /**
    * Make trainer's pokemon fighting in the current round attack with the attack id passed
    * and make opposite trainer's Pokemon undergo the attack loosing life points
    * @param attackId the attack id
    */
  def myPokemonAttack(attackId: Int): Unit

  /**
    * Make opposite trainer's pokemon fighting in the current round attack with the attack id passed
    * and make trainer's Pokemon undergo the attack loosing life points
    * @param attackId the attack id
    */
  def otherPokemonAttack(attackId: Int): Unit

  /**
    * Update trainer's Pokemon attributes (life, level, experience) in the remote database
    */
  def updatePokemon(): Unit

  /**
    * Update opposite trainer's Pokemon attributes (life, level, experience) in the remote database
    */
  def updateOtherPokemon(): Unit
}

/**
  * @inheritdoc
  * @param myPokemon trainer's Pokemon as an instance of PokemonWithLife
  * @param myPokemonIdDB trainer's Pokemon Id of the remote database
  * @param otherPokemon opposite Pokemon as an istance of PokemonWithLife (could be a wild Pokemon or an other trainer's
  *                     Pokemon
  * @param battle instance of the Battle which this round is part of
  */
class BattleRoundImpl(private val myPokemon: PokemonWithLife,
                      private val myPokemonIdDB: Int,
                      private val otherPokemon: PokemonWithLife,
                      private val battle: Battle) extends  BattleRound{
  var myPokemonBehaviour: PokemonBehaviour = new PokemonBehaviourImpl(myPokemon)
  var otherPokemonBehaviour: PokemonBehaviour = new PokemonBehaviourImpl(otherPokemon)

  /**
    * @inheritdoc
    */
  override def pokemonIsCaptured(): Boolean = {
    if((otherPokemon.pokemonLife * otherPokemon.pokemon.experiencePoints) / battle.trainer.level < Random.nextInt(300) + 1500) {
      otherPokemonBehaviour.insertPokemonIntoDB(battle.trainer.id)
      return true
    }
    false
  }

  /**
    * @inheritdoc
    */
  override def myPokemonAttack(attackId: Int): Unit = {
    val damage: Int = myPokemonBehaviour.launchAttack(attackId)
    otherPokemonBehaviour.undergoAttack(damage)
    if(!otherPokemonBehaviour.isAlive){
      myPokemonBehaviour.growExperiencePoints(otherPokemonBehaviour.giveExperiencePoints)
      updatePokemon()
      battle.myPokemonKillsOtherPokemon(true)
    }
  }

  /**
    * @inheritdoc
    */
  override def otherPokemonAttack(attackId: Int): Unit ={
    val damage: Int = otherPokemonBehaviour.launchAttack(attackId)
    myPokemonBehaviour.undergoAttack(damage)
    if(!myPokemonBehaviour.isAlive){
      updatePokemon()
      battle.myPokemonKillsOtherPokemon(false)
    }
  }

  /**
    * @inheritdoc
    */
  override def updatePokemon(): Unit = {
    myPokemonBehaviour.updatePokemonTrainer(myPokemonIdDB)
  }

  /**
    * @inheritdoc
    */
  override def updateOtherPokemon(): Unit = {
    otherPokemonBehaviour.updatePokemonTrainer(battle.getOtherPokemonId)
  }
}
