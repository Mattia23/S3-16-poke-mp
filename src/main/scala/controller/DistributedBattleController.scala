package controller

import database.remote.DBConnect
import model.entities.{Owner, Trainer}
import model.environment.{Audio, AudioImpl}
import model.game.{Battle, TrainersBattle}
import utilities.Settings
import view.View

class DistributedBattleController(val controller: GameController, val view: View, val otherTrainerUsername: String) extends BattleController {
  private val OTHER_POKEMON: Int = 0
  private val MY_POKEMON: Int = 1
  private val otherTrainer: Trainer = DBConnect.getTrainerFromDB(otherTrainerUsername).get()
  val battle: Battle = new TrainersBattle(controller.trainer,this,otherTrainer)
  private var timer: Thread = _
  battle.startBattleRound(controller.trainer.getFirstAvailableFavouritePokemon,otherTrainer.getFirstAvailableFavouritePokemon)
  showNewView()
  private val audio: Audio = new AudioImpl(Settings.POKEMON_WILD_SONG)
  audio.loop()

  override def myPokemonAttacks(attackId: Int): Unit = {
    battle.round.myPokemonAttack(attackId)
    view.getBattlePanel.setPokemonLifeProgressBar(battle.otherPokemon.pokemonLife,Owner.WILD.id)
    if(battle.battleFinished || battle.roundFinished) {
      pokemonIsDead(OTHER_POKEMON)
    }
  }

  override def otherPokemonAttacks(id: Int): Unit = {
    battle.round.otherPokemonAttack(id)
    view.getBattlePanel.setPokemonLife()
    view.getBattlePanel.setPokemonLifeProgressBar(battle.myPokemon.pokemonLife,Owner.TRAINER.id)
    if(battle.battleFinished || battle.roundFinished) {
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
    //pokemonWildAttacksAfterTrainerChoice()
  }

  override def getPokeballAvailableNumber: Int = {
    battle.pokeball
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
        if(!battle.battleFinished) {
          showNewView()
        } else {
          controller.resume()
        }
      }
    }
    timer.start()
  }

  override def trainerThrowPokeball(): Boolean = {false}
  override def trainerCanQuit(): Boolean = {false}
}
