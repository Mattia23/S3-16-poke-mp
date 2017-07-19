package controller

import java.util.concurrent.{ConcurrentHashMap, ConcurrentMap}

import database.remote.DBConnect
import distributed.User
import distributed.client.{PlayerPositionClientManager, PlayerPositionClientManagerImpl}
import model.entities.{Trainer, TrainerSprites}
import model.environment.{Audio, Coordinate, CoordinateImpl}
import model.environment.Direction.Direction
import model.map._
import utilities.Settings
import view._

import scala.util.Random

object MapController {
  def apply(view: View, _trainer: Trainer, connectedUsers: ConcurrentMap[Int, User]): GameController = new MapController(view, _trainer, connectedUsers)

  private final val RANDOM_MAX_VALUE = 10
  private final val MIN_VALUE_TO_FIND_POKEMON = 8
  private final val POKEMON_CENTER_BUILDING = "Pokemon center"
  private final val LABORATORY_BUILDING = "Laboratory"
}

class MapController(private val view: View, private val _trainer: Trainer, private val connectedUsers: ConcurrentMap[Int, User]) extends GameControllerImpl(view, _trainer){
  import MapController._

  private val gameMap = MapCreator.create(Settings.MAP_HEIGHT, Settings.MAP_WIDTH, InitialTownElements())
  private var lastCoordinates: Coordinate = _
  private val distributedMapController: DistributedMapController = DistributedMapControllerImpl(connectedUsers)
  private var distributedAgent: DistributedMapControllerAgent = _
  private val playerPositionManager: PlayerPositionClientManager = PlayerPositionClientManagerImpl()
  audio = Audio(Settings.MAP_SONG)


  override protected def doStart(): Unit = {
    initView()
    distributedAgent = new DistributedMapControllerAgent(this, distributedMapController)
    distributedAgent.start
    if(trainer.capturedPokemons.isEmpty){
      doFirstLogin()
    }else {
      audio.loop()
    }
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
          case (POKEMON_CENTER_BUILDING, tile: PokemonCenter) =>
            lastCoordinates = CoordinateImpl(tile.topLeftCoordinate.x + tile.doorCoordinates.x, tile.topLeftCoordinate.y + tile.doorCoordinates.y + 1)
          case _ =>
        }
      }
    }
  }

  override protected def doPause(): Unit = {
    if(distributedAgent != null) distributedAgent.terminate
    lastCoordinates = trainer.coordinate
    audio.stop()
    this.gamePanel.setFocusable(false)
  }

  override protected def doResume(): Unit = {
    if(trainer.getFirstAvailableFavouritePokemon <= 0) {
      DBConnect.rechangeAllTrainerPokemon(trainer.id)
      updateLastCoordinateToBuilding(POKEMON_CENTER_BUILDING)
    }
    trainer.coordinate = lastCoordinates
    initView()
    distributedAgent = new DistributedMapControllerAgent(this, distributedMapController)
    distributedAgent.start
    audio.loop()
    this.gamePanel.setFocusable(true)
  }

  override protected def doTerminate(): Unit = {
    if(distributedAgent != null) distributedAgent.terminate
    audio.stop()
  }

  private def initView(): Unit = {
    setTrainerSpriteFront()
    view.showMap(this, distributedMapController, gameMap)
    gamePanel = view.getGamePanel
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
          playerPositionManager.sendPlayerPosition(trainer.id, nextPosition)
          if(tile.isInstanceOf[TallGrass]) randomPokemonAppearance()
        case _ => trainerIsMoving = false
      }
    }
  }

  override protected def doInteract(direction: Direction): Unit = ???

  private def enterInBuilding(building: Building): Unit = {
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
      pause()
      new BattleControllerImpl(this: GameController, trainer: Trainer, view: View)
    }
  }

}
