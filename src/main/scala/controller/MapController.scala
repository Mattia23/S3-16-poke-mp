package controller

import java.util.Optional
import javax.swing.SwingUtilities

import model.entities.{Owner, PokemonFactory}
import model.environment.{Audio, CoordinateImpl}
import model.environment.Direction.Direction
import model.map._
import utilities.Settings
import view._

import scala.util.Random

class MapController(private var view: View) extends GameController(view){
  private final val RANDOM_MAX_VALUE = 10
  private final val MIN_VALUE_TO_FIND_POKEMON = 8

  private var agent: GameControllerAgent = _
  private val gameMap = MapCreator.create(Settings.MAP_HEIGHT, Settings.MAP_WIDTH, InitialTownElements())
  private val audio = Audio(Settings.MAP_SONG)

  this.setTrainerSpriteFront()

  override var gamePanel: GamePanel = new MapPanel(this, gameMap)

  override def doStart(): Unit = {
    agent = new GameControllerAgent
    audio.loop()
    try {
      agent.start()
    } catch {
      case e: IllegalStateException => view.showError(e.toString, "Not initialized")
    }
  }

  override def doPause(): Unit = {
    setTrainerSpriteFront()
    audio.stop()
    agent.terminate()
  }

  override def doResume(): Unit = {
    agent = new GameControllerAgent
    agent.start()
    audio.loop()
  }

  override def doTerminate(): Unit = {
    audio.stop()
    agent.terminate()
  }

  override def doMove(direction: Direction): Unit = {
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

  private def enterInBuilding(building: Building): Unit = {
    this.pauseGame()
    var buildingPanel: BuildingPanel = null
    var buildingController: BuildingController = null
    building match{
      case _: PokemonCenter =>
        buildingController = new PokemonCenterController(this.view, this)
        buildingPanel = new PokemonCenterPanel(buildingController)
      case _: Laboratory =>
        buildingController = new LaboratoryController(this.view, this)
        buildingPanel = new LaboratoryPanel(buildingController)
    }
    buildingController.startGame()
    trainerIsMoving = false
  }

  private def randomPokemonAppearance(): Unit = {
    val random: Int = Random.nextInt(RANDOM_MAX_VALUE)
    if(random >= MIN_VALUE_TO_FIND_POKEMON) {
      val pokemon = PokemonFactory.createPokemon(Owner.WILD, Optional.empty(), Optional.of(trainer.level))
      if(pokemon.isPresent)
        println("Trovato il Pokemon: "+ pokemon.get().pokemon.name+" al livello: "+pokemon.get().pokemon.level)
    }
  }

  private class GameControllerAgent extends Thread {
    var stopped: Boolean = false

    override def run(): Unit = {
      while(isInGame && !stopped){
        if(!isInPause){
          try
            SwingUtilities.invokeAndWait(() => gamePanel.repaint())
          catch {
            case e: Exception => System.out.println(e)
          }
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

  override protected def doInteract(direction: Direction): Unit = ???
}
