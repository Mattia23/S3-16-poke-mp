package model.entities

import database.remote.DBConnect

trait Pokedex {
  def pokedex : List[Int]

  def pokedex_=(list: List[Int]): Unit

  def checkIfAlreadyMet(pokemonId: Int): Boolean

}

class PokedexImpl(val id: Int) extends Pokedex {
  private var _pokedex: List[Int] = DBConnect.getMetPokemonList(id).get()

  override def pokedex: List[Int] = this._pokedex

  override def pokedex_=(list: List[Int]): Unit = this._pokedex = list

  override def checkIfAlreadyMet(pokemonId: Int): Boolean = this._pokedex.contains(pokemonId)
}
