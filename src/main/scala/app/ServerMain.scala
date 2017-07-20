package app

import java.util.concurrent.ConcurrentHashMap

import distributed.{DistributedConnectionImpl, Player}
import distributed.server.{PlayerLoginServerService, PlayerPositionServerService}

object ServerMain extends App{

  val connectedUsers = new ConcurrentHashMap[Int, Player]()
  val connection = DistributedConnectionImpl().connection
  PlayerLoginServerService(connection, connectedUsers).start()
  PlayerPositionServerService(connection, connectedUsers).start()

  //ServerConnection.close()

}
