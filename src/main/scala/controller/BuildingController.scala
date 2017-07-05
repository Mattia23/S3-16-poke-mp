package controller

import javax.swing.SwingUtilities

import model.environment.{Audio, BuildingMap}
import model.environment.Direction.Direction
import utilities.Settings
import view.{BuildingPanel, GamePanel, View}

class BuildingController(private var view: View) extends GameController(view){

  private var agent: GameControllerAgent = _
  private val buildingMap: BuildingMap = ???
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

  override protected def doTerminate(): Unit = ???

  override protected def doPause(): Unit = ???

  override protected def doResume(): Unit = ???

  override protected def doMove(direction: Direction): Unit = ???

  override def gamePanel: GamePanel = ???

  override def gamePanel_=(gamePanel: GamePanel): Unit = ???

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


