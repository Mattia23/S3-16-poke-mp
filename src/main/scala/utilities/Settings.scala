package utilities

import java.awt.{Dimension, Toolkit}

object Settings {
  val SCREEN_DIMENSION: Dimension = Toolkit.getDefaultToolkit.getScreenSize

  val SCREEN_WIDTH: Int = SCREEN_DIMENSION.width

  val SCREEN_HEIGHT: Int = SCREEN_DIMENSION.height

  val PANELS_FOLDER: String =  "/panels/"

  val IMAGES_FOLDER: String =  "/images/"
}
