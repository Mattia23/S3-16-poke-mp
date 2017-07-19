package app

import java.util.concurrent.ConcurrentHashMap

import distributed.{DistributedConnectionImpl, User}
import distributed.server.{PlayerConnectionServerManager, PlayerPositionServerManager}

object ServerMain extends App{

  val connectedUsers = new ConcurrentHashMap[Int, User]()
  DistributedConnectionImpl().connection
  PlayerConnectionServerManager(connectedUsers).start()
  PlayerPositionServerManager(connectedUsers).start()

  //ServerConnection.close()

}
