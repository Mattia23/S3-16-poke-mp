package model.game

import model.entities.{PokemonFactory, Owner, PokemonWithLife}

trait Battle {
  def startBattle: Unit

  def trainerExperiencePoint: Int
}

class BattleImpl(idTrainer: Int, pokemonTrainedId: Int) extends Battle {

  private var _trainerExperiencePoint: Int = 0

  override def trainerExperiencePoint = this._trainerExperiencePoint
  //var wildPokemon: PokemonWithLife = PokemonFactory.createPokemon(Owner.WILD, )

  override def startBattle: Unit = {

  }


}
