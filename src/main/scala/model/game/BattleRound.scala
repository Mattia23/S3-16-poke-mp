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
    if((wildPokemon.pokemonLife * wildPokemon.pokemon.experiencePoints) / battle.trainer.level < Random.nextInt(100) + 400) {
      wildPokemonBehaviour.insertPokemonIntoDB(battle.trainer.id)
      return true
    }
    false
  }

  override def myPokemonAttack(idAttack: Int): Unit = {
    println("Il mio pokemon attacca")
    val damage: Int = myPokemonBehaviour.launchAttack(idAttack)
    wildPokemonBehaviour.undergoAttack(damage)
    if(wildPokemonBehaviour.isAlive){
      println("Wild pokemon vivo")
      wildPokemonAttack(Random.nextInt(4))
    } else {
      println("Wild pokemon morto")
      myPokemonBehaviour.growExperiencePoints(wildPokemonBehaviour.giveExperiencePoints)
      myPokemonBehaviour.updatePokemonTrainer(myPokemonIdDB)
      battle.battleFinished(true)
    }
  }

  private def wildPokemonAttack(attack: Int): Unit ={
    println("Il selvatico attacca")
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
