package app

import java.util.concurrent.ConcurrentHashMap

import distributed.{DistributedConnectionImpl, Player}
import distributed.server._

object ServerMain extends App{

  val connectedUsers = new ConcurrentHashMap[Int, Player]()
  val connection = DistributedConnectionImpl().connection
  PlayerLoginServerService(connection, connectedUsers).start()
  PlayerPositionServerService(connection, connectedUsers).start()
  PlayerInBuildingServerService(connection, connectedUsers).start()
  PlayerIsFightingServerService(connection, connectedUsers).start()
  PlayerLogoutServerService(connection, connectedUsers).start()

  //ServerConnection.close()

}
