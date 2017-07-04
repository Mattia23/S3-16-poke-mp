package controller

import model.entities._
import model.environment.{Coordinate, CoordinateImpl}
import model.environment.Direction
import model.environment.Direction.Direction
import utilities.Settings
import view.{GamePanel, View}

trait GameViewObserver {
  def trainerPosition: Coordinate

  def trainerPosition_=(position: Coordinate): Unit

  def trainerIsMoving: Boolean

  def trainerIsMoving_=(isMoving: Boolean): Unit

  def gamePanel: GamePanel

  def gamePanel_=(gamePanel: GamePanel): Unit

  def startGame(): Unit

  def pauseGame(): Unit

  def resumeGame(): Unit

  def terminateGame(): Unit

  def isInGame: Boolean

  def isInPause: Boolean

  def moveTrainer(direction: Direction.Direction): Unit

  def trainerSprite: String
  //def speakTrainer: Unit
}

abstract class GameController(private var view: View) extends GameViewObserver{
  private final val TRAINER_STEPS = 4

  protected var inGame = false
  protected var inPause = false
  protected val trainer: Trainer = new TrainerImpl("Ash", 1, 0)
  private var _trainerSprite: Sprite = trainer.sprites.frontS
  private var fistStep: Boolean = true

  override var trainerPosition: Coordinate = trainer.coordinate

  override var trainerIsMoving: Boolean = false

  override def isInGame: Boolean = this.inGame

  override def isInPause: Boolean = this.inPause

  override def trainerSprite: String = _trainerSprite.image

  protected def nextTrainerPosition(direction: Direction): Coordinate = direction match {
    case Direction.UP => CoordinateImpl(trainerPosition.x, trainerPosition.y - 1)
    case Direction.DOWN => CoordinateImpl(trainerPosition.x, trainerPosition.y + 1)
    case Direction.RIGHT => CoordinateImpl(trainerPosition.x + 1, trainerPosition.y)
    case Direction.LEFT => CoordinateImpl(trainerPosition.x - 1, trainerPosition.y)
  }

  protected def walk(direction: Direction, nextPosition: Coordinate) : Unit = {
    new Thread(() => {
      var actualX: Double = trainerPosition.x
      var actualY: Double = trainerPosition.y
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

  override def moveTrainer(direction: Direction): Unit

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

}
