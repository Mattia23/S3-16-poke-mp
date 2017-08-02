package distributed.messages

/**
  * A PlayerLogoutMessage is used in order to communicate from a client to the server when a trainer logs out from the game,
  * and to communicate from the server to all clients when someone logs out from the game
  */
trait PlayerLogoutMessage {
  /**
    * @return the id of the user
    */
  def userId: Int
}

object PlayerLogoutMessage {
  def apply(userId: Int): PlayerLogoutMessage = new PlayerLogoutMessageImpl(userId)
}

/**
  * @inheritdoc
  * @param userId the id of the user
  */
class PlayerLogoutMessageImpl(override val userId: Int) extends PlayerLogoutMessage
