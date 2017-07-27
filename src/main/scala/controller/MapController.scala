package controller

import java.util
import java.util.concurrent.ConcurrentMap

import com.rabbitmq.client.Connection
import database.remote.DBConnect
import distributed.Player
import distributed.client.{BattleClientManager, BattleClientManagerImpl}
import model.entities.Trainer
import model.environment.Direction.Direction
import model.environment.{Audio, Coordinate, CoordinateImpl}
import model.map._
import utilities.Settings
import view._

import scala.util.Random

object MapController {
  def apply(view: View, _trainer: Trainer, connection: Connection, connectedUsers: ConcurrentMap[Int, Player]): GameController = new MapController(view, _trainer, connection, connectedUsers)

  private final val RANDOM_MAX_VALUE = 10
  private final val MIN_VALUE_TO_FIND_POKEMON = 8
  private final val POKEMON_CENTER_BUILDING = "Pokemon center"
  private final val LABORATORY_BUILDING = "Laboratory"
}

class MapController(private val view: View, private val _trainer: Trainer, private val connection: Connection, private val connectedUsers: ConcurrentMap[Int, Player]) extends GameControllerImpl(view, _trainer){
  import MapController._

  private val gameMap = MapCreator.create(Settings.MAP_HEIGHT, Settings.MAP_WIDTH, InitialTownElements())
  private var lastCoordinates: Coordinate = _
  private val distributedMapController: DistributedMapController = DistributedMapController(this, connection, connectedUsers)
  private var distributedAgent: DistributedMapControllerAgent = _
  private var currentDialogue: DialoguePanel = _
  audio = Audio(Settings.MAP_SONG)


  override protected def doStart(): Unit = {
    initView()
    distributedAgent = new DistributedMapControllerAgent(this, distributedMapController)
    distributedAgent.start()
    if(trainer.capturedPokemons.isEmpty){
      doFirstLogin()
    }else {
      audio.loop()
    }
  }

  private def initView(): Unit = {
    view.showMap(this, distributedMapController, gameMap)
    gamePanel = view.getGamePanel
  }

  private def doFirstLogin(): Unit = {
    pause()
    updateLastCoordinateToBuilding(LABORATORY_BUILDING)
    new LaboratoryController(this.view, this, trainer).start()
  }

  private def updateLastCoordinateToBuilding(building: String): Unit = {
    for( x <- 0 until gameMap.width){
      for( y <- 0 until gameMap.height){
        (building, gameMap.map(x)(y)) match {
          case (LABORATORY_BUILDING, tile:Laboratory) =>
            lastCoordinates = CoordinateImpl(tile.topLeftCoordinate.x + tile.doorCoordinates.x, tile.topLeftCoordinate.y + tile.doorCoordinates.y + 1)
            return
          case (POKEMON_CENTER_BUILDING, tile: PokemonCenter) =>
            lastCoordinates = CoordinateImpl(tile.topLeftCoordinate.x + tile.doorCoordinates.x, tile.topLeftCoordinate.y + tile.doorCoordinates.y + 1)
            distributedMapController.sendTrainerPosition(lastCoordinates)
            return
          case _ =>
        }
      }
    }
  }

  override protected def doPause(): Unit = {
    if(distributedAgent != null) distributedAgent.terminate()
    lastCoordinates = trainer.coordinate
    audio.stop()
    //distributedMapController.connectedPlayers.get(trainer.id).isFighting_=(true)
    setFocusableOff()
  }

  override protected def doResume(): Unit = {
    distributedMapController.sendTrainerInBuilding(true)
    if(trainer.getFirstAvailableFavouritePokemon <= 0) {
      DBConnect.rechangeAllTrainerPokemon(trainer.id)
      setTrainerSpriteFront()
      updateLastCoordinateToBuilding(POKEMON_CENTER_BUILDING)
    }
    trainer.coordinate = lastCoordinates
    initView()
    distributedAgent = new DistributedMapControllerAgent(this, distributedMapController)
    distributedAgent.start()
    audio.loop()
    //distributedMapController.connectedPlayers.get(trainer.id).isFighting_=(false)
    setFocusableOn()
  }

  override protected def doTerminate(): Unit = {
    if(distributedAgent != null) distributedAgent.terminate()
    audio.stop()
  }

  override protected def doMove(direction: Direction): Unit = {
    if (!isInPause) {
      val nextPosition = nextTrainerPosition(direction)
      val tile = gameMap.map(nextPosition.x)(nextPosition.y)
      tile match {
        case tile:Building
          if nextPosition.equals(CoordinateImpl(tile.topLeftCoordinate.x + tile.doorCoordinates.x, tile.topLeftCoordinate.y + tile.doorCoordinates.y)) =>
          enterInBuilding(tile)
        case _ if tile.walkable =>
          walk(direction, nextPosition)
          distributedMapController.sendTrainerPosition(nextPosition)
          if(tile.isInstanceOf[TallGrass]) randomPokemonAppearance()
        case _ => trainerIsMoving = false
      }
    }
  }

  override protected def doInteract(direction: Direction): Unit = {
    if (!isInPause){
      var nextPosition: Coordinate = nextTrainerPosition(direction)
      distributedMapController.connectedPlayers.values() forEach (otherPlayer =>
        if((nextPosition equals otherPlayer.position) /*&&  !otherPlayer.isFighting*/){
          //var plyer2: Player = Player(trainer.id, trainer.name, 2, CoordinateImpl(25,25), true, false)
          /*val player: Player = distributedMapController.connectedPlayers.get(trainer.id)
          player.isFighting_=(true)*/
          distributedMapController.challengeTrainer(otherPlayer.userId, true, true)
          currentDialogue = new ClassicDialoguePanel(this, util.Arrays.asList("Waiting for an answer from " + otherPlayer.username))
          showDialogue(currentDialogue)
        }/*else if((nextPosition equals otherPlayer.position) &&  otherPlayer.isFighting){
          showDialogue(new ClassicDialoguePanel(this, util.Arrays.asList("the plaer is dai hai capito")))
        }*/)
    }
  }

  private def enterInBuilding(building: Building): Unit = {
    distributedMapController.sendTrainerInBuilding(false)
    pause()
    var buildingController: BuildingController = null
    building match{
      case _: PokemonCenter =>
        buildingController = new PokemonCenterController(this.view, this, trainer)
      case _: Laboratory =>
        buildingController = new LaboratoryController(this.view, this, trainer)
    }
    buildingController.start()
    trainerIsMoving = false
  }

  private def randomPokemonAppearance(): Unit = {
    val random: Int = Random.nextInt(RANDOM_MAX_VALUE)
    if(random >= MIN_VALUE_TO_FIND_POKEMON) {
      semaphore.acquire()
      pause()
      semaphore.release()
      new BattleControllerImpl(this: GameController, view: View)
    }
  }

  override protected def doLogout(): Unit = {
    distributedMapController.playerLogout()
    terminate()
  }

  override def createDistributedBattle(otherPlayerId: Int, yourPlayerIsFirst: Boolean): Unit = {
    semaphore.acquire()
    pause()
    semaphore.release()
    val otherPlayerUsername = connectedUsers.get(otherPlayerId).username
    val distributedBattle: BattleController = new DistributedBattleController(this, view, otherPlayerUsername,yourPlayerIsFirst)
    val battleManager: BattleClientManager = new BattleClientManagerImpl(connection,trainer.id,otherPlayerId,distributedBattle)
    distributedBattle.passManager(battleManager)
  }

  override def hideCurrentDialogue(): Unit = {
    currentDialogue.setVisible(false)
  }
}
