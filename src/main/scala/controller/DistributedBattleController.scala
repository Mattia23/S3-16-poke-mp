package controller

import database.remote.DBConnect
import distributed.client.BattleClientManager
import model.battle.{TrainersBattle, TrainersBattleImpl}
import model.entities.{Owner, Trainer}
import model.environment.Audio
import utilities.Settings
import view.View

/**
  * DistributedBattleController permits to manage every event during a battle against an other trainer (attacks
  * during the fight, allow trainer to change his Pokemons, sending message to the other trainer...)
  */
trait DistributedBattleController extends BattleController {
  /**
    * Set the BattleClientManager that manage battle messages between two trainers
    * @param battleClientManager Istance if BattleClientManager
    */
  def passManager(battleClientManager: BattleClientManager): Unit

  /**
    * Manage the situation in which trainer's Pokemon undergo an attack from the other trainer's Pokemon updating
    * life points
    * @param id the id of the attack that comes from the other Pokemon
    */
  def otherPokemonAttacks(id: Int): Unit

  /**
    * Update trainer's Pokemon, create a new BattleRound and show the new view with the Pokemon chosen by the
    * other trainer
    * @param newPokemonId the id of the Pokemon chosen by the other trainer
    */
  def otherPokemonChanges(newPokemonId: Int): Unit
}

/**
  * @inheritdoc
  * @param controller istance of GameController
  * @param view instance if View
  * @param otherTrainerUsername other trainer username
  * @param playerIsFirst Boolean representing if playing trainer is the first to attack or not
  */
class DistributedBattleControllerImpl(private val controller: GameController,
                                      private val view: View,
                                      private val otherTrainerUsername: String,
                                      private val playerIsFirst: Boolean) extends DistributedBattleController {
  private val MY_POKEMON: Int = 1
  private var battleManager: BattleClientManager = _
  private val otherTrainer: Trainer = DBConnect.getTrainerFromDB(otherTrainerUsername).get()
  private val battle: TrainersBattle = new TrainersBattleImpl(controller.trainer,this,otherTrainer)
  battle.startBattleRound(controller.trainer.getFirstAvailableFavouritePokemon,otherTrainer.getFirstAvailableFavouritePokemon)
  showNewView()
  private val audio: Audio = Audio(Settings.Audio.POKEMON_WILD_SONG)
  audio.loop()

  /**
    * @inheritdoc
    * @param battleClientManager Istance if BattleClientManager
    */
  def passManager(battleClientManager: BattleClientManager): Unit = {
    this.battleManager = battleClientManager
    this.battleManager.receiveBattleMessage()
  }

  /**
    * Manage the attack of trainer's Pokemon, update view and send a message to the other trainer with the attack id
    * @param attackId the id of the attack that the trainer chose
    */
  override def myPokemonAttacks(attackId: Int): Unit = {
    battle.round.myPokemonAttack(attackId)
    view.getBattlePanel.setPokemonLifeProgressBar(battle.otherPokemon.pokemonLife,Owner.WILD.id)
    this.battleManager.sendBattleMessage(controller.trainer.id,battle.myPokemon.pokemon.id,attackId)
  }

  /**
    * @inheritdoc
    * @param id the id of the attack that comes from the other Pokemon
    */
  override def otherPokemonAttacks(id: Int): Unit = {
    battle.round.otherPokemonAttack(id)
    view.getBattlePanel.setPokemonLife()
    view.getBattlePanel.setPokemonLifeProgressBar(battle.myPokemon.pokemonLife,Owner.TRAINER.id)
    if(battle.myPokemon.pokemonLife == 0) {
      myPokemonIsDead()
    }
  }

  private def myPokemonChanges(newPokemonId: Int): Unit = {
    battle.round.updatePokemon()
    battle.startBattleRound(newPokemonId,battle.getOtherPokemonId)
    showNewView()
  }

  /**
    * @inheritdoc
    * @param newPokemonId the id of the Pokemon chosen by the other trainer
    */
  override def otherPokemonChanges(newPokemonId: Int): Unit = {
    battle.round.updatePokemon()
    battle.startBattleRound(battle.getMyPokemonId,newPokemonId)
    showNewView()
  }

  /**
    * @inheritdoc
    */
  override def changePokemon(): Unit = {
    view.showPokemonChoice(this, controller.trainer)
  }

  /**
    * After trainer chose a new Pokemon to fight, update Pokemon, view and send a battle message to the other trainer to
    * inform him about the change
    * @param id id of the new trainer's Pokemon
    */
  override def pokemonToChangeIsSelected(id: Int): Unit =  {
    battle.round.updateOtherPokemon()
    myPokemonChanges(id)
    battleManager.sendBattleMessage(controller.trainer.id,id,0)
  }

  /**
    * @inheritdoc
    * @return available Pokeball number
    */
  override def getPokeballAvailableNumber: Int = {
    battle.pokeball
  }

  /**
    * @inheritdoc
    */
  override def resumeGame(): Unit = {
    audio.stop()
    controller.resume()
  }

  private def showNewView(): Unit = {
    view.showBattle(battle.myPokemon,battle.otherPokemon,this)
  }

  private def myPokemonIsDead(): Unit = {
    view.getBattlePanel.pokemonIsDead(MY_POKEMON)
    val nextPokemon: Int = controller.trainer.getFirstAvailableFavouritePokemon
    if(nextPokemon > 0) {
      battleManager.sendBattleMessage(controller.trainer.id,nextPokemon,0)
      myPokemonChanges(nextPokemon)
    } else {
      battleManager.sendBattleMessage(controller.trainer.id,0,0)
      battle.round.updatePokemon()
      resumeGame()
    }
  }

  /**
    * @inheritdoc
    * @return a boolean representing if the battle is distributed or not
    */
  override def isDistributedBattle: Boolean = true

  /**
    * @inheritdoc
    * @return a boolean representing if the playing trainer is the first to attack
    */
  override def yourPlayerIsFirst: Boolean = playerIsFirst

  /**
    * Return always false
    * @return false
    */
  override def trainerThrowPokeball(): Boolean = false

  /**
    * @inheritdoc
    * @return true if the trainer can escape, false in the opposite case
    */
  override def trainerCanQuit(): Boolean = {
    battleManager.sendBattleMessage(controller.trainer.id,0,0)
    resumeGame()
    true
  }
}
