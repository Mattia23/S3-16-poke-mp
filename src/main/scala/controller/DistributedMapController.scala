package controller

import java.util.concurrent.{ConcurrentHashMap, ConcurrentMap, ExecutorService, Executors}
import com.rabbitmq.client.Connection
import distributed.client._
import distributed.{ConnectedPlayers, ConnectedPlayersObserver, Player, PlayerPositionDetails}
import model.entities.TrainerSprites
import model.environment.Direction.Direction
import model.environment.{Coordinate, CoordinateImpl, Direction}
import model.map.{Movement, OtherTrainerMovement}
import scala.concurrent.{ExecutionContext, Future}

/**
  * DistributedMapController manages the interaction with the server allowing the game to operate at a distributed level.
  * It manages all trainers in the map and the interactions between them.
  */
trait DistributedMapController{
  /**
    * @return players currently connected to the game
    */
  def connectedPlayers: ConnectedPlayers

  /**
    * @return the details of the position of all the players
    */
  def playersPositionDetails: ConcurrentMap[Int, PlayerPositionDetails]

  /**
    * Sends to the server the current trainer position
    * @param position current trainer position
    */
  def sendTrainerPosition(position: Coordinate): Unit

  /**
    * Sends to the server if the trainer is in a building or not
    * @param isInBuilding indicates whether a player is in a building
    */
  def sendTrainerInBuilding(isInBuilding: Boolean): Unit

  /**
    * Sends to the server if the trainer is busy or not
    * @param isBusy indicates whether a player is busy
    */
  def sendTrainerIsBusy(isBusy: Boolean): Unit

  /**
    * Sends a fight request to the otherPlayerId
    * @param otherPlayerId the recipient trainer id
    */
  def sendChallengeToTrainer(otherPlayerId: Int): Unit

  /**
    * Performs the logout of the player
    */
  def playerLogout(): Unit
}

object DistributedMapController{
  def apply(mapController: GameController, connection: Connection, connectedPlayers: ConnectedPlayers): DistributedMapController =
    new DistributedMapControllerImpl(mapController, connection, connectedPlayers)
}

/**
  * @inheritdoc
  * @param mapController instance of map controller
  * @param connection instance of connection with RabbitMQ
  * @param connectedPlayers players currently connected to the game
  */
class DistributedMapControllerImpl(private val mapController: GameController,
                                   private val connection: Connection,
                                   override val connectedPlayers: ConnectedPlayers) extends DistributedMapController with ConnectedPlayersObserver{

  private val trainerId: Int = mapController.trainer.id
  private val newPlayerInGameManager: NewPlayerInGameClientManager = NewPlayerInGameClientManager(connection)
  private val playerPositionManager: PlayerPositionClientManager = PlayerPositionClientManager(connection)
  private val playerInBuildingManager: PlayerInBuildingClientManager = PlayerInBuildingClientManager(connection)
  private val playerLogoutManager: PlayerLogoutClientManager = PlayerLogoutClientManager(connection)
  private val trainerDialogueClientManager: TrainerDialogueClientManager = TrainerDialogueClientManager(connection, mapController)
  private val playerIsBusyManager: PlayerIsBusyClientManager = PlayerIsBusyClientManager(connection)

  private val poolSize: Int = Runtime.getRuntime.availableProcessors + 1
  private val executor: ExecutorService = Executors.newFixedThreadPool(poolSize)
  private implicit val executionContext = ExecutionContext.fromExecutor(executor)

  newPlayerInGameManager.receiveNewPlayerInGame(trainerId, connectedPlayers)
  playerPositionManager.receiveOtherPlayerPosition(trainerId, connectedPlayers)
  playerInBuildingManager.receiveOtherPlayerIsInBuilding(trainerId, connectedPlayers)
  playerLogoutManager.receiveOtherPlayerLogout(trainerId, connectedPlayers)
  trainerDialogueClientManager.receiveResponse()
  playerIsBusyManager.receiveOtherPlayerIsBusy(trainerId, connectedPlayers)

  override val playersPositionDetails: ConcurrentMap[Int, PlayerPositionDetails] = new ConcurrentHashMap[Int, PlayerPositionDetails]()

  /**
    * @inheritdoc
    * @param position current trainer position
    */
  override def sendTrainerPosition(position: Coordinate): Unit = playerPositionManager.sendPlayerPosition(trainerId, position)

  /**
    * @inheritdoc
    * @param isInBuilding indicates whether a player is in a building
    */
  override def sendTrainerInBuilding(isInBuilding: Boolean): Unit = playerInBuildingManager.sendPlayerIsInBuilding(trainerId, isInBuilding)

  /**
    * @inheritdoc
    * @param isBusy indicates whether a player is busy
    */
  override def sendTrainerIsBusy(isBusy: Boolean): Unit = playerIsBusyManager.sendPlayerIsBusy(trainerId, isBusy)

  /**
    * @inheritdoc
    * @param otherPlayerId the recipient trainer id
    */
  override def sendChallengeToTrainer(otherPlayerId: Int): Unit =
    trainerDialogueClientManager.sendDialogueRequest(otherPlayerId, wantToFight = true, isFirst = true)

  /**
    * @inheritdoc
    */
  override def playerLogout(): Unit = {
    playerLogoutManager sendPlayerLogout trainerId
    trainerDialogueClientManager.close()
    connection.close()
  }

  /**
    * @inheritdoc
    * @param player player added to the connected players
    */
  override def newPlayerAdded(player: Player): Unit = {
    val playerDetails = PlayerPositionDetails(player.userId, player.position.x, player.position.y, TrainerSprites(player.idImage).frontS)
    playersPositionDetails.put(player.userId, playerDetails)
  }

  /**
    * @inheritdoc
    * @param userId id of the player who changed position
    */
  override def playerPositionUpdated(userId: Int): Unit = {
    val positionDetails: PlayerPositionDetails = playersPositionDetails get userId
    val player: Player = connectedPlayers get userId

    val initialPosition = CoordinateImpl(positionDetails.coordinateX.asInstanceOf[Int], positionDetails.coordinateY.asInstanceOf[Int])
    val nextPosition = player.position
    var direction: Direction = null

    if (Math.abs(nextPosition.x - initialPosition.x) + Math.abs(nextPosition.y - initialPosition.y) == 1) {
      (initialPosition, nextPosition) match {
        case (CoordinateImpl(x1, _), CoordinateImpl(x2, _)) if x2 == x1 + 1 => direction = Direction.RIGHT
        case (CoordinateImpl(x1, _), CoordinateImpl(x2, _)) if x2 == x1 - 1 => direction = Direction.LEFT
        case (CoordinateImpl(_, y1), CoordinateImpl(_, y2)) if y2 == y1 + 1 => direction = Direction.DOWN
        case _ => direction = Direction.UP
      }
      val movement: Movement = OtherTrainerMovement(userId, playersPositionDetails, TrainerSprites(player.idImage))
      Future {
        movement.walk(initialPosition, direction, nextPosition)
      }
    } else {
      positionDetails.coordinateX = nextPosition.x
      positionDetails.coordinateY = nextPosition.y
    }
  }

  /**
    * @inheritdoc
    * @param userId id of the disconnected player
    */
  override def playerRemoved(userId: Int): Unit = playersPositionDetails remove userId
}