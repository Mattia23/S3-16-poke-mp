package model.entities

/**
  * Pokemon with life is the the instance of a pokemon during the battle. It takes in input a static object pokemon
  * (whose fields don't change during the battle, in fact are final) and the life (mutable) of this pokemon.
  */
trait PokemonWithLife {
  /**
    * The final instance of the pokemon, whose fields are final.
    * @return
    */
  def pokemon: Pokemon

  /**
    * The life of the pokemon, that change during the battle.
    * @return
    */
  def pokemonLife: Int

  /**
    * This method is called by PokemonBehaviour when a pokemon undergo an attack. loseLifePoints takes in input the damage,
    * update the pokemon life, check if the pokemon is dead and return his status.
    * @param lostPoints
    * @return pokemon life status
    */
  def loseLifePoints(lostPoints: Int): PokemonStatus.Value

}

class PokemonWithLifeImpl(val pokemon: Pokemon, var pokemonLife: Int) extends PokemonWithLife {
  /**
    * @inheritdoc
    */
  override def loseLifePoints(lostPoints: Int): PokemonStatus.Value = {
    if(this.pokemonLife - lostPoints > 0) {
      this.pokemonLife = this.pokemonLife - lostPoints
      PokemonStatus.ALIVE
    } else {
      this.pokemonLife = 0
      PokemonStatus.DEATH
    }
  }

}
