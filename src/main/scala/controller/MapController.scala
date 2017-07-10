package controller

import model.entities.Trainer
import model.environment.{Audio, CoordinateImpl}
import model.environment.Direction.Direction
import model.map._
import utilities.Settings
import view._

import scala.util.Random

class MapController(private var view: View) extends GameControllerImpl(view){
  private final val RANDOM_MAX_VALUE = 10
  private final val MIN_VALUE_TO_FIND_POKEMON = 8

  private val gameMap = MapCreator.create(Settings.MAP_HEIGHT, Settings.MAP_WIDTH, InitialTownElements())
  audio = Audio(Settings.MAP_SONG)

  this.setTrainerSpriteFront()

  override var gamePanel: GamePanel = new MapPanel(this, gameMap)

  override protected def doStart(): Unit = {
    audio.loop()
  }

  override protected def doPause(): Unit = {
    setTrainerSpriteFront()
    audio.stop()
  }

  override protected def doResume(): Unit = {
    audio.loop()
  }

  override protected def doTerminate(): Unit = {
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
          if(tile.isInstanceOf[TallGrass]) randomPokemonAppearance()
        case _ => trainerIsMoving = false
      }
    }
  }

  override protected def doInteract(direction: Direction): Unit = ???

  private def enterInBuilding(building: Building): Unit = {
    this.pauseGame()
    var buildingController: BuildingController = null
    building match{
      case _: PokemonCenter =>
        buildingController = new PokemonCenterController(this.view, this)
      case _: Laboratory =>
        buildingController = new LaboratoryController(this.view, this)
    }
    buildingController.startGame()
    trainerIsMoving = false
  }

  private def randomPokemonAppearance(): Unit = {
    val random: Int = Random.nextInt(RANDOM_MAX_VALUE)
    if(random >= MIN_VALUE_TO_FIND_POKEMON) {
      new BattleControllerImpl(trainer: Trainer,view: View)
    }
  }

}
