package app

import distributed.server.ServerConnection

object ServerMain extends App{

  ServerConnection.connection

  ServerConnection.close()

}
