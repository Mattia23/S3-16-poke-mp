package controller

import database.remote.DBConnect
import distributed.client.BattleClientManager
import model.entities.{Owner, Trainer}
import model.environment.{Audio, AudioImpl}
import model.game.{Battle, TrainersBattle}
import utilities.Settings
import view.View

class DistributedBattleController(val controller: GameController, val view: View, val otherTrainerUsername: String) extends BattleController {
  private val OTHER_POKEMON: Int = 0
  private val MY_POKEMON: Int = 1
  private var battleManager: BattleClientManager = _
  private val otherTrainer: Trainer = DBConnect.getTrainerFromDB(otherTrainerUsername).get()
  private var battle: Battle = new TrainersBattle(controller.trainer,this,otherTrainer)
  private var timer: Thread = _
  battle.startBattleRound(controller.trainer.getFirstAvailableFavouritePokemon,otherTrainer.getFirstAvailableFavouritePokemon)
  showNewView()
  private val audio: Audio = new AudioImpl(Settings.POKEMON_WILD_SONG)
  audio.loop()

  def passManager(battleClientManager: BattleClientManager): Unit = {
    this.battleManager = battleClientManager
    this.battleManager.receiveBattleMessage()
  }
  override def myPokemonAttacks(attackId: Int): Unit = {
    battle.round.myPokemonAttack(attackId)
    view.getBattlePanel.setPokemonLifeProgressBar(battle.otherPokemon.pokemonLife,Owner.WILD.id)
    this.battleManager.sendBattleMessage(controller.trainer.id,battle.myPokemon.pokemon.id,attackId)
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
    println("IL NUOVO POKEMON NEL DISTRIBUTED CONTROLLER è "+battle.otherPokemon.pokemon.id)
    println("IL NUOVO POKEMON NEL DISTRIBUTED CONTROLLER è (PROVA)"+otherTrainer.getFirstAvailableFavouritePokemon)
    view.showBattle(battle.myPokemon,battle.otherPokemon,this)
  }

  private def pokemonIsDead(pokemonDeadId: Int): Unit = {
    timer = new Thread() {
      override def run() {
        view.getBattlePanel.pokemonIsDead(pokemonDeadId)
        Thread.sleep(3000)
        if(!battle.battleFinished) {
          println("nuovo round")
          showNewView()
        } else {
          println("la battaglia è finita")
          controller.resume()
        }
      }
    }
    timer.start()
  }

  override def trainerThrowPokeball(): Boolean = {false}
  override def trainerCanQuit(): Boolean = {
    resumeGame()
    true
  }
}
