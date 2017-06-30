package utilities

import java.awt.{Dimension, Toolkit}
import java.util

object Settings {
  val SCREEN_DIMENSION: Dimension = Toolkit.getDefaultToolkit.getScreenSize

  val SCREEN_WIDTH: Int = SCREEN_DIMENSION.width

  val SCREEN_HEIGHT: Int = SCREEN_DIMENSION.height

  val TILE_PIXEL : Int = 32

  val TILE_HEIGHT: Int = 1

  val TILE_WIDTH: Int = 1

  val POKEMON_CENTER_HEIGHT = 5

  val POKEMON_CENTER_WIDTH = 5

  val POKEMON_CENTER_DOOR_X = 2

  val POKEMON_CENTER_DOOR_Y = 4

  val LABORATORY_HEIGHT = 4

  val LABORATORY_WIDTH = 7

  val LABORATORY_DOOR_X = 3

  val LABORATORY_DOOR_Y = 3

  val PANELS_FOLDER: String =  "/panels/"

  val IMAGES_FOLDER: String =  "/images/"

  val MAP_IMAGES_FOLDER = "/images/maps/"

  val CHARACTER_IMAGES_FOLDER = "/images/characters/"

  val OK_BUTTON: util.List[String] = util.Arrays.asList("ok")

  val YES_NO_BUTTON: util.List[String] = util.Arrays.asList("yes", "no")

  val TRAINER_BUTTON: util.List[String] = util.Arrays.asList("fight", "change", "bye")

}