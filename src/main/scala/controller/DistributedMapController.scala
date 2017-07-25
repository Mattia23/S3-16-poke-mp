package controller

import java.util.concurrent.{ConcurrentHashMap, ConcurrentMap}

import com.rabbitmq.client.Connection
import distributed.client._
import distributed.{Player, PlayerPositionDetails}
import distributed.client.{NewPlayerInGameClientManager, PlayerPositionClientManager}
import model.entities.TrainerSprites
import model.environment.Coordinate
import model.map.{Movement, OtherTrainerMovement}
import utilities.Settings

trait DistributedMapController{
  def connectedPlayers: ConcurrentMap[Int, Player]

  def playersPositionDetails: ConcurrentMap[Int, PlayerPositionDetails]

  def sendTrainerPosition(position: Coordinate): Unit

  def sendTrainerInBuilding(isInBuilding: Boolean): Unit

  def playerLogout(): Unit
}

object DistributedMapController{
  def apply(mapController: GameController, connection: Connection, connectedPlayers: ConcurrentMap[Int, Player]): DistributedMapController =
    new DistributedMapControllerImpl(mapController, connection, connectedPlayers)
}

class DistributedMapControllerImpl(private val mapController: GameController, private val connection: Connection, override val connectedPlayers: ConcurrentMap[Int, Player]) extends DistributedMapController{

  private val trainerId: Int = mapController.trainer.id
  private val newPlayerInGameManager: NewPlayerInGameClientManager = NewPlayerInGameClientManager(connection)
  private val playerPositionManager: PlayerPositionClientManager = PlayerPositionClientManager(connection)
  private val playerInBuildingManager: PlayerInBuildingClientManager = PlayerInBuildingClientManager(connection)
  private val playerLogoutManager: PlayerLogoutClientManager = PlayerLogoutClientManager(connection)

  newPlayerInGameManager.receiveNewPlayerInGame(trainerId, connectedPlayers)
  playerPositionManager.receiveOtherPlayerPosition(trainerId, connectedPlayers)
  playerInBuildingManager.receiveOtherPlayerIsInBuilding(trainerId, connectedPlayers)
  playerLogoutManager.receiveOtherPlayerLogout(trainerId, connectedPlayers)

  override val playersPositionDetails: ConcurrentMap[Int, PlayerPositionDetails] = new ConcurrentHashMap[Int, PlayerPositionDetails]()

  //private val otherTrainerMovement: Movement = new OtherTrainerMovement(playersPositionDetails)

  override def sendTrainerPosition(position: Coordinate): Unit = playerPositionManager.sendPlayerPosition(trainerId, position)

  override def sendTrainerInBuilding(isInBuilding: Boolean): Unit = playerInBuildingManager.sendPlayerIsInBuilding(trainerId, isInBuilding)

  override def playerLogout(): Unit = {
    playerLogoutManager.sendPlayerLogout(trainerId)
    connection.close()
  }
}

class DistributedMapControllerAgent(private val mapController: GameController, private val distributedMapController: DistributedMapController) extends Thread {
  var stopped: Boolean = false

  override def run(): Unit = {
    while(mapController.isInGame && !stopped){
      if(!mapController.isInPause){
        distributedMapController.connectedPlayers.values() forEach (player => addPlayer(player) )
      }
      try
        Thread.sleep(Settings.GAME_REFRESH_TIME)
      catch {
        case e: InterruptedException => System.out.println(e)
      }
    }
  }

  private def addPlayer(player: Player): Unit = {
    if(distributedMapController.playersPositionDetails.get(player.userId) == null) {
      val playerDetails = PlayerPositionDetails(player.userId, player.position.x, player.position.y, TrainerSprites.selectTrainerSprite(player.idImage).frontS)
      distributedMapController.playersPositionDetails.put(player.userId, playerDetails)
    }
  }

  def terminate(): Unit = {
    stopped = true
  }

}