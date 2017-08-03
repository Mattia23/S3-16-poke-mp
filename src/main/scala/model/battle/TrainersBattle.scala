package model.battle

import java.util.Optional

import controller.BattleController
import model.entities.{Owner, PokemonFactory, PokemonWithLife, Trainer}
import utilities.Settings

/**
  * A TrainersBattle represents a battle between two different trainers; it extends Battle and can be composed of a lot
  * of different rounds.
  */
trait TrainersBattle extends Battle {
  /**
    * Start a new round in a battle against an other trainer
    * @param myPokemonId my trainer's pokemon ID of the Pokemon that fight in the round
    * @param otherPokemonId other trainer's pokemon ID of the Pokemon that fight in the round
    */
  def startBattleRound(myPokemonId: Int, otherPokemonId: Int): Unit

  /**
    * Return trainer's Pokemon id
    * @return trainer's Pokemon Id
    */
  def getMyPokemonId: Int
}

/**
  * @inheritdoc
  * @param trainer the playing trainer
  * @param controller a BattleController object that manages all the possible situations
  * @param otherTrainer the other trainer fighting againts the playing trainer
  */
class TrainersBattleImpl(override val trainer: Trainer,
                         private val controller: BattleController,
                         private val otherTrainer: Trainer) extends TrainersBattle {
  override var battleFinished: Boolean = false
  override var roundFinished: Boolean = false
  override var round: BattleRound = _
  override var pokeball: Int = 0
  override var myPokemon: PokemonWithLife = _
  override var otherPokemon: PokemonWithLife = _
  private var myPokemonId = 0
  private var otherPokemonId = 0

  /**
    * @inheritdoc
    */
  override def startBattleRound(pokemonId: Int): Unit = {}

  /**
    * @inheritdoc
    */
  override def startBattleRound(myPokemonId: Int, otherPokemonId: Int): Unit = {
    myPokemon = PokemonFactory.createPokemon(Owner.TRAINER,Optional.of(myPokemonId),Optional.empty()).get()
    otherPokemon = PokemonFactory.createPokemon(Owner.TRAINER,Optional.of(otherPokemonId),Optional.empty()).get()
    this.myPokemonId = myPokemonId
    this.otherPokemonId = otherPokemonId
    trainer.addMetPokemon(otherPokemon.pokemon.id)
    round = new BattleRoundImpl(myPokemon, myPokemonId, otherPokemon, this)
  }

  /**
    * Calculate and update trainer's experience points if trainer's Pokemon killed the other trainer's Pokemon.
    * @param won true if my trainer's Pokemons killed the other Pokemon, false in the opposite case
    */
  override def myPokemonKillsOtherPokemon(won: Boolean): Unit = {
    var pointsEarned: Int = 0
    if(won){
      pointsEarned = (otherTrainer.level * math.pow(1.05, trainer.level)).toInt
      trainer.updateTrainer(pointsEarned)
    }
  }

  /**
    * @inheritdoc
    */
  override def updatePokemonAndTrainer(event: Int): Unit = event match {
    case Settings.Constants.BATTLE_EVENT_ESCAPE =>
      trainer.updateTrainer(0)
      round.updatePokemon()
  }

  /**
    * @inheritdoc
    */
  override def pokemonIsCaptured(): Boolean = {false}

  /**
    * @inheritdoc
    */
  override def getMyPokemonId: Int = this.myPokemonId


  /**
    * @inheritdoc
    */
  override def getOtherPokemonId: Int = this.otherPokemonId
}