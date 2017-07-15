package distributed

import model.entities.TrainerSprites
import model.environment.Coordinate

trait User {
  def userId: Int

  def username: String

  def sprites: TrainerSprites

  def position: Coordinate
}

object User {
  def apply(userId: Int, username: String, sprites: TrainerSprites, position: Coordinate): User =
    new UserImpl(userId, username, sprites, position)
}

class UserImpl(override val userId: Int, override val username: String,
               override val sprites: TrainerSprites, override val position: Coordinate) extends User
