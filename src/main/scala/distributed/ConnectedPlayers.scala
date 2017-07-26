package distributed

import java.util
import java.util.concurrent.{ConcurrentHashMap, ConcurrentMap}

trait ConnectedPlayers {
  def add(userId: Int, player: Player): Unit

  def get(userId: Int): Player

  def addAll(map: util.Map[Int, Player]): Unit

  def getAll: util.Map[Int, Player]

  def remove(userId: Int): Unit

  def addObserver(observer: ConnectedPlayersObserver): Unit
}

object ConnectedPlayers {
  def apply(): ConnectedPlayers = new ConnectedPlayersImpl()
}

class ConnectedPlayersImpl extends ConnectedPlayers{
  val map: ConcurrentMap[Int, Player] = new ConcurrentHashMap[Int, Player]()

  var observers: List[ConnectedPlayersObserver] = List[ConnectedPlayersObserver]()

  override def add(userId: Int, player: Player): Unit = {
    map.put(userId, player)
    observers foreach ( _ newPlayerAdded())
  }

  override def get(userId: Int): Player = map get userId

  override def addAll(map: util.Map[Int, Player]): Unit = {
    map forEach (add(_,_))
  }

  override def remove(idUser: Int): Unit = {
    map remove idUser
    observers foreach ( _ playerRemoved())
  }

  override def getAll: util.Map[Int, Player] = map

  override def addObserver(observer: ConnectedPlayersObserver): Unit = observers = observers :+ observer
}
