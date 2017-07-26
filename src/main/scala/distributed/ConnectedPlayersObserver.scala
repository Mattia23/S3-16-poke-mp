package distributed

trait ConnectedPlayersObserver {
  def newPlayerAdded()

  def playerPositionUpdated()

  def playerRemoved()
}
