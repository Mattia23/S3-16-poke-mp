package distributed

import model.environment.Coordinate

trait Player {
  def userId: Int

  def username: String

  def idImage: Int

  def position: Coordinate

  def position_=(coordinate: Coordinate): Unit

  def isVisible: Boolean

  def isVisible_=(visible: Boolean): Unit

  /*def isFighting: Boolean

  def isFighting_=(isFighting: Boolean): Unit*/
}

object Player {
  def apply(userId: Int, username: String, idImage: Int, position: Coordinate, isVisible: Boolean/*, isFighting: Boolean*/): Player =
    new PlayerImpl(userId, username, idImage, position, isVisible/*, isFighting*/)
}

class PlayerImpl(override val userId: Int, override val username: String,
                 override val idImage: Int, override var position: Coordinate,
                 override var isVisible: Boolean/*, override var isFighting: Boolean*/) extends Player


