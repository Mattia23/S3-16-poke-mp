package model.battle

import java.util.Optional

import controller.{BattleController, GameController}
import model.entities.{Owner, PokemonFactory, PokemonWithLife, Trainer}
import utilities.Settings

trait Battle {
  def trainer: Trainer

  def round: BattleRound

  def startBattleRound(pokemonId: Int): Unit

  def startBattleRound(myPokemonId: Int, otherPokemonId: Int): Unit

  def myPokemon: PokemonWithLife

  def myPokemon_=(myPokemon:PokemonWithLife): Unit

  def otherPokemon: PokemonWithLife

  def otherPokemon_=(otherPokemon: PokemonWithLife): Unit

  def myPokemonKillsOtherPokemon(won: Boolean): Unit

  def pokeball_=(pokeball: Int): Unit

  def pokeball: Int

  def pokeballLaunched(): Boolean

  def battleFinished: Boolean

  def battleFinished_=(battleFinished: Boolean): Unit

  def roundFinished: Boolean

  def roundFinished_=(battleFinished: Boolean): Unit

  def updatePokemonAndTrainer(event: Int): Unit

  def getMyPokemonId: Int

  def getOtherPokemonId: Int
}

class BattleImpl(_trainer: Trainer, controller: BattleController) extends Battle {
  var _round: BattleRound = _
  override var battleFinished: Boolean = false
  override var roundFinished: Boolean = false
  override def trainer: Trainer = _trainer
  override def round: BattleRound = _round
  override var pokeball: Int = 3
  override var myPokemon: PokemonWithLife = _
  override var otherPokemon: PokemonWithLife = PokemonFactory.createPokemon(Owner.WILD,Optional.empty(),Optional.of(this._trainer.level)).get()

  override def startBattleRound(pokemonId: Int): Unit = {
    _trainer.addMetPokemon(otherPokemon.pokemon.id)
    myPokemon = PokemonFactory.createPokemon(Owner.TRAINER,Optional.of(pokemonId),Optional.empty()).get()
    _round = new BattleRoundImpl(myPokemon, pokemonId, otherPokemon, this)
  }

  override def myPokemonKillsOtherPokemon(won: Boolean): Unit = {
    var pointsEarned: Int = 0
    if(won){
      battleFinished = true
      pointsEarned = (otherPokemon.pokemon.level * math.pow(1.2,_trainer.level)).toInt
      _trainer.updateTrainer(pointsEarned)
    } else {
      roundFinished = true
      val newPokemonId = _trainer.getFirstAvailableFavouritePokemon
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
        _trainer.updateTrainer(pointsEarned)
        controller.resumeGame()
      }
    }
  }

  override def pokeballLaunched(): Boolean = {
    _round.pokeballLaunched()
  }

  override def updatePokemonAndTrainer(event: Int): Unit = event match {
    case Settings.BATTLE_EVENT_CAPTURE_POKEMON =>
      val pointsEarned: Int = (otherPokemon.pokemon.level * math.pow(1.3,_trainer.level)).toInt
      _trainer.updateTrainer(pointsEarned)
      if(trainer.favouritePokemons.contains(0)){
        trainer.addFavouritePokemon(trainer.capturedPokemons.last._1)
      }
      _round.updatePokemon()
    case Settings.BATTLE_EVENT_ESCAPE =>
      _trainer.updateTrainer(0)
      _round.updatePokemon()
  }

  override def startBattleRound(pokemonId: Int, otherPokemon: Int): Unit = {}

  override def getMyPokemonId: Int = 0
  override def getOtherPokemonId: Int = 0
}
