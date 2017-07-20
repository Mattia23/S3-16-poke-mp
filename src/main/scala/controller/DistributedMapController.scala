package controller

import java.util.concurrent.{ConcurrentHashMap, ConcurrentMap}

import com.rabbitmq.client.Connection
import distributed.{CommunicationService, Player}
import distributed.client.{NewPlayerInGameClientManager, NewPlayerInGameClientManagerImpl, PlayerPositionClientManager, PlayerPositionClientManagerImpl}
import model.entities.TrainerSprites
import model.environment.Coordinate
import utilities.Settings

trait DistributedMapController{
  def connectedPlayers: ConcurrentMap[Int, Player]

  def playersTrainerSprites: ConcurrentMap[Int, String]

  def sendTrainerPosition(trainerId: Int, position: Coordinate): Unit
}

object DistributedMapControllerImpl{
  def apply(mapController: GameController, connection: Connection, connectedPlayers: ConcurrentMap[Int, Player]): DistributedMapController =
    new DistributedMapControllerImpl(mapController, connection, connectedPlayers)
}

class DistributedMapControllerImpl(private val mapController: GameController, private val connection: Connection, override val connectedPlayers: ConcurrentMap[Int, Player]) extends DistributedMapController{

  private val newPlayerInGame: NewPlayerInGameClientManager = NewPlayerInGameClientManager(connection)
  private val playerPositionManager: PlayerPositionClientManager = PlayerPositionClientManager(connection)

  newPlayerInGame.receiveNewPlayerInGame(mapController.trainer.id, connectedPlayers)
  playerPositionManager.receiveOtherPlayerPosition(mapController.trainer.id, connectedPlayers)

  override val playersTrainerSprites: ConcurrentMap[Int, String] = new ConcurrentHashMap[Int, String]()

  override def sendTrainerPosition(trainerId: Int, position: Coordinate): Unit = playerPositionManager.sendPlayerPosition(trainerId, position)
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