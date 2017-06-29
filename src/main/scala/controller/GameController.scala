package controller

import javax.swing.SwingUtilities

import model.environment.{Coordinate, CoordinateImpl}
import model.environment.Direction
import model.environment.Direction.Direction
import model.game.Model
import model.map.{InitialTownElements, MapCreator}
import utilities.Settings
import view.{GamePanel, View}

trait GameViewObserver {
  def startGame: Unit

  def pauseButton: Unit

  def resumeGame: Unit

  def moveTrainer(direction: Direction.Direction): Unit

  def gamePanel: GamePanel

  def gamePanel_=(gamePanel: GamePanel): Unit

  def trainerPosition: Coordinate

  def trainerPosition_=(position: Coordinate): Unit

  def trainerIsMoving: Boolean

  def trainerIsMoving_=(isMoving: Boolean): Unit
  //def speakTrainer: Unit
  //def ...
}

class GameController(private var model: Model, private var view: View) extends GameViewObserver{
  private var agent: GameControllerAgent = _
  private val gameMap = MapCreator.create(Settings.MAP_HEIGHT, Settings.MAP_WIDTH, InitialTownElements())

  override var trainerPosition: Coordinate = CoordinateImpl(Settings.MAP_WIDTH / 2, Settings.MAP_HEIGHT / 2)

  override var gamePanel: GamePanel = new GamePanel(this, gameMap)

  override var trainerIsMoving: Boolean = false

  override def startGame: Unit = {
    this.agent = new GameControllerAgent

    try {
      this.model.startGame
      this.view.showGame(gamePanel)
      this.agent.start
    } catch {
      case e: IllegalStateException =>
        this.view.showError(e.toString, "Not initialized")
    }
  }

  override def pauseButton: Unit = {
    this.agent.terminate
    this.model.pause
    this.view.showPause
  }

  override def resumeGame: Unit = {
    this.view.showGame(gamePanel)
    this.agent = new GameControllerAgent
    this.model.resumeGame
    this.agent.start
  }

  override def moveTrainer(direction: Direction): Unit = {
    if (!this.model.isInPause) {
       new Thread(() => {
          for(i <- 1 to 35) {
            direction match {
              case Direction.UP => trainerPosition = CoordinateImpl(trainerPosition.x, trainerPosition.y - 1)
              case Direction.DOWN => trainerPosition = CoordinateImpl(trainerPosition.x, trainerPosition.y + 1)
              case Direction.RIGHT => trainerPosition = CoordinateImpl(trainerPosition.x + 1, trainerPosition.y)
              case Direction.LEFT => trainerPosition = CoordinateImpl(trainerPosition.x - 1, trainerPosition.y)
            }
            gamePanel.updateCurrentPosition(trainerPosition)
            Thread.sleep(7)
          }
          this.trainerIsMoving = false
        }).start()
    }
  }

  private class GameControllerAgent extends Thread {
    var stopped: Boolean = false

    override def run: Unit = {
      while(model.isInGame && !stopped){
        if(!model.isInPause){
          try
            SwingUtilities.invokeAndWait(() => gamePanel.repaint())
          catch {
            case e: Exception => System.out.println(e)
          }
        }

        try
          Thread.sleep(5)
        catch {
          case e: InterruptedException => System.out.println(e)
        }
      }
    }

    def terminate: Unit = {
      stopped = true
    }

  }

}
