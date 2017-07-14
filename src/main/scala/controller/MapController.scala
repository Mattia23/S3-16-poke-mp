package controller

import database.remote.DBConnect
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
  private final val POKEMON_CENTER_BUILDING = "Pokemon center"
  private final val LABORATORY_BUILDING = "Laboratory"

  private val gameMap = MapCreator.create(Settings.MAP_HEIGHT, Settings.MAP_WIDTH, InitialTownElements())
  private var lastCoordinates: Coordinate = _
  audio = Audio(Settings.MAP_SONG)

  override protected def doStart(): Unit = {
    initView()
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
    if(this.trainer.capturedPokemons.isEmpty)
      this.view.showDialogue(new ClassicDialoguePanel(this, Settings.INITIAL_DIALOGUE))
  }

  override protected def doPause(): Unit = {
    lastCoordinates = trainer.coordinate
    audio.stop()
  }

  override protected def doResume(): Unit = {
    if(trainer.getFirstAvailableFavouritePokemon <= 0) {
      DBConnect.rechangeAllTrainerPokemon(trainer.id)
      updateLastCoordinateToBuilding(POKEMON_CENTER_BUILDING)
    }
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
