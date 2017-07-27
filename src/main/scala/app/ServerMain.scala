package app

import java.util.concurrent.ConcurrentHashMap

import distributed.{DistributedConnectionImpl, Player}
import distributed.server._

object ServerMain extends App{
  val connection = DistributedConnectionImpl().connection
  val connectedUsers = new ConcurrentHashMap[Int, Player]()

  CommunicationService(CommunicationService.Service.PlayerLogin, connection, connectedUsers).start()
  CommunicationService(CommunicationService.Service.PlayerPosition, connection, connectedUsers).start()
  CommunicationService(CommunicationService.Service.PlayerInBuilding, connection, connectedUsers).start()
  CommunicationService(CommunicationService.Service.PlayerLogout, connection, connectedUsers).start()
}
