package distributed.messages

import model.environment.Coordinate

trait PlayerPositionMessage {
  def userId: Int

  def position: Coordinate
}

object PlayerPositionMessage {
  def apply(userId: Int, position: Coordinate): PlayerPositionMessage = new PlayerPositionMessageImpl(userId, position)
}

class PlayerPositionMessageImpl(override val userId: Int, override val position: Coordinate) extends PlayerPositionMessage

