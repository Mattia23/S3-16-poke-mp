package model.game

import java.util.Optional

import model.entities.{Owner, PokemonFactory, PokemonWithLife, Trainer}

trait Battle {
  def trainer: Trainer

  def round: BattleRound

  def startBattleRound(pokemonId: Int): Unit

  def myPokemon: PokemonWithLife

  def myPokemon_=(myPokemon:PokemonWithLife): Unit

  def wildPokemon: PokemonWithLife

  def myPokemonKillsWildPokemon(won: Boolean): Unit

  def pokeballLaunched(): Boolean

  def battleFinished: Boolean

  def battleFinished_=(battleFinished: Boolean): Unit
}

class BattleImpl(_trainer: Trainer) extends Battle {
  var _round: BattleRound = _
  override var battleFinished: Boolean = false
  override def trainer: Trainer = _trainer
  override def round: BattleRound = _round
  override var myPokemon: PokemonWithLife = _
  override val wildPokemon: PokemonWithLife = PokemonFactory.createPokemon(Owner.WILD,Optional.empty(),Optional.of(this._trainer.level)).get()

  override def startBattleRound(pokemonId: Int): Unit = {
    _trainer.addMetPokemon(wildPokemon.pokemon.id)
    myPokemon = PokemonFactory.createPokemon(Owner.TRAINER,Optional.of(pokemonId),Optional.empty()).get()
    _round = new BattleRoundImpl(myPokemon, pokemonId, wildPokemon, this)
  }

  override def myPokemonKillsWildPokemon(won: Boolean): Unit = {
    battleFinished = true
    var pointsEarned: Int = 0
    if(won){
      pointsEarned = (wildPokemon.pokemon.experiencePoints / wildPokemon.pokemon.level * math.pow(1.3,_trainer.level)).toInt
      _trainer.updateTrainer(pointsEarned)
    } else {
      var newPokemonId = _trainer.getFirstAvailableFavouritePokemon
      if(newPokemonId > 0){
        startBattleRound(newPokemonId)
      } else {
        pointsEarned = 30
        _trainer.updateTrainer(pointsEarned)
        println("torno al centro pokemon")
      }
    }
  }

  override def pokeballLaunched(): Boolean = {
    _round.pokeballLaunched()
  }
}
