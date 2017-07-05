package model.game

import database.remote.DBConnect
import model.entities.{PokemonBehaviour, PokemonBehaviourImpl, PokemonWithLife}

import scala.util.Random

trait BattleRound {
  def pokeballLaunched(): Boolean

  def myPokemonAttack(idAttack: Int): Unit

}

class BattleRoundImpl(myPokemon: PokemonWithLife, myPokemonIdDB: Int, wildPokemon: PokemonWithLife, battle: Battle) extends  BattleRound{
  var myPokemonBehaviour: PokemonBehaviour = new PokemonBehaviourImpl(myPokemon)
  var wildPokemonBehaviour: PokemonBehaviour = new PokemonBehaviourImpl(wildPokemon)

  override def pokeballLaunched(): Boolean = {
    (wildPokemon.pokemonLife * wildPokemon.pokemon.experiencePoints) / battle.trainer.level < Random.nextInt(100) + 400
  }

  override def myPokemonAttack(idAttack: Int): Unit = {
    val damage: Int = myPokemonBehaviour.launchAttack(idAttack)
    wildPokemonBehaviour.undergoAttack(damage)
    if(wildPokemonBehaviour.isAlive){
      wildPokemonAttack(Random.nextInt(4))
    } else {
      myPokemonBehaviour.updatePokemonTrainer(myPokemonIdDB)
      battle.battleFinished(true)
    }
  }

  private def wildPokemonAttack(attack: Int): Unit ={
    new Thread(() => {
      Thread.sleep(4000)
      val damage: Int = wildPokemonBehaviour.launchAttack(attack)
      myPokemonBehaviour.undergoAttack(damage)
      if(!myPokemonBehaviour.isAlive){
        myPokemonBehaviour.updatePokemonTrainer(myPokemonIdDB)
        battle.battleFinished(false)
      }
    })

  }
}
