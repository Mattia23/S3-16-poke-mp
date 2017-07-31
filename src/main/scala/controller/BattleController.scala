package controller

import distributed.client.BattleClientManager
import model.battle.{Battle, BattleImpl}
import model.entities.Owner
import model.environment.{Audio, AudioImpl}
import utilities.Settings
import view.View

import scala.util.Random

trait BattleController {
  def myPokemonAttacks(attackId: Int): Unit

  def otherPokemonAttacks(id: Int): Unit

  def otherPokemonChanges(newPokemonId: Int): Unit

  def getPokeballAvailableNumber: Int

  def trainerThrowPokeball(): Boolean

  def changePokemon(): Unit

  def pokemonToChangeIsSelected(id: Int): Unit

  def trainerCanQuit(): Boolean

  def resumeGame(): Unit

  def passManager(battleClientManager: BattleClientManager): Unit

  def isDistributedBattle: Boolean = false

  def yourPlayerIsFirst: Boolean = false
}

class BattleControllerImpl(val controller: GameController, val view: View) extends BattleController {
  private val WILD_POKEMON: Int = 0
  private val MY_POKEMON: Int = 1
  val battle: Battle = new BattleImpl(controller.trainer,this)
  private var timer: Thread = _
  battle.startBattleRound(controller.trainer.getFirstAvailableFavouritePokemon)
  showNewView()
  private val audio: Audio = new AudioImpl(Settings.POKEMON_WILD_SONG)
  audio.loop()

  override def myPokemonAttacks(attackId: Int): Unit = {
    battle.round.myPokemonAttack(attackId)
    view.getBattlePanel.setPokemonLifeProgressBar(battle.otherPokemon.pokemonLife,Owner.WILD.id)
    if(!battle.battleFinished) {
      timer = new Thread() {
        override def run() {
          Thread.sleep(3000)
          otherPokemonAttacks()
        }
      }
      timer.start()
    } else {
      pokemonIsDead(WILD_POKEMON)
    }
  }

  private def otherPokemonAttacks(): Unit = {
    battle.round.otherPokemonAttack(view.getBattlePanel.getOtherPokemonAttacks()(Random.nextInt(3)))
    view.getBattlePanel.setPokemonLife()
    view.getBattlePanel.setPokemonLifeProgressBar(battle.myPokemon.pokemonLife,Owner.TRAINER.id)
    if(battle.roundFinished) {
      pokemonIsDead(MY_POKEMON)
    }
  }

  override def changePokemon(): Unit = {
    view.showPokemonChoice(this, controller.trainer)
  }

  override def pokemonToChangeIsSelected(id: Int): Unit =  {
    battle.updatePokemonAndTrainer(Settings.BATTLE_EVENT_CHANGE_POKEMON)
    battle.startBattleRound(id)
    showNewView()
    pokemonWildAttacksAfterTrainerChoice()
  }

  override def getPokeballAvailableNumber: Int = {
    battle.pokeball
  }

  override def trainerThrowPokeball(): Boolean = {
    battle.pokeball_=(battle.pokeball-1)
    if(!battle.pokeballLaunched()) {
      new AudioImpl(Settings.CAPTURE_FAILED_SONG)
      pokemonWildAttacksAfterTrainerChoice()
      false
    } else {
      new AudioImpl(Settings.CAPTURE_SONG)
      timer = new Thread() {
        override def run() {
          battle.updatePokemonAndTrainer(Settings.BATTLE_EVENT_CAPTURE_POKEMON)
          Thread.sleep(3000)
          resumeGame()
        }
      }
      timer.start()
      true
    }
  }

  override def trainerCanQuit(): Boolean = {
    if (Random.nextDouble()<0.5) {
      battle.updatePokemonAndTrainer(Settings.BATTLE_EVENT_ESCAPE)
      resumeGame()
      true
    } else {
      pokemonWildAttacksAfterTrainerChoice()
      false
    }
  }

  override def resumeGame(): Unit = {
    audio.stop()
    controller.resume()
  }

  private def showNewView(): Unit = {
    view.showBattle(battle.myPokemon,battle.otherPokemon,this)
  }

  private def pokemonIsDead(pokemonDeadId: Int): Unit = {
    timer = new Thread() {
      override def run() {
        view.getBattlePanel.pokemonIsDead(pokemonDeadId)
        Thread.sleep(2000)
        if(pokemonDeadId==MY_POKEMON && !battle.battleFinished) {
          showNewView()
        } else if (pokemonDeadId==WILD_POKEMON) {
          resumeGame()
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
      otherPokemonAttacks()
      }
    }
    timer.start()
  }

  override def otherPokemonAttacks(id: Int): Unit = {}
  override def passManager(battleClientManager: BattleClientManager): Unit = {}
  override def otherPokemonChanges(newPokemonId: Int): Unit = {}
}