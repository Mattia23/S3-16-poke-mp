package model.entities

object Owner extends Enumeration {
  val TRAINER, WILD = Value
}

object PokemonStatus extends Enumeration {
  val ALIVE, DEATH = Value
}
