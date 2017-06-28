package utilities

import java.awt.{Dimension, Toolkit}

object Settings {
  val SCREEN_DIMENSION: Dimension = Toolkit.getDefaultToolkit.getScreenSize

  val SCREEN_WIDTH: Int = SCREEN_DIMENSION.width

  val SCREEN_HEIGHT: Int = SCREEN_DIMENSION.height

  val FRAME_WIDTH: Int = Settings.SCREEN_WIDTH / 2

  val FRAME_HEIGHT: Int = Settings.SCREEN_HEIGHT / 2

  val MAP_WIDTH: Int = 50

  val MAP_HEIGHT: Int = 50

  val TILE_HEIGHT: Int = 1

  val TILE_WIDTH: Int = 1

  val TILE_PIXEL: Int = 32

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

  val GRASS_IMAGE_STRING: String = IMAGES_FOLDER + "grass-tile.png"

  val TALL_GRASS_IMAGE_STRING: String = IMAGES_FOLDER + "tall-grass-tile.png"

  val WATER_IMAGE_STRING: String = IMAGES_FOLDER + "water-tile.png"

  val LABORATORY_IMAGE_STRING: String = IMAGES_FOLDER + "laboratory-tile.png"

  val POKEMON_CENTER_IMAGE_STRING: String = IMAGES_FOLDER + "pokemon-center-tile.png"

  val TREE_IMAGE_STRING: String = IMAGES_FOLDER + "tree-tile.png"
}
