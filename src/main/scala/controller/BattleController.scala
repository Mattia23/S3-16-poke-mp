package controller

import java.util.concurrent.{Executors, TimeUnit}

import model.entities.{Owner, Trainer}
import model.game.{Battle, BattleImpl}
import view.View

import scala.util.Random

trait BattleController {
  def myPokemonAttacks(attackId: Int): Unit

  def pokemonWildAttacks(): Unit

  def getPokeballAvailableNumber(): Int

  def trainerThrowPokeball(): Boolean

  def changePokemon(): Unit

  def pokemonToChangeIsSelected(id: Int): Unit

  def trainerCanQuit(): Boolean
}

class BattleControllerImpl(val controller: GameController, val trainer: Trainer, val view: View) extends BattleController {
  val battle: Battle = new BattleImpl(trainer,this)
  battle.startBattleRound(trainer.getFirstAvailableFavouritePokemon)
  showNewView
  private var timer: Thread = _

  override def myPokemonAttacks(attackId: Int): Unit = {
    battle.round.myPokemonAttack(attackId)
    view.getBattlePanel.setPokemonLifeProgressBar(battle.wildPokemon.pokemonLife,Owner.WILD.id)
    if(!battle.battleFinished) {
      timer = new Thread() {
        override def run() {
          Thread.sleep(3000)
          pokemonWildAttacks()
        }
      }
      timer.start()
    } else {
      pokemonIsDead(0)
    }
  }

  override def pokemonWildAttacks(): Unit = {
    battle.round.wildPokemonAttack(Random.nextInt(3)+1)
    view.getBattlePanel.setPokemonLife()
    view.getBattlePanel.setPokemonLifeProgressBar(battle.myPokemon.pokemonLife,Owner.TRAINER.id)
    if(battle.battleFinished) {
      pokemonIsDead(1)
    }
  }

  override def changePokemon(): Unit = {
    view.showPokemonChoice(this)
  }

  override def pokemonToChangeIsSelected(id: Int): Unit =  {
    battle.updatePokemon()
    battle.startBattleRound(id)
    showNewView
    pokemonWildAttacksAfterTrainerChoice()
  }

  override def getPokeballAvailableNumber(): Int = {
    battle.pokeball
  }

  override def trainerThrowPokeball(): Boolean = {
    battle.pokeball_=(battle.pokeball-1)
    if(!battle.pokeballLaunched()) {
      pokemonWildAttacksAfterTrainerChoice()
      false
    } else {
      timer = new Thread() {
        override def run() {
          battle.updatePokemon()
          Thread.sleep(3000)
          controller.resumeGame()
        }
      }
      timer.start()
      true
    }
  }

  override def trainerCanQuit(): Boolean = {
    if (Random.nextDouble()<0.5) {
      battle.updatePokemon()
      controller.resumeGame()
      true
    } else {
      pokemonWildAttacksAfterTrainerChoice()
      false
    }
  }

  private def showNewView(): Unit = {
    view.showBattle(battle.myPokemon,battle.wildPokemon,this)
  }

  private def pokemonIsDead(index: Int): Unit = {
    timer = new Thread() {
      override def run() {
        view.getBattlePanel.pokemonIsDead(index)
        Thread.sleep(2000)
        if(index==1) {
          showNewView
        } else if (index==0) {
          controller.resumeGame()
        }

      }
    }
    timer.start()
  }

  private def pokemonWildAttacksAfterTrainerChoice(): Unit = {
    timer = new Thread() {
      override def run() {
      view.getBattlePanel.pokemonWildAttacksAfterTrainerChoice()
      Thread.sleep(2000)
      pokemonWildAttacks()
      }
    }
    timer.start()
  }
}