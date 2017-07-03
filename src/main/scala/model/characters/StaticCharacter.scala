package model.characters

import java.awt.Image

import model.entities.Pokemon
import utilities.Settings
import view.LoadImage

trait StaticCharacter {

  def HEIGHT: Int

  def image: Image

  def dialogue: String

}

class Oak extends StaticCharacter{

  override val HEIGHT: Int = 44

  override val image: Image = LoadImage.load(Settings.CHARACTER_IMAGES_FOLDER + "oak.png")

  override val dialogue: String = "Ciao bello!"
}

class Doctor extends StaticCharacter{

  override val HEIGHT: Int = 82

  override val image: Image = LoadImage.load(Settings.CHARACTER_IMAGES_FOLDER + "doctor.png")

  override val dialogue: String = "Uuuuuuuuè ragazzi come va? Vuoi che ti curi i Pokemon?"
}

abstract class PokemonCharacter extends StaticCharacter{

  override def HEIGHT: Int = 32

  def image: Image

  def dialogue: String

  def pokemon: Pokemon
}

class Charmander extends PokemonCharacter{
  override def pokemon: Pokemon = ???

  override def image: Image = LoadImage.load(Settings.CHARACTER_IMAGES_FOLDER + "charmander.png")

  override def dialogue: String = "Chaaaarmandeeer"
}

class Bulbasaur extends PokemonCharacter{
  override def pokemon: Pokemon = ???

  override def image: Image = LoadImage.load(Settings.CHARACTER_IMAGES_FOLDER + "bulbasaur.png")

  override def dialogue: String = "Bulbasaaaaur"
}

class Squirtle extends PokemonCharacter{
  override def pokemon: Pokemon = ???

  override def image: Image = LoadImage.load(Settings.CHARACTER_IMAGES_FOLDER + "squirtle.png")

  override def dialogue: String = "Squeroo squerooo"
}
