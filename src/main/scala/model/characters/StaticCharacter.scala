package model.characters

import java.awt.Image

import model.entities.Pokemon
import model.environment.{Coordinate, CoordinateImpl}
import utilities.Settings
import view.LoadImage

trait StaticCharacter {

  def HEIGHT: Int

  def image: Image

  def coordinate: Coordinate

  def dialogue: String

}

class Oak extends StaticCharacter{

  override val HEIGHT: Int = 40

  override val image: Image = LoadImage.load(Settings.CHARACTER_IMAGES_FOLDER + "oak.png")

  override def coordinate: Coordinate = CoordinateImpl(6, 4)

  override val dialogue: String = "Ciao bello!"

}

class Doctor extends StaticCharacter{

  override val HEIGHT: Int = 82

  override val image: Image = LoadImage.load(Settings.CHARACTER_IMAGES_FOLDER + "doctor.png")

  override def coordinate: Coordinate = CoordinateImpl(7, 3)

  override val dialogue: String = "Uuuuuuuuè ragazzi come va? Vuoi che ti curi i Pokemon?"
}

abstract class PokemonCharacter extends StaticCharacter{

  override def HEIGHT: Int = 32

  def image: Image

  def dialogue: String

  def pokemon: Pokemon
}

class Bulbasaur extends PokemonCharacter{
  override def pokemon: Pokemon = ???

  override def image: Image = LoadImage.load(Settings.CHARACTER_IMAGES_FOLDER + "bulbasaur.png")

  override def coordinate: Coordinate = CoordinateImpl(8, 4)

  override def dialogue: String = "Bulbasaaaaur"
}

class Charmander extends PokemonCharacter{
  override def pokemon: Pokemon = ???

  override def image: Image = LoadImage.load(Settings.CHARACTER_IMAGES_FOLDER + "charmander.png")

  override def coordinate: Coordinate = CoordinateImpl(9, 4)

  override def dialogue: String = "Chaaaarmandeeer"
}

class Squirtle extends PokemonCharacter{
  override def pokemon: Pokemon = ???

  override def image: Image = LoadImage.load(Settings.CHARACTER_IMAGES_FOLDER + "squirtle.png")

  override def coordinate: Coordinate = CoordinateImpl(10, 4)

  override def dialogue: String = "Squeroo squerooo"
}
