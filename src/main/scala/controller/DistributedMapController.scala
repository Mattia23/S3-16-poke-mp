package controller

import java.util.concurrent.{ConcurrentHashMap, ConcurrentMap}

import com.rabbitmq.client.Connection
import distributed.{CommunicationService, User}
import distributed.client.{NewPlayerInGameClientManager, PlayerPositionClientManager, PlayerPositionClientManagerImpl}
import model.entities.TrainerSprites
import model.environment.Coordinate
import utilities.Settings

trait DistributedMapController{
  def connectedUsers: ConcurrentMap[Int, User]

  def usersTrainerSprites: ConcurrentMap[Int, String]

  def sendTrainerPosition(trainerId: Int, position: Coordinate): Unit
}

object DistributedMapControllerImpl{
  def apply(mapController: GameController, connection: Connection, connectedUsers: ConcurrentMap[Int, User]): DistributedMapController =
    new DistributedMapControllerImpl(mapController, connection, connectedUsers)
}

class DistributedMapControllerImpl(private val mapController: GameController, private val connection: Connection, override val connectedUsers: ConcurrentMap[Int, User]) extends DistributedMapController{

  private val newPlayerInGame: CommunicationService = NewPlayerInGameClientManager(connection, mapController.trainer.id, connectedUsers)
  private val playerPositionManager: PlayerPositionClientManager = PlayerPositionClientManagerImpl(connection)

  newPlayerInGame.start()
  playerPositionManager.receiveOtherPlayerPosition(mapController.trainer.id, connectedUsers)

  override val usersTrainerSprites: ConcurrentMap[Int, String] = new ConcurrentHashMap[Int, String]()

  override def sendTrainerPosition(trainerId: Int, position: Coordinate): Unit = playerPositionManager.sendPlayerPosition(trainerId, position)
}


class DistributedMapControllerAgent(private val mapController: GameController, private val distributedMapController: DistributedMapController) extends Thread {
  var stopped: Boolean = false

  override def run(): Unit = {
    while(mapController.isInGame && !stopped){
      if(!mapController.isInPause){
        distributedMapController.connectedUsers.values() forEach (user =>
          distributedMapController.usersTrainerSprites.put(user.userId, TrainerSprites.selectTrainerSprite(user.idImage).frontS.image))
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