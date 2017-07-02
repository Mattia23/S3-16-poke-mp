package controller

import javax.swing.SwingUtilities

import model.environment.{Coordinate, CoordinateImpl}
import model.environment.Direction
import model.environment.Direction.Direction
import model.map._
import utilities.Settings
import view.{GamePanel, View}

trait GameViewObserver {
  def startGame(): Unit

  def pauseGame(): Unit

  def resumeGame(): Unit

  def terminateGame(): Unit

  def isInGame: Boolean

  def isInPause: Boolean

  def moveTrainer(direction: Direction.Direction): Unit

  def gamePanel: GamePanel

  def gamePanel_=(gamePanel: GamePanel): Unit

  def trainerPosition: Coordinate

  def trainerPosition_=(position: Coordinate): Unit

  def trainerIsMoving: Boolean

  def trainerIsMoving_=(isMoving: Boolean): Unit
  //def speakTrainer: Unit
}

class GameController(private var view: View) extends GameViewObserver{
  private var agent: GameControllerAgent = _
  private val gameMap = MapCreator.create(Settings.MAP_HEIGHT, Settings.MAP_WIDTH, InitialTownElements())
  private var inGame = false
  private var inPause = false

  override var trainerPosition: Coordinate = CoordinateImpl(Settings.MAP_WIDTH / 2, Settings.MAP_HEIGHT / 2)

  override var gamePanel: GamePanel = new GamePanel(this, gameMap)

  override var trainerIsMoving: Boolean = false

  override def startGame(): Unit = {
    agent = new GameControllerAgent

    try {
      inGame = true
      view.showGame(gamePanel)
      agent.start()
    } catch {
      case e: IllegalStateException =>
        view.showError(e.toString, "Not initialized")
    }
  }

  override def pauseGame(): Unit = {
    inPause = true
    agent.terminate()
    view.showPause()
  }

  override def resumeGame(): Unit = {
    inPause = false
    view.showGame(gamePanel)
    agent = new GameControllerAgent
    agent.start()
  }

  override def terminateGame(): Unit = {
    inGame = false
    agent.terminate()
  }

  override def isInGame: Boolean = this.inGame
  override def isInPause: Boolean = this.inPause

  override def moveTrainer(direction: Direction): Unit = {
    if (!isInPause) {
      val nextPosition = nextTrainerPosition(direction)
      val tile = nextTile(nextPosition)
      tile match {
        case tile:Building
          if nextPosition.equals(CoordinateImpl(tile.topLeftCoordinate.x + tile.doorCoordinates.x, tile.topLeftCoordinate.y + tile.doorCoordinates.y)) =>
            enterInBuilding(tile)
        case _ if tile.walkable => walk(direction, nextPosition)
        case _ => trainerIsMoving = false
      }
    }
  }

  private def enterInBuilding(building: Building): Unit = {
    println("Entro dentro "+ building.toString)
    trainerIsMoving = false
  }

  private def walk(direction: Direction, nextPosition: Coordinate) : Unit = {
    new Thread(() => {
      var actualX: Double = trainerPosition.x
      var actualY: Double = trainerPosition.y
      for (_ <- 1 to 4) {
        direction match {
          case Direction.UP =>
            actualY = actualY - (Settings.TILE_HEIGHT.asInstanceOf[Double] / 4)
            gamePanel.updateCurrentY(actualY)
          case Direction.DOWN =>
            actualY = actualY + (Settings.TILE_HEIGHT.asInstanceOf[Double] / 4)
            gamePanel.updateCurrentY(actualY)
          case Direction.RIGHT =>
            actualX = actualX + (Settings.TILE_WIDTH.asInstanceOf[Double] / 4)
            gamePanel.updateCurrentX(actualX)
          case Direction.LEFT =>
            actualX = actualX - (Settings.TILE_WIDTH.asInstanceOf[Double] / 4)
            gamePanel.updateCurrentX(actualX)
        }
        Thread.sleep(Settings.GAME_REFRESH_TIME)
      }
      updateTrainerPosition(nextPosition)
      trainerIsMoving = false
    }).start()
  }

  private def nextTrainerPosition(direction: Direction): Coordinate = direction match {
    case Direction.UP => CoordinateImpl(trainerPosition.x, trainerPosition.y - 1)
    case Direction.DOWN => CoordinateImpl(trainerPosition.x, trainerPosition.y + 1)
    case Direction.RIGHT => CoordinateImpl(trainerPosition.x + 1, trainerPosition.y)
    case Direction.LEFT => CoordinateImpl(trainerPosition.x - 1, trainerPosition.y)
  }

  private def updateTrainerPosition(coordinate: Coordinate): Unit = trainerPosition = CoordinateImpl(coordinate.x, coordinate.y)

  private def nextTile(coordinate: Coordinate): Tile = gameMap.map(coordinate.x)(coordinate.y)



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
