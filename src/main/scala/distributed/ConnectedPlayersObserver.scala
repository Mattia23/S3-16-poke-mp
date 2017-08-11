package distributed

/**
  * Observer of the changes made to the currently connected players
  */
trait ConnectedPlayersObserver {
  /**
    * Manages when a player is added to the connected players
    * @param player player added to the connected players
    */
  def newPlayerAdded(player: Player)

  /**
    * Manages when a player position is updated
    * @param userId id of the player who changed position
    */
  def playerPositionUpdated(userId: Int)

  /**
    * Manages when a player logs out
    * @param userId id of the disconnected player
    */
  def playerRemoved(userId: Int)
}
