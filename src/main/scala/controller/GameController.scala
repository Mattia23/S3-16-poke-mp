package controller

import javax.swing.SwingUtilities

import model.entities._
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

  def gamePanel: GamePanel

  def gamePanel_=(gamePanel: GamePanel): Unit

  def moveTrainer(direction: Direction.Direction): Unit

  def trainerPosition: Coordinate

  def trainerPosition_=(position: Coordinate): Unit

  def trainerIsMoving: Boolean

  def trainerIsMoving_=(isMoving: Boolean): Unit

  def trainerSprite: String
  //def speakTrainer: Unit
}

class GameController(private var view: View) extends GameViewObserver{
  private var agent: GameControllerAgent = _
  private var inGame = false
  private var inPause = false
  private val gameMap: GameMap = MapCreator.create(Settings.MAP_HEIGHT, Settings.MAP_WIDTH, InitialTownElements())
  private val trainer: Trainer = new TrainerImpl("Ash", 1, 0)
  private var _trainerSprite: Sprite = trainer.sprites.frontS
  private var fistStep: Boolean = true

  override def startGame(): Unit = {
    agent = new GameControllerAgent

    try {
      inGame = true
      view.showGame(gamePanel)
      agent.start()
    } catch {
      case e: IllegalStateException => view.showError(e.toString, "Not initialized")
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

  override var gamePanel: GamePanel = new GamePanel(this, gameMap)

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

  override var trainerPosition: Coordinate = trainer.coordinate

  override var trainerIsMoving: Boolean = false

  override def trainerSprite: String = _trainerSprite.image

  private def nextTrainerPosition(direction: Direction): Coordinate = direction match {
    case Direction.UP => CoordinateImpl(trainerPosition.x, trainerPosition.y - 1)
    case Direction.DOWN => CoordinateImpl(trainerPosition.x, trainerPosition.y + 1)
    case Direction.RIGHT => CoordinateImpl(trainerPosition.x + 1, trainerPosition.y)
    case Direction.LEFT => CoordinateImpl(trainerPosition.x - 1, trainerPosition.y)
  }

  private def nextTile(coordinate: Coordinate): Tile = gameMap.map(coordinate.x)(coordinate.y)

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

  private def updateTrainerSprite(direction: Direction): Unit = {
    if (trainerIsMoving) {
      direction match {
        case Direction.UP => _trainerSprite match {
          case BackS(_) =>
            if (fistStep) {
              _trainerSprite = trainer.sprites.back1
              fistStep = false
            } else {
              _trainerSprite = trainer.sprites.back2
              fistStep = true
            }
          case Back1(_) | Back2(_) => _trainerSprite = trainer.sprites.backS
          case _ => _trainerSprite = trainer.sprites.back1
        }
        case Direction.DOWN => _trainerSprite match {
          case FrontS(_) =>
            if (fistStep) {
              _trainerSprite = trainer.sprites.front1
              fistStep = false
            } else {
              _trainerSprite = trainer.sprites.front2
              fistStep = true
            }
          case Front1(_) | Front2(_) => _trainerSprite = trainer.sprites.frontS
          case _ => _trainerSprite = trainer.sprites.front1
        }
        case Direction.LEFT => _trainerSprite match {
          case LeftS(_) =>
            if (fistStep) {
              _trainerSprite = trainer.sprites.left1
              fistStep = false
            } else {
              _trainerSprite = trainer.sprites.left2
              fistStep = true
            }
          case Left1(_) | Left2(_) => _trainerSprite = trainer.sprites.leftS
          case _ => _trainerSprite = trainer.sprites.left1
        }
        case Direction.RIGHT => _trainerSprite match {
          case RightS(_) =>
            if (fistStep) {
              _trainerSprite = trainer.sprites.right1
              fistStep = false
            } else {
              _trainerSprite = trainer.sprites.right2
              fistStep = true
            }
          case Right1(_) | Right2(_) => _trainerSprite = trainer.sprites.rightS
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
  
  private def updateTrainerPosition(coordinate: Coordinate): Unit = trainerPosition = CoordinateImpl(coordinate.x, coordinate.y)

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
