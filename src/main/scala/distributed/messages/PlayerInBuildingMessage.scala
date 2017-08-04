package distributed.messages

/**
  * A PlayerInBuildingMessage is used to communicate from a client to the server when a trainer enters/leaves a building,
  * and to communicate from the server to all clients when someone enters/leaves a building
  */
trait PlayerInBuildingMessage{
  /**
    * @return the id of the user
    */
  def userId: Int

  /**
    * @return the boolean that states if the player is or not in building
    */
  def isInBuilding: Boolean
}

object PlayerInBuildingMessage {
  def apply(userId: Int, isInBuilding: Boolean): PlayerInBuildingMessage =
    new PlayerInBuildingMessageImpl(userId, isInBuilding)
}

/**
  * @inheritdoc
  * @param userId the id of the user
  * @param isInBuilding the boolean that states if the player is or not in building
  */
class PlayerInBuildingMessageImpl(override val userId: Int,
                                  override val isInBuilding: Boolean) extends PlayerInBuildingMessage
