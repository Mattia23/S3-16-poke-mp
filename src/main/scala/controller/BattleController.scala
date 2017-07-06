package controller

import java.util.concurrent.{Executors, TimeUnit}

import model.entities.{Owner, Trainer}
import model.game.{Battle, BattleImpl}
import view.View

import scala.util.Random

trait BattleController {
  def myPokemonAttacks(attackId: Int): Unit

  def pokemonWildAttacks(): Unit

  def trainerThrowPokeball(): Boolean
}

class BattleControllerImpl(val trainer: Trainer, val view: View) extends BattleController {
  val battle: Battle = new BattleImpl(trainer)
  battle.startBattleRound(trainer.getFirstAvailableFavouritePokemon)
  view.showBattle(battle.myPokemon,battle.wildPokemon,this)

  override def myPokemonAttacks(attackId: Int): Unit = {
    battle.round.myPokemonAttack(attackId)
    view.getBattlePanel.setPokemonLifeProgressBar(battle.wildPokemon.pokemonLife,Owner.WILD.id)
    if(!battle.battleFinished) {/*
      val executorService = Executors.newSingleThreadScheduledExecutor
      executorService.scheduleAtFixedRate(() => {
        pokemonWildAttacks()
      }, 3, 3, TimeUnit.SECONDS)*/
      val t: Thread = new Thread() {
        override def run() {
          Thread.sleep(3000)
          pokemonWildAttacks()
        }
      }
      t.start()
    } else {
      //qualcosa
    }
  }

  override def pokemonWildAttacks(): Unit = {
    battle.round.wildPokemonAttack(Random.nextInt(4))
    view.getBattlePanel.setPokemonLife()
    view.getBattlePanel.setPokemonLifeProgressBar(battle.myPokemon.pokemonLife,Owner.TRAINER.id)
    if(battle.battleFinished) {
      //qualcosa
    }
  }

  override def trainerThrowPokeball(): Boolean = {
    if(!battle.pokeballLaunched()) {
      val t: Thread = new Thread() {
        override def run() {
          Thread.sleep(3000)
          pokemonWildAttacks()
        }
      }
      t.start()
    }
    battle.pokeballLaunched()
  }

}