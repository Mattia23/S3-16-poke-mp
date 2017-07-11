package controller

import model.entities.Trainer
import model.environment.{Audio, Coordinate, CoordinateImpl}
import model.environment.Direction.Direction
import model.map._
import utilities.Settings
import view._

import scala.util.Random

class MapController(private val view: View, private val _trainer: Trainer) extends GameControllerImpl(view, _trainer){
  private final val RANDOM_MAX_VALUE = 10
  private final val MIN_VALUE_TO_FIND_POKEMON = 8

  private val gameMap = MapCreator.create(Settings.MAP_HEIGHT, Settings.MAP_WIDTH, InitialTownElements())
  private var lastCoordinates: Coordinate = _
  audio = Audio(Settings.MAP_SONG)

  override protected def doStart(): Unit = {
    initView()
    audio.loop()
  }

  override protected def doPause(): Unit = {
    lastCoordinates = trainer.coordinate
    audio.stop()
  }

  override protected def doResume(): Unit = {
    trainer.coordinate = lastCoordinates
    initView()
    audio.loop()
  }

  override protected def doTerminate(): Unit = {
    audio.stop()
  }

  private def initView(): Unit = {
    setTrainerSpriteFront()
    view.showMap(this, gameMap)
    gamePanel = view.getMapPanel
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
      new BattleControllerImpl(trainer,view)
    }
  }

}
