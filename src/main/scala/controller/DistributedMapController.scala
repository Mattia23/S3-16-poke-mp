package controller

import java.util.concurrent.{ConcurrentHashMap, ConcurrentMap}

import com.rabbitmq.client.Connection
import distributed.client._
import distributed.Player
import distributed.client.{NewPlayerInGameClientManager, PlayerPositionClientManager}
import model.entities.TrainerSprites
import model.environment.Coordinate
import utilities.Settings

trait DistributedMapController{
  def connectedPlayers: ConcurrentMap[Int, Player]

  def playersTrainerSprites: ConcurrentMap[Int, String]

  def sendTrainerPosition(position: Coordinate): Unit

  def sendTrainerInBuilding(isInBuilding: Boolean): Unit

  def challengeTrainer(otherTrainerId: Int, wantToFight: Boolean): Unit

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
  private val trainerDialogueClientManager: TrainerDialogueClientManager = TrainerDialogueClientManager(connection, mapController)

  newPlayerInGameManager.receiveNewPlayerInGame(trainerId, connectedPlayers)
  playerPositionManager.receiveOtherPlayerPosition(trainerId, connectedPlayers)
  playerInBuildingManager.receiveOtherPlayerIsInBuilding(trainerId, connectedPlayers)
  playerLogoutManager.receiveOtherPlayerLogout(trainerId, connectedPlayers)
  trainerDialogueClientManager.receiveResponse()

  override val playersTrainerSprites: ConcurrentMap[Int, String] = new ConcurrentHashMap[Int, String]()

  override def sendTrainerPosition(position: Coordinate): Unit = playerPositionManager.sendPlayerPosition(trainerId, position)

  override def sendTrainerInBuilding(isInBuilding: Boolean): Unit = playerInBuildingManager.sendPlayerIsInBuilding(trainerId, isInBuilding)

  override def challengeTrainer(otherTrainerId: Int, wantToFight: Boolean): Unit =
    trainerDialogueClientManager.sendDialogueRequest(otherTrainerId, wantToFight)

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
        distributedMapController.connectedPlayers.values() forEach (player =>
          distributedMapController.playersTrainerSprites.put(player.userId, TrainerSprites.selectTrainerSprite(player.idImage).frontS.image))
      }

      try
        Thread.sleep(Settings.GAME_REFRESH_TIME)
      catch {
        case e: InterruptedException => System.out.println(e)
      }
    }
  }

  def terminate(): Unit = {
    stopped = true
  }

}