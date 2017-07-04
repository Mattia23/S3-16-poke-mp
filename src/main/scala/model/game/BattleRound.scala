package model.game

import model.entities.PokemonWithLife

trait BattleRound {
  def pokeballLaunched(): Unit

}

class BattleRoundImpl(myPokemon: PokemonWithLife, wildPokemon: PokemonWithLife, battle: Battle) extends  BattleRound{
  override def pokeballLaunched(): Unit = ???
}
