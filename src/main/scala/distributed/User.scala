package distributed

trait User {
  def userId: Int

  def username: String

  def idImage: Int

  def xPosition: Int

  def yPosition: Int
}

object User {
  def apply(userId: Int, username: String, idImage: Int, xPosition: Int, yPosition: Int): User =
    new UserImpl(userId, username, idImage, xPosition, yPosition)
}

class UserImpl(override val userId: Int, override val username: String,
               override val idImage: Int, override val xPosition: Int, override val yPosition: Int) extends User
