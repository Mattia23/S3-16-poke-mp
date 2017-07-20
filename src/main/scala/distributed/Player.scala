package distributed

import model.environment.Coordinate

trait Player {
  def userId: Int

  def username: String

  def idImage: Int

  def position: Coordinate

  def position_=(coordinate: Coordinate): Unit
}

object Player {
  def apply(userId: Int, username: String, idImage: Int, position: Coordinate): Player =
    new PlayerImpl(userId, username, idImage, position)
}

class PlayerImpl(override val userId: Int, override val username: String,
                 override val idImage: Int, override var position: Coordinate) extends Player


