package model.entities

import database.local.PokedexConnect

class Prova(firstPokemon: (Pokemon,Int),secondPokemon: (Pokemon,Int)) {
  println(firstPokemon._1.name, secondPokemon._1.name)
  println(PokedexConnect.getPokemonAttack(1))
}
