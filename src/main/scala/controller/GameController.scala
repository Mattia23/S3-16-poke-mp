package controller

import javax.swing.SwingUtilities

import model.environment.{Coordinate, CoordinateImpl}
import model.environment.Direction
import model.environment.Direction.Direction
import model.game.Model
import model.map.{InitialTownElements, MapCreator, Tile}
import utilities.Settings
import view.{BuildingPanel, GamePanel, View}

trait GameViewObserver {
  def startGame: Unit

  def pauseButton: Unit

  def resumeGame: Unit

  def moveTrainer(direction: Direction.Direction): Unit

  def gamePanel: GamePanel

  def gamePanel_=(gamePanel: GamePanel): Unit

  def buildingPanel: BuildingPanel

  def buildingPanel_=(buildingPanel: BuildingPanel): Unit

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

  override var trainerPosition: Coordinate = CoordinateImpl(13, 25)

  override var gamePanel: GamePanel = new GamePanel(this, gameMap)

  override var buildingPanel: BuildingPanel = new BuildingPanel(this)
  var inBuilding: Boolean = false

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
        var actualX: Double = trainerPosition.x
        var actualY: Double = trainerPosition.y
        for(_ <- 1 to 4) {
          direction match {
            case Direction.UP =>
              if(actualX == 13 && actualY == 25){
                this.view.showGame(buildingPanel)
                this.trainerPosition = CoordinateImpl(actualX toInt, actualY toInt)
              }
              else if(actualX == 44 && actualY == 25){
                this.view.showGame(buildingPanel)
                this.trainerPosition = CoordinateImpl(actualX toInt, actualY toInt)
              }
              actualY = actualY - (Settings.TILE_HEIGHT.asInstanceOf[Double] / 4)
              this.gamePanel.updateCurrentY(actualY)
            case Direction.DOWN =>
              actualY = actualY + (Settings.TILE_HEIGHT.asInstanceOf[Double] / 4)
              this.gamePanel.updateCurrentY(actualY)
            case Direction.RIGHT =>
              actualX = actualX + (Settings.TILE_WIDTH.asInstanceOf[Double] / 4)
              this.gamePanel.updateCurrentX(actualX)
            case Direction.LEFT =>
              actualX = actualX - (Settings.TILE_WIDTH.asInstanceOf[Double] / 4)
              this.gamePanel.updateCurrentX(actualX)
          }
          Thread.sleep(Settings.GAME_REFRESH_TIME)
        }
        println(trainerPosition)
        this.updateTrainerPosition(direction)
        this.trainerIsMoving = false
      }).start()
    }
  }

  private def inBuilding(variable: Boolean): Unit ={
    inBuilding = variable
  }

  private def updateTrainerPosition(direction: Direction): Unit = direction match {
    case Direction.UP => this.trainerPosition = CoordinateImpl(trainerPosition.x, trainerPosition.y - 1)
    case Direction.DOWN => this.trainerPosition = CoordinateImpl(trainerPosition.x, trainerPosition.y + 1)
    case Direction.RIGHT => this.trainerPosition = CoordinateImpl(trainerPosition.x + 1, trainerPosition.y)
    case Direction.LEFT => this.trainerPosition = CoordinateImpl(trainerPosition.x - 1, trainerPosition.y)
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
          Thread.sleep(Settings.GAME_REFRESH_TIME)
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
