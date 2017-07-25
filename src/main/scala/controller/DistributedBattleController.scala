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
  private val battle: Battle = new TrainersBattle(controller.trainer,this,otherTrainer)
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
  }

  override def otherPokemonAttacks(id: Int): Unit = {
    battle.round.otherPokemonAttack(id)
    view.getBattlePanel.setPokemonLife()
    view.getBattlePanel.setPokemonLifeProgressBar(battle.myPokemon.pokemonLife,Owner.TRAINER.id)
    if(battle.myPokemon.pokemonLife == 0) {
      myPokemonIsDead
    }
  }

  private def myPokemonChanges(newPokemonId: Int): Unit = {
    battle.round.updatePokemon()
    battle.startBattleRound(newPokemonId,battle.getOtherPokemonId)
    showNewView()
  }

  override def otherPokemonChanges(newPokemonId: Int): Unit = {
    battle.round.updatePokemon()
    battle.startBattleRound(battle.getMyPokemonId,newPokemonId)
    showNewView()
  }

  override def changePokemon(): Unit = {
    view.showPokemonChoice(this, controller.trainer)
  }

  override def pokemonToChangeIsSelected(id: Int): Unit =  {
    battle.updatePokemonAndTrainer(Settings.BATTLE_EVENT_CHANGE_POKEMON)
    battle.startBattleRound(id)
    showNewView()
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

  private def myPokemonIsDead: Unit = {
    view.getBattlePanel.pokemonIsDead(MY_POKEMON)
    val nextPokemon: Int = controller.trainer.getFirstAvailableFavouritePokemon
    println(nextPokemon)
    if(nextPokemon > 0) {
      battleManager.sendBattleMessage(controller.trainer.id,nextPokemon,0)
      myPokemonChanges(nextPokemon)
    } else {
      battleManager.sendBattleMessage(controller.trainer.id,0,0)
      controller.resume()
    }
  }

  override def trainerThrowPokeball(): Boolean = {false}
  override def trainerCanQuit(): Boolean = {
    resumeGame()
    true
  }
}
