package model.entities

object Owner extends Enumeration {
  val TRAINER, WILD, INITIAL = Value
}

object PokemonStatus extends Enumeration {
  val ALIVE, DEATH = Value
}
