package distributed.messages

trait PlayerInBuildingMessage{
  def userId: Int

  def isInBuilding: Boolean
}

object PlayerInBuildingMessage {
  def apply(userId: Int, isInBuilding: Boolean): PlayerInBuildingMessage =
    new PlayerInBuildingMessageImpl(userId, isInBuilding)
}

class PlayerInBuildingMessageImpl(override val userId: Int,
                                  override val isInBuilding: Boolean) extends PlayerInBuildingMessage
