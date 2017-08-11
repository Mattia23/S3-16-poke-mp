package model.entities

import database.remote.DBConnect

trait Pokedex {
  /**
    * Return Pokedex (the list of the Pokemon IDs already met)
    * @return Pokedex
    */
  def pokedex : List[Int]

  /**
    * Set the new Pokedex
    * @param list the new list of the Pokemon already met
    */
  def pokedex_=(list: List[Int]): Unit

  /**
    * Check if the pokemon ID has been already met and return a Boolean with the answer
    * @param pokemonId pokemon ID of the local database to be checked
    * @return true if already met, false in the opposite case
    */
  def checkIfAlreadyMet(pokemonId: Int): Boolean

}

class PokedexImpl(val id: Int) extends Pokedex {
  override var pokedex: List[Int] = DBConnect.getMetPokemonList(id).get()

  /**
    * @inheritdoc
    */
  override def checkIfAlreadyMet(pokemonId: Int): Boolean = this.pokedex.contains(pokemonId)
}
