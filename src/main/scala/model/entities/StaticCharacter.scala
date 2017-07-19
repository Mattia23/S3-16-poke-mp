package model.entities

import java.awt.Image
import java.util.Optional

import model.entities.Oak.{DIALOGUE_1, DIALOGUE_2, DIALOGUE_3, DIALOGUE_4}
import model.environment.{Coordinate, CoordinateImpl}
import utilities.Settings
import view.LoadImage


trait StaticCharacter {

  def HEIGHT: Int

  def image: Image

  def coordinate: Coordinate

  def dialogue: List[String]

}

object Oak{
  private final val DIALOGUE_1: String = "Hi!"

  private final val DIALOGUE_2: String = "Welcome to Pokémon World."

  private final val DIALOGUE_3: String = "You can choose one of 3 Pokémon on my desk."

  private final val DIALOGUE_4: String = "Good adventure!"
}

class Oak extends StaticCharacter{
  import Oak._

  override val HEIGHT: Int = 40

  override val image: Image = LoadImage.load(Settings.CHARACTER_IMAGES_FOLDER + "oak.png")

  override def coordinate: Coordinate = CoordinateImpl(6, 4)

  override val dialogue: List[String] = List(DIALOGUE_1, DIALOGUE_2, DIALOGUE_3, DIALOGUE_4)

}

object OakAfterChoise{
  private final val DIALOGUE_1: String = "Hey, how are you doing?"

  private final val DIALOGUE_2: String = "I see the pokemon you chose is happy!"

  private final val DIALOGUE_3: String = "Keep it up and..."

  private final val DIALOGUE_4: String = "Gotta Catch 'Em All!"
}

class OakAfterChoise extends Oak{
  import OakAfterChoise._

  override val dialogue: List[String] = List(DIALOGUE_1, DIALOGUE_2, DIALOGUE_3, DIALOGUE_4)

}

object Doctor{
  private final val DIALOGUE_1: String = "Hi trainer!"

  private final val DIALOGUE_2: String = "Do you want to heal your Pokémon?"

  final val DIALOGUE_AFTER_HEAL: String = "Your Pokémon has been healed!"
}

class Doctor extends StaticCharacter{

  import Doctor._

  override val HEIGHT: Int = 82

  override val image: Image = LoadImage.load(Settings.CHARACTER_IMAGES_FOLDER + "doctor.png")

  override def coordinate: Coordinate = CoordinateImpl(7, 3)

  override val dialogue: List[String] = List(DIALOGUE_1, DIALOGUE_2)
}

abstract class PokemonCharacter extends StaticCharacter{

  override def HEIGHT: Int = 32

  def image: Image

  def pokemonWithLife: PokemonWithLife
}

class Bulbasaur extends PokemonCharacter{
  override def pokemonWithLife: PokemonWithLife = PokemonFactory.createPokemon(Owner.INITIAL, Optional.of(1), Optional.of(1)).get()

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
