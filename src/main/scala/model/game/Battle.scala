package model.game

import model.entities.{PokemonFactory, Owner, PokemonWithLife}

trait Battle {
  def startBattle: Unit

  def trainerExperiencePoint: Int
}

class BattleImpl(idTrainer: Int, pokemonTrainedId: Int) extends Battle {

  var trainerExperiencePoint: Int = 0
  //var wildPokemon: PokemonWithLife = PokemonFactory.createPokemon(Owner.WILD, )

  override def startBattle: Unit = {

  }


}
