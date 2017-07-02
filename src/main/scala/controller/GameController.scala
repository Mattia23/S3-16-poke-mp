package controller

import javax.swing.SwingUtilities

import model.entities._
import model.environment.{Coordinate, CoordinateImpl}
import model.environment.Direction
import model.environment.Direction.Direction
import model.game.Model
import model.map._
import utilities.Settings
import view.{GamePanel, View}

trait GameViewObserver {
  def startGame(): Unit

  def pauseButton(): Unit

  def resumeGame(): Unit

  def moveTrainer(direction: Direction.Direction): Unit

  def gamePanel: GamePanel

  def gamePanel_=(gamePanel: GamePanel): Unit

  def trainerPosition: Coordinate

  def trainerPosition_=(position: Coordinate): Unit

  def trainerIsMoving: Boolean

  def trainerIsMoving_=(isMoving: Boolean): Unit

  def trainerSprite: String
  //def speakTrainer: Unit
  //def ...
}

class GameController(private var model: Model, private var view: View) extends GameViewObserver{
  private var agent: GameControllerAgent = _
  private val gameMap: GameMap = MapCreator.create(Settings.MAP_HEIGHT, Settings.MAP_WIDTH, InitialTownElements())
  private val trainer: Trainer = new TrainerImpl("Ash", 1, 0)
  private var _trainerSprite: Sprite = trainer.sprites.frontS

  override var trainerPosition: Coordinate = trainer.coordinate

  override var gamePanel: GamePanel = new GamePanel(this, gameMap)

  override var trainerIsMoving: Boolean = false

  override def startGame(): Unit = {
    agent = new GameControllerAgent

    try {
      model.startGame
      view.showGame(gamePanel)
      agent.start()
    } catch {
      case e: IllegalStateException =>
        view.showError(e.toString, "Not initialized")
    }
  }

  override def pauseButton(): Unit = {
    agent.terminate()
    model.pause
    view.showPause()
  }

  override def resumeGame(): Unit = {
    view.showGame(gamePanel)
    agent = new GameControllerAgent
    model.resumeGame
    agent.start()
  }

  override def moveTrainer(direction: Direction): Unit = {
    if (!model.isInPause) {
      val nextPosition = nextTrainerPosition(direction)
      println("x:"+nextPosition.x+" y:"+nextPosition.y)
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

  override def trainerSprite: String = _trainerSprite.image

  private def updateTrainerSprite(direction: Direction): Unit = {
    if (trainerIsMoving) {
      direction match {
        case Direction.UP => _trainerSprite match {
          case Back1(_) => _trainerSprite = trainer.sprites.back2
          case _ => _trainerSprite = trainer.sprites.back1
        }
        case Direction.DOWN => _trainerSprite match {
          case Front1(_) => _trainerSprite = trainer.sprites.front2
          case _ => _trainerSprite = trainer.sprites.front1
        }
        case Direction.LEFT => _trainerSprite match {
          case Left1(_) => _trainerSprite = trainer.sprites.left2
          case _ => _trainerSprite = trainer.sprites.left1
        }
        case Direction.RIGHT => _trainerSprite match {
          case Right1(_) => _trainerSprite = trainer.sprites.right2
          case _ => _trainerSprite = trainer.sprites.right1
        }
      }
    } else {
      direction match {
        case Direction.UP => _trainerSprite = trainer.sprites.backS
        case Direction.DOWN => _trainerSprite = trainer.sprites.frontS
        case Direction.LEFT => _trainerSprite = trainer.sprites.leftS
        case Direction.RIGHT => _trainerSprite = trainer.sprites.rightS
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
        updateTrainerSprite(direction)
        Thread.sleep(Settings.GAME_REFRESH_TIME)
      }
      updateTrainerPosition(nextPosition)
      trainerIsMoving = false
      updateTrainerSprite(direction)
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

    def terminate(): Unit = {
      stopped = true
    }

  }
}
