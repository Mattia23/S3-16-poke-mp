package model.game

import java.util.Optional

import model.entities.{Owner, PokemonFactory, PokemonWithLife, Trainer}

trait Battle {
  def startBattleRound(pokemonId: Int): Unit

  def battleFinished(won: Boolean): Unit

  def pokeballLaunched(): Unit
}

class BattleImpl(trainer: Trainer) extends Battle {
  var wildPokemon: PokemonWithLife = PokemonFactory.createPokemon(Owner.WILD,Optional.empty(),Optional.of(trainer.level)).get()

  override def startBattleRound(pokemonId: Int): Unit = {
    var myPokemon: PokemonWithLife = PokemonFactory.createPokemon(Owner.TRAINER,Optional.of(pokemonId),Optional.empty()).get()
    var round: BattleRound = new BattleRoundImpl(myPokemon, wildPokemon, this)
  }

  override def battleFinished(won: Boolean): Unit = ???

  override def pokeballLaunched(): Unit = ???
}
