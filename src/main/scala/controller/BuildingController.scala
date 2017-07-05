package controller

import javax.swing.SwingUtilities

import model.environment.{Audio, BuildingMap, CoordinateImpl}
import model.environment.Direction.Direction
import utilities.Settings
import view.{BuildingPanel, GamePanel, View}

class BuildingController(private var view: View) extends GameController(view){

  private var agent: GameControllerAgent = _
  private val buildingMap: BuildingMap = null
  private val audio = Audio(Settings.MAP_SONG)

  override var gamePanel: GamePanel = new BuildingPanel(this, buildingMap)

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

class LaboratoryController(private var view: View) extends BuildingController(view){

}

class PokemonCenterController(private var view: View) extends BuildingController(view){

}


