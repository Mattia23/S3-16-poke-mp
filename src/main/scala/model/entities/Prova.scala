package model.entities

class Prova(firstPokemon: (Pokemon,Int),secondPokemon: (Pokemon,Int)) {
  println(firstPokemon._1.attacks, secondPokemon._1.name)
}
