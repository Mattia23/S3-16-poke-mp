package distributed.messages

import model.environment.Coordinate

/**
  * A PlayerPositionMessage is used in order to communicate from a client to the server that the client's player has moved,
  * and to communicate from the server to all clients that a player has moved
  */
trait PlayerPositionMessage {
  /**
    * @return the id of the user
    */
  def userId: Int

  /**
    * @return the position of the player as Coordinate
    */
  def position: Coordinate
}

object PlayerPositionMessage {
  def apply(userId: Int, position: Coordinate): PlayerPositionMessage = new PlayerPositionMessageImpl(userId, position)
}

/**
  * @inheritdoc
  * @param userId the id of the user
  * @param position he position of the player as Coordinate
  */
class PlayerPositionMessageImpl(override val userId: Int,
                                override val position: Coordinate) extends PlayerPositionMessage

