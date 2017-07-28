package distributed

import model.environment.{Coordinate, CoordinateImpl}
import utilities.Settings

trait Player {
  def userId: Int

  def username: String

  def idImage: Int

  def position: Coordinate

  def position_=(coordinate: Coordinate): Unit

  def isVisible: Boolean

  def isVisible_=(visible: Boolean): Unit

  def isFighting: Boolean

  def isFighting_=(isFighting: Boolean): Unit
}

case class PlayerImpl(override val userId: Int, override val username: String,
                 override val idImage: Int, override var position: Coordinate = Settings.INITIAL_PLAYER_POSITION,
                 override var isVisible: Boolean = true, override var isFighting: Boolean = false) extends Player


