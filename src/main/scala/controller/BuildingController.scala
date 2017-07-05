package controller

import javax.swing.SwingUtilities

import model.environment.Direction.Direction
import model.environment._
import model.map.Box
import utilities.Settings
import view.{BuildingPanel, GamePanel, View}

abstract class BuildingController(private var view: View) extends GameController(view){

  private var agent: GameControllerAgent = _
  protected var buildingMap: BuildingMap
  protected var audio: Audio = _

  this.setTrainerSpriteBack()

  override protected def doStart(): Unit = {
    agent = new GameControllerAgent
    audio.loop()
    try {
      agent.start()
    } catch {
      case e: IllegalStateException => view.showError(e.toString, "Not initialized")
    }
  }

  override protected def doTerminate(): Unit = agent.terminate()

  override protected def doPause(): Unit = agent.terminate()

  override protected def doResume(): Unit = {
    agent = new GameControllerAgent
    agent.start()
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
}

class PokemonCenterController(private var view: View) extends BuildingController(view){
  override protected var buildingMap: BuildingMap = new PokemonCenterMap
  this.trainerPosition = CoordinateImpl(buildingMap.entryCoordinate.x, buildingMap.entryCoordinate.y)
  override var gamePanel: GamePanel = new BuildingPanel(this, buildingMap)

  this.audio = Audio(Settings.POKEMONCENTER_SONG)

  override protected def doMove(direction: Direction): Unit = {
    if (!isInPause) {
      val nextPosition = nextTrainerPosition(direction)
      val tile = buildingMap.map(nextPosition.x)(nextPosition.y)
      tile match {
        case tile:Box =>{}
        //if nextPosition.equals(CoordinateImpl(tile.topLeftCoordinate.x + tile.doorCoordinates.x, tile.topLeftCoordinate.y + tile.doorCoordinates.y)) =>

        case _ if tile.walkable =>
          walk(direction, nextPosition)
        case _ => trainerIsMoving = false
      }
    }
  }
}

class LaboratoryController(private var view: View) extends BuildingController(view){
  override protected var buildingMap: BuildingMap = new LaboratoryMap
  this.trainerPosition = CoordinateImpl(buildingMap.entryCoordinate.x, buildingMap.entryCoordinate.y)
  override var gamePanel: GamePanel = new BuildingPanel(this, buildingMap)

  this.audio = Audio(Settings.LABORATORY_SONG)

  override protected def doMove(direction: Direction): Unit = {
    if (!isInPause) {
      val nextPosition = nextTrainerPosition(direction)
      val tile = buildingMap.map(nextPosition.x)(nextPosition.y)
      tile match {
        /*case tile:Building
          if nextPosition.equals(CoordinateImpl(tile.topLeftCoordinate.x + tile.doorCoordinates.x, tile.topLeftCoordinate.y + tile.doorCoordinates.y)) =>
        */
        case _ if tile.walkable =>
          walk(direction, nextPosition)
        case _ => trainerIsMoving = false
      }
    }
  }
}

