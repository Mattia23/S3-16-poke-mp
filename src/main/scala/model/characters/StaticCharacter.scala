package model.characters

import java.awt.Image

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


