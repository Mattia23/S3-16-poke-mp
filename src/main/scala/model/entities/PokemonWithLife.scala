package model.entities

trait PokemonWithLife {

  def pokemon: Pokemon

  def pokemonLife: Int

  def loseLifePoints(lostPoints: Int): PokemonStatus.Value

}

class PokemonWithLifeImpl(val pokemon: Pokemon, var pokemonLife: Int) extends PokemonWithLife {

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
