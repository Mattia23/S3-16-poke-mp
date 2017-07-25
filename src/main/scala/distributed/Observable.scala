package distributed

trait Observable {
  def addObserver(observer: ConnectedPlayersObserver)
}
