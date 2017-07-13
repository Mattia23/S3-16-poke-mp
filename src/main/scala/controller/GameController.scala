package controller

import javax.swing.SwingUtilities

import model.entities._
import model.environment.Direction.Direction
import model.environment.{Audio, Coordinate, CoordinateImpl, Direction}
import utilities.Settings
import view.{GamePanel, View}

trait GameController {
  def trainer: Trainer

  def trainerIsMoving: Boolean

  def trainerIsMoving_=(isMoving: Boolean): Unit

  def isInGame: Boolean

  def isInPause: Boolean

  def trainerSprite: String

  def start(): Unit

  def terminate(): Unit

  def pause(): Unit

  def resume(): Unit

  def moveTrainer(direction: Direction.Direction): Unit

  def trainerInteract(direction: Direction.Direction): Unit
}

abstract class GameControllerImpl(private var view: View, override val trainer: Trainer) extends GameController{
  private final val TRAINER_STEPS = 4

  private var agent: GameControllerAgent = _
  private var _trainerSprite: Sprite = _
  private var fistStep: Boolean = true
  protected var inGame = false
  protected var inPause = false
  protected var audio: Audio = _
  protected var gamePanel: GamePanel = _

  override var trainerIsMoving: Boolean = false

  override def isInGame: Boolean = this.inGame

  override def isInPause: Boolean = this.inPause

  override def trainerSprite: String = _trainerSprite.image

  override final def start(): Unit = {
    inGame = true
    agent = new GameControllerAgent
    agent.start()
    doStart()
  }

  protected def doStart(): Unit

  override final def terminate(): Unit = {
    inGame = false
    agent.terminate()
    doTerminate()
  }

  protected def doTerminate(): Unit

  override final def pause(): Unit = {
    inPause = true
    agent.terminate()
    doPause()
  }

  protected def doPause(): Unit

  override final def resume(): Unit = {
    inPause = false
    agent = new GameControllerAgent
    agent.start()
    doResume()
  }

  protected def doResume(): Unit

  override final def moveTrainer(direction: Direction): Unit = doMove(direction)

  protected def doMove(direction: Direction): Unit

  override final def trainerInteract(direction: Direction): Unit = doInteract(direction)

  protected def doInteract(direction: Direction) : Unit

  protected def nextTrainerPosition(direction: Direction): Coordinate = direction match {
    case Direction.UP =>
      _trainerSprite = trainer.sprites.backS
      CoordinateImpl(trainer.coordinate.x, trainer.coordinate.y - 1)
    case Direction.DOWN =>
      _trainerSprite = trainer.sprites.frontS
      CoordinateImpl(trainer.coordinate.x, trainer.coordinate.y + 1)
    case Direction.RIGHT =>
      _trainerSprite = trainer.sprites.rightS
      CoordinateImpl(trainer.coordinate.x + 1, trainer.coordinate.y)
    case Direction.LEFT =>
      _trainerSprite = trainer.sprites.leftS
      CoordinateImpl(trainer.coordinate.x - 1, trainer.coordinate.y)
  }

  protected def walk(direction: Direction, nextPosition: Coordinate) : Unit = {
    new Thread(() => {
      var actualX: Double = trainer.coordinate.x
      var actualY: Double = trainer.coordinate.y
      for (_ <- 1 to TRAINER_STEPS) {
        direction match {
          case Direction.UP =>
            actualY = actualY - (Settings.TILE_HEIGHT.asInstanceOf[Double] / TRAINER_STEPS)
            gamePanel.updateCurrentY(actualY)
          case Direction.DOWN =>
            actualY = actualY + (Settings.TILE_HEIGHT.asInstanceOf[Double] / TRAINER_STEPS)
            gamePanel.updateCurrentY(actualY)
          case Direction.RIGHT =>
            actualX = actualX + (Settings.TILE_WIDTH.asInstanceOf[Double] / TRAINER_STEPS)
            gamePanel.updateCurrentX(actualX)
          case Direction.LEFT =>
            actualX = actualX - (Settings.TILE_WIDTH.asInstanceOf[Double] / TRAINER_STEPS)
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

  private def updateTrainerPosition(coordinate: Coordinate): Unit = trainer.coordinate = CoordinateImpl(coordinate.x, coordinate.y)

  protected def setTrainerSpriteFront(): Unit = _trainerSprite = trainer.sprites.frontS

  protected def setTrainerSpriteBack(): Unit = _trainerSprite = trainer.sprites.backS

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
