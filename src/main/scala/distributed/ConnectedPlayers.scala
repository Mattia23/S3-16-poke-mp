package distributed

import java.util
import java.util.concurrent.{ConcurrentHashMap, ConcurrentMap}

import model.environment.Coordinate

trait ConnectedPlayers {
  def add(userId: Int, player: Player): Unit

  def get(userId: Int): Player

  def addAll(map: util.Map[Int, Player]): Unit

  def getAll: util.Map[Int, Player]

  def remove(userId: Int): Unit

  def containsPlayer(userId: Int): Boolean

  def updateTrainerPosition(userId: Int, position: Coordinate): Unit

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
    observers foreach ( _ newPlayerAdded player)
  }

  override def get(userId: Int): Player = map get userId

  override def addAll(map: util.Map[Int, Player]): Unit = {
    map forEach (add(_,_))
  }

  override def remove(userId: Int): Unit = {
    map remove userId
    observers foreach ( _ playerRemoved userId)
  }

  override def getAll: util.Map[Int, Player] = map

  override def containsPlayer(userId: Int): Boolean = map.containsKey(userId)

  override def updateTrainerPosition(userId: Int, position: Coordinate): Unit = {
      get(userId).position = position
      observers foreach( _ playerPositionUpdated userId)
  }

  override def addObserver(observer: ConnectedPlayersObserver): Unit = observers = observers :+ observer
}
