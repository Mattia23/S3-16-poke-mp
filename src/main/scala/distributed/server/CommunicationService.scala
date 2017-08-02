package distributed.server

import com.rabbitmq.client.Connection
import distributed.ConnectedPlayers

trait CommunicationService {
  def start()
}

object CommunicationService {

  object Service extends Enumeration {
    type Service = Value
    val PlayerInBuilding, PlayerLogin, PlayerLogout, PlayerPosition, PlayerIsBusy = Value
  }

  def apply(service: Service.Value, connection: Connection, connectedPlayers: ConnectedPlayers): CommunicationService = service match {
    case Service.PlayerInBuilding => PlayerInBuildingServerService(connection, connectedPlayers)
    case Service.PlayerLogin => PlayerLoginServerService(connection, connectedPlayers)
    case Service.PlayerLogout => PlayerLogoutServerService(connection, connectedPlayers)
    case Service.PlayerPosition => PlayerPositionServerService(connection, connectedPlayers)
    case Service.PlayerIsBusy => PlayerIsBusyServerService(connection, connectedPlayers)
  }
}
