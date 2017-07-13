package model.characters

import java.awt.Image
import java.util.Optional

import model.entities.{Owner, Pokemon, PokemonFactory, PokemonWithLife}
import model.environment.{Coordinate, CoordinateImpl}
import utilities.Settings
import view.LoadImage


trait StaticCharacter {

  def HEIGHT: Int

  def image: Image

  def coordinate: Coordinate

  def dialogue: List[String]

}

class Oak extends StaticCharacter{

  override val HEIGHT: Int = 40

  override val image: Image = LoadImage.load(Settings.CHARACTER_IMAGES_FOLDER + "oak.png")

  override def coordinate: Coordinate = CoordinateImpl(6, 4)

  override val dialogue: List[String] = List("Ciao bell!", "Benvenuto nel mondo dei Pokémon.",
    "Se non lo hai già fatto,", "puoi scegliere uno dei 3 Pokémon sulla mia scrivania.", "Buon avventura!")

}

class Doctor extends StaticCharacter{

  override val HEIGHT: Int = 82

  override val image: Image = LoadImage.load(Settings.CHARACTER_IMAGES_FOLDER + "doctor.png")

  override def coordinate: Coordinate = CoordinateImpl(7, 3)

  override val dialogue: List[String] = List("Ciao ragazzo.", "Vuoi che curi i tuoi Pokemon?")
}

abstract class PokemonCharacter extends StaticCharacter{

  override def HEIGHT: Int = 32

  def image: Image

  def pokemonWithLife: PokemonWithLife
}

class Bulbasaur extends PokemonCharacter{
  override def pokemonWithLife: PokemonWithLife = PokemonFactory.createPokemon(Owner.INITIAL, Optional.of(7), Optional.of(1)).get()

  override def image: Image = LoadImage.load(Settings.CHARACTER_IMAGES_FOLDER + "bulbasaur.png")

  override def coordinate: Coordinate = CoordinateImpl(8, 4)

  override def dialogue: List[String] = List("Bulbasaaaaur")
}

class Charmander extends PokemonCharacter{
  override def pokemonWithLife: PokemonWithLife = PokemonFactory.createPokemon(Owner.INITIAL, Optional.of(4), Optional.of(1)).get()

  override def image: Image = LoadImage.load(Settings.CHARACTER_IMAGES_FOLDER + "charmander.png")

  override def coordinate: Coordinate = CoordinateImpl(9, 4)

  override def dialogue: List[String] = List("Chaaaarmandeeer")
}

class Squirtle extends PokemonCharacter{
  override def pokemonWithLife: PokemonWithLife = PokemonFactory.createPokemon(Owner.INITIAL, Optional.of(7), Optional.of(1)).get()

  override def image: Image = LoadImage.load(Settings.CHARACTER_IMAGES_FOLDER + "squirtle.png")

  override def coordinate: Coordinate = CoordinateImpl(10, 4)

  override def dialogue: List[String] = List("Squeroo squerooo")
}
