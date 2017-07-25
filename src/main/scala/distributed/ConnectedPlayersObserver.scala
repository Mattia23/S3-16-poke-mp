package distributed

trait ConnectedPlayersObserver {
  def newPlayerAdded()

  def playerUpdated()

  def playerRemoved()
}
