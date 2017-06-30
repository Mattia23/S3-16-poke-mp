package utilities

import java.awt.{Dimension, Toolkit}

object Settings {
  val SCREEN_DIMENSION: Dimension = Toolkit.getDefaultToolkit.getScreenSize

  val SCREEN_WIDTH: Int = SCREEN_DIMENSION.width

  val SCREEN_HEIGHT: Int = SCREEN_DIMENSION.height

  val PANELS_FOLDER: String =  "/panels/"

  val IMAGES_FOLDER: String =  "/images/"

  val DATABASE_FOLDER: String =  "/database/"

  val INITIAL_TRAINER_LEVEL: Int = 0

  val LEVEL_STEP: Int = 50

}
