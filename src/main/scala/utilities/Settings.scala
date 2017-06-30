package utilities

import java.awt.{Dimension, Toolkit}
import java.util

object Settings {
  val SCREEN_DIMENSION: Dimension = Toolkit.getDefaultToolkit.getScreenSize

  val GAME_REFRESH_TIME: Int = 16

  val SCREEN_WIDTH: Int = SCREEN_DIMENSION.width

  val SCREEN_HEIGHT: Int = SCREEN_DIMENSION.height

  val FRAME_WIDTH: Int = SCREEN_WIDTH / 3

  val FRAME_HEIGHT: Int = SCREEN_HEIGHT / 3

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

  val GRASS_IMAGE_STRING: String = IMAGES_FOLDER + "grass.png"

  val TALL_GRASS_IMAGE_STRING: String = IMAGES_FOLDER + "tall-grass.png"

  val LABORATORY_IMAGE_STRING: String = IMAGES_FOLDER + "laboratory.png"

  val POKEMON_CENTER_IMAGE_STRING: String = IMAGES_FOLDER + "pokemon-center.png"

  val TREE_IMAGE_STRING: String = IMAGES_FOLDER + "tree.png"

  val WATER_IMAGE_STRING: String = IMAGES_FOLDER + "water.png"

  val WATER_MARGIN_TOP_LEFT_IMAGE_STRING: String = IMAGES_FOLDER + "water-margin-top-left.png"

  val WATER_MARGIN_TOP_RIGHT_IMAGE_STRING: String = IMAGES_FOLDER + "water-margin-top-right.png"

  val WATER_MARGIN_TOP_IMAGE_STRING: String = IMAGES_FOLDER + "water-margin-top.png"

  val WATER_MARGIN_BOTTOM_LEFT_IMAGE_STRING: String = IMAGES_FOLDER + "water-margin-bottom-left.png"

  val WATER_MARGIN_BOTTOM_RIGHT_IMAGE_STRING: String = IMAGES_FOLDER + "water-margin-bottom-right.png"

  val WATER_MARGIN_BOTTOM_IMAGE_STRING: String = IMAGES_FOLDER + "water-margin-bottom.png"

  val WATER_MARGIN_LEFT_IMAGE_STRING: String = IMAGES_FOLDER + "water-margin-left.png"

  val WATER_MARGIN_RIGHT_IMAGE_STRING: String = IMAGES_FOLDER + "water-margin-right.png"

  val ROAD_IMAGE_STRING: String = IMAGES_FOLDER + "road.png"

  val ROAD_MARGIN_TOP_LEFT_IMAGE_STRING: String = IMAGES_FOLDER + "road-margin-top-left.png"

  val ROAD_MARGIN_TOP_RIGHT_IMAGE_STRING: String = IMAGES_FOLDER + "road-margin-top-right.png"

  val ROAD_MARGIN_TOP_IMAGE_STRING: String = IMAGES_FOLDER + "road-margin-top.png"

  val ROAD_MARGIN_BOTTOM_LEFT_IMAGE_STRING: String = IMAGES_FOLDER + "road-margin-bottom-left.png"

  val ROAD_MARGIN_BOTTOM_RIGHT_IMAGE_STRING: String = IMAGES_FOLDER + "road-margin-bottom-right.png"

  val ROAD_MARGIN_BOTTOM_IMAGE_STRING: String = IMAGES_FOLDER + "road-margin-bottom.png"

  val ROAD_MARGIN_LEFT_IMAGE_STRING: String = IMAGES_FOLDER + "road-margin-left.png"

  val ROAD_MARGIN_RIGHT_IMAGE_STRING: String = IMAGES_FOLDER + "road-margin-right.png"

  val DATABASE_FOLDER: String =  "/database/"

  val INITIAL_TRAINER_LEVEL: Int = 0

  val LEVEL_STEP: Int = 50

  val MAP_IMAGES_FOLDER = "/images/maps/"

  val CHARACTER_IMAGES_FOLDER = "/images/characters/"

  val OK_BUTTON: util.List[String] = util.Arrays.asList("ok")

  val YES_NO_BUTTON: util.List[String] = util.Arrays.asList("yes", "no")

  val TRAINER_BUTTON: util.List[String] = util.Arrays.asList("fight", "change", "bye")

}