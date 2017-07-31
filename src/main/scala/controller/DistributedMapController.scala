package controller

import java.util.concurrent.{ConcurrentHashMap, ConcurrentMap, ExecutorService, Executors}

import com.rabbitmq.client.Connection
import distributed.client._
import distributed.{ConnectedPlayers, ConnectedPlayersObserver, Player, PlayerPositionDetails}
import model.entities.TrainerSprites
import model.environment.Direction.Direction
import model.environment.{Coordinate, CoordinateImpl, Direction}
import model.map.{Movement, OtherTrainerMovement}

import scala.collection.immutable.HashMap
import scala.concurrent.{ExecutionContext, Future}

trait DistributedMapController{
  def connectedPlayers: ConnectedPlayers

  def playersPositionDetails: ConcurrentMap[Int, PlayerPositionDetails]

  def sendTrainerPosition(position: Coordinate): Unit

  def sendTrainerInBuilding(isInBuilding: Boolean): Unit

  def sendTrainerIsFighting(isInBuilding: Boolean): Unit

  def challengeTrainer(otherPlayerId: Int, wantToFight: Boolean, isFirst: Boolean): Unit

  def playerLogout(): Unit
}

object DistributedMapController{
  def apply(mapController: GameController, connection: Connection, connectedPlayers: ConnectedPlayers): DistributedMapController =
    new DistributedMapControllerImpl(mapController, connection, connectedPlayers)
}

class DistributedMapControllerImpl(private val mapController: GameController,
                                   private val connection: Connection,
                                   override val connectedPlayers: ConnectedPlayers) extends DistributedMapController with ConnectedPlayersObserver{

  private val trainerId: Int = mapController.trainer.id
  private val newPlayerInGameManager: NewPlayerInGameClientManager = NewPlayerInGameClientManager(connection)
  private val playerPositionManager: PlayerPositionClientManager = PlayerPositionClientManager(connection)
  private val playerInBuildingManager: PlayerInBuildingClientManager = PlayerInBuildingClientManager(connection)
  private val playerLogoutManager: PlayerLogoutClientManager = PlayerLogoutClientManager(connection)
  private val trainerDialogueClientManager: TrainerDialogueClientManager = TrainerDialogueClientManager(connection, mapController)
  private val playerIsFightingManager: PlayerIsFightingClientManager = PlayerIsFightingClientManager(connection)

  private val poolSize: Int = Runtime.getRuntime.availableProcessors + 1
  private val executor: ExecutorService = Executors.newFixedThreadPool(poolSize)
  private implicit val executionContext = ExecutionContext.fromExecutor(executor)

  newPlayerInGameManager.receiveNewPlayerInGame(trainerId, connectedPlayers)
  playerPositionManager.receiveOtherPlayerPosition(trainerId, connectedPlayers)
  playerInBuildingManager.receiveOtherPlayerIsInBuilding(trainerId, connectedPlayers)
  playerLogoutManager.receiveOtherPlayerLogout(trainerId, connectedPlayers)
  trainerDialogueClientManager.receiveResponse()
  playerIsFightingManager.receiveOtherPlayerIsFighting(trainerId, connectedPlayers)

  override val playersPositionDetails: ConcurrentMap[Int, PlayerPositionDetails] = new ConcurrentHashMap[Int, PlayerPositionDetails]()

  override def sendTrainerPosition(position: Coordinate): Unit = playerPositionManager.sendPlayerPosition(trainerId, position)

  override def sendTrainerInBuilding(isInBuilding: Boolean): Unit = playerInBuildingManager.sendPlayerIsInBuilding(trainerId, isInBuilding)

  override def challengeTrainer(otherPlayerId: Int, wantToFight: Boolean, isFirst: Boolean): Unit =
    trainerDialogueClientManager.sendDialogueRequest(otherPlayerId, wantToFight, isFirst)

  override def sendTrainerIsFighting(isFighting: Boolean): Unit = playerIsFightingManager.sendPlayerIsFighting(trainerId, isFighting)

  override def playerLogout(): Unit = {
    playerLogoutManager.sendPlayerLogout(trainerId)
    connection.close()
  }

  override def newPlayerAdded(player: Player): Unit = {
    val playerDetails = PlayerPositionDetails(player.userId, player.position.x, player.position.y, TrainerSprites.selectTrainerSprite(player.idImage).frontS)
    playersPositionDetails.put(player.userId, playerDetails)
  }

  override def playerPositionUpdated(userId: Int): Unit = {
    val positionDetails: PlayerPositionDetails = playersPositionDetails.get(userId)
    val player: Player = connectedPlayers.get(userId)

    val initialPosition = CoordinateImpl(positionDetails.coordinateX.asInstanceOf[Int], positionDetails.coordinateY.asInstanceOf[Int])
    val nextPosition = player.position
    var direction: Direction = null

    if (Math.abs(nextPosition.x - initialPosition.x) + Math.abs(nextPosition.y - initialPosition.y) > 1) {
      positionDetails.coordinateX = nextPosition.x
      positionDetails.coordinateY = nextPosition.y
    } else {
      (initialPosition, nextPosition) match {
        case (CoordinateImpl(x1, _), CoordinateImpl(x2, _)) if x2 == x1 + 1 => direction = Direction.RIGHT
        case (CoordinateImpl(x1, _), CoordinateImpl(x2, _)) if x2 == x1 - 1 => direction = Direction.LEFT
        case (CoordinateImpl(_, y1), CoordinateImpl(_, y2)) if y2 == y1 + 1 => direction = Direction.DOWN
        case _ => direction = Direction.UP
      }

      val movement: Movement = OtherTrainerMovement(userId, playersPositionDetails, TrainerSprites.selectTrainerSprite(player.idImage))
      Future {
        movement.walk(initialPosition, direction, nextPosition)
      }
    }
  }

  override def playerRemoved(userId: Int): Unit = playersPositionDetails.remove(userId)
}