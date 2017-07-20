package app

import java.util.concurrent.ConcurrentHashMap

import distributed.{DistributedConnectionImpl, User}
import distributed.server.{PlayerConnectionServerManager, PlayerPositionServerManager}

object ServerMain extends App{

  val connectedUsers = new ConcurrentHashMap[Int, User]()
  val connection = DistributedConnectionImpl().connection
  PlayerConnectionServerManager(connection, connectedUsers).start()
  PlayerPositionServerManager(connection, connectedUsers).start()

  //ServerConnection.close()

}
