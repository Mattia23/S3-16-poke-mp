package distributed.Messages

import model.environment.Coordinate

trait UserInformationMessage {
  def userId: Int

  def username: String

  def idImage: Int

  def position: Coordinate
}

object UserInformationMessage {
  def apply(userId: Int, username: String, idImage: Int, position: Coordinate): UserInformationMessage =
    new UserInformationMessageImpl(userId, username, idImage, position)
}

class UserInformationMessageImpl(override val userId: Int, override val username: String,
                                 override val idImage: Int, override val position: Coordinate) extends UserInformationMessage
