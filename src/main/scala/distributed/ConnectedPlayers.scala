package distributed

import java.util
import java.util.concurrent.{ConcurrentHashMap, ConcurrentMap}

import scala.collection.mutable.ListBuffer

trait ConnectedPlayers {
  def add(idUser: Int, player: Player): Unit

  def get(idUser: Int): Player

  def addAll(map: util.Map[Int, Player]): Unit

  def getAll: util.Map[Int, Player]

  def remove(idUser: Int): Unit
}

object ConnectedPlayers {
  def apply(): ConnectedPlayers = new ConnectedPlayersImpl()
}

class ConnectedPlayersImpl extends ConnectedPlayers with Observable{
  val map: ConcurrentMap[Int, Player] = new ConcurrentHashMap[Int, Player]()

  val observers: ListBuffer[ConnectedPlayersObserver] = ListBuffer[ConnectedPlayersObserver]()

  override def add(idUser: Int, player: Player): Unit = {
    map.put(idUser, player)
    observers foreach( _ newPlayerAdded())
  }

  override def get(idUser: Int): Player = map get idUser

  override def addAll(map: util.Map[Int, Player]): Unit = this.map putAll map

  override def remove(idUser: Int): Unit = {
    map remove idUser
    observers foreach( _ playerRemoved())
  }

  override def getAll(): util.Map[Int, Player] = map

  override def addObserver(observer: ConnectedPlayersObserver): Unit = observers += observer
}
