package distributed

trait ConnectedPlayersObserver {
  def newPlayerAdded(player: Player)

  def playerPositionUpdated(userId: Int)

  def playerRemoved(userId: Int)
}
