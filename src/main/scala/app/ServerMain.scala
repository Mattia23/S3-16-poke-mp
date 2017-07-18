package app

import distributed.DistributedConnectionImpl
import distributed.server.PlayerConnectionServerManager

object ServerMain extends App{

  DistributedConnectionImpl().connection
  PlayerConnectionServerManager().start()

  //ServerConnection.close()

}
