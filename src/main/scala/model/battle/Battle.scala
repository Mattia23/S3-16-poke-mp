package model.battle

import java.util.Optional

import controller.BattleController
import model.entities.{Owner, PokemonFactory, PokemonWithLife, Trainer}
import utilities.Settings

/**
  * A Battle represents a fight between a trainer and a wild Pokemon. It is composed by one or more BattleRound based on
  * how many of trainer's Pokemon the trainer chooses.
  */
trait Battle {
  /**
    * Return the playing trainer
    * @return playing trainer
    */
  def trainer: Trainer

  /**
    * Return the current round of the battle
    * @return current round
    */
  def round: BattleRound

  /**
    * Set a new round for the battle
    * @param round the new round
    */
  def round_=(round: BattleRound): Unit

  /**
    * Start a new round in a battle against a wild Pokemon
    * @param pokemonId trainer's pokemon ID of the Pokemon that fight in the round
    */
  def startBattleRound(pokemonId: Int): Unit

  /**
    * Return my fighting pokemon
    * @return my fighting pokemon
    */
  def myPokemon: PokemonWithLife

  /**
    * Set my fighting Pokemon
    * @param myPokemon the new Pokemon that is going to fight
    */
  def myPokemon_=(myPokemon:PokemonWithLife): Unit

  /**
    * Return other trainer's fighting pokemon
    * @return other trainer's fighting pokemon
    */
  def otherPokemon: PokemonWithLife

  /**
    * Set other trainer's fighting pokemon
    * @param otherPokemon other trainer's fighting pokemon
    */
  def otherPokemon_=(otherPokemon: PokemonWithLife): Unit

  /**
    * Manage the event of my trainer's Pokemon kills the other Pokemon or viceversa.
    * @param won true if my trainer's Pokemons killed the other Pokemon, false in the opposite case
    */
  def myPokemonKillsOtherPokemon(won: Boolean): Unit

  /**
    * Return the number of Pokeball available in the battle
    * @return the number of Pokeball
    */
  def pokeball: Int

  /**
    * Set the number of Pokemon available
    * @param pokeball new number of pokeball available
    */
  def pokeball_=(pokeball: Int): Unit

  /**
    * Return if the trainer captured the other Pokemon after launching a Pokeball
    * @return true if the opposite Pokemon is captured, false in the opposite case
    */
  def pokemonIsCaptured(): Boolean

  /**
    * Return if the current battle is finished
    * @return true if the battle is finished, false in the opposite case
    */
  def battleFinished: Boolean

  /**
    * Set the value of battle is finished
    * @param battleFinished boolean value for the end of the battle
    */
  def battleFinished_=(battleFinished: Boolean): Unit

  /**
    * Return if current round is finished
    * @return true if the round is finished, false in the opposite case
    */
  def roundFinished: Boolean

  /**
    * Set the value of battle is finished
    * @param roundFinished boolean value for the end of the round
    */
  def roundFinished_=(roundFinished: Boolean): Unit

  /**
    * Update trainer and his Pokemon and based on the event give the correct earned points to the trainer
    * @param event event happened
    */
  def updatePokemonAndTrainer(event: Int): Unit

  /**
    * Return opposite trainer's Pokemon id
    * @return opposite trainer's Pokemon id
    */
  def getOtherPokemonId: Int
}

/**
  * @inheritdoc
  * @param trainer the playing trainer
  * @param controller a BattleController object that manages the different situations
  */
class BattleImpl(override val trainer: Trainer, private val controller: BattleController) extends Battle {
  override var battleFinished: Boolean = false
  override var roundFinished: Boolean = false
  override var round: BattleRound = _
  override var pokeball: Int = 3
  override var myPokemon: PokemonWithLife = _
  override var otherPokemon: PokemonWithLife = PokemonFactory.createPokemon(Owner.WILD,Optional.empty(),Optional.of(this.trainer.level)).get()

  /**
    * @inheritdoc
    */
  override def startBattleRound(pokemonId: Int): Unit = {
    trainer.addMetPokemon(otherPokemon.pokemon.id)
    myPokemon = PokemonFactory.createPokemon(Owner.TRAINER,Optional.of(pokemonId),Optional.empty()).get()
    round = new BattleRoundImpl(myPokemon, pokemonId, otherPokemon, this)
  }

  /**
    * Calculate and update trainer if his Pokemon killed the wild one. If wild Pokemon killed trainer's Pokemon,
    * check if a new trainer's Pokemon is available to fight and if so start a new round, else resume the game.
    * @param won true if my trainer's Pokemons killed the other Pokemon, false in the opposite case
    */
  override def myPokemonKillsOtherPokemon(won: Boolean): Unit = {
    var pointsEarned: Int = 0
    if(won){
      battleFinished = true
      pointsEarned = (otherPokemon.pokemon.level * math.pow(1.2,trainer.level)).toInt
      trainer.updateTrainer(pointsEarned)
    } else {
      roundFinished = true
      val newPokemonId = trainer.getFirstAvailableFavouritePokemon
      if(newPokemonId > 0){
        startBattleRound(newPokemonId)
        val t: Thread = new Thread {
          override def run() {
            Thread.sleep(1000)
            roundFinished = false
          }
        }
        t.start()
      } else {
        battleFinished = true
        pointsEarned = 30
        trainer.updateTrainer(pointsEarned)
        controller.resumeGame()
      }
    }
  }

  /**
    * @inheritdoc
    */
  override def pokemonIsCaptured(): Boolean = {
    round.pokemonIsCaptured()
  }

  /**
    * @inheritdoc
    */
  override def updatePokemonAndTrainer(event: Int): Unit = event match {
    case Settings.Constants.BATTLE_EVENT_CAPTURE_POKEMON =>
      val pointsEarned: Int = (otherPokemon.pokemon.level * math.pow(1.3,trainer.level)).toInt
      trainer.updateTrainer(pointsEarned)
      if(trainer.favouritePokemons.contains(0)){
        trainer.addFavouritePokemon(trainer.capturedPokemons.last._1)
      }
      round.updatePokemon()
    case Settings.Constants.BATTLE_EVENT_ESCAPE =>
      trainer.updateTrainer(0)
      round.updatePokemon()
  }

  /**
    * @inheritdoc
    */
  override def getOtherPokemonId: Int = 0
}
