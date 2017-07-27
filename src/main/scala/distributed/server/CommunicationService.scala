package distributed.server

import java.util.concurrent.ConcurrentMap

import com.rabbitmq.client.Connection
import distributed.Player

trait CommunicationService {
  def start()
}

object CommunicationService {

  object Service extends Enumeration {
    type Service = Value
    val PlayerInBuilding, PlayerLogin, PlayerLogout, PlayerPosition = Value
  }

  def apply(service: CommunicationService.Service.Value, connection: Connection, connectedPlayers: ConcurrentMap[Int, Player]): CommunicationService = service match {
    case Service.PlayerInBuilding => PlayerInBuildingServerService(connection, connectedPlayers)
    case Service.PlayerLogin => PlayerLoginServerService(connection, connectedPlayers)
    case Service.PlayerLogout => PlayerLogoutServerService(connection, connectedPlayers)
    case Service.PlayerPosition => PlayerPositionServerService(connection, connectedPlayers)
  }
}
