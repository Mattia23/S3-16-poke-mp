package controller

import model.entities._
import model.environment.{Coordinate, CoordinateImpl, Direction}
import model.environment.Direction.Direction
import utilities.Settings
import view.{GameMenuPanel, GamePanel, View}

trait GameViewObserver {

  def gamePanel: GamePanel

  def gamePanel_=(gamePanel: GamePanel): Unit

  def trainerPosition: Coordinate

  def trainerPosition_=(position: Coordinate): Unit

  def trainerIsMoving: Boolean

  def trainerIsMoving_=(isMoving: Boolean): Unit

  def isInGame: Boolean

  def isInPause: Boolean

  def trainerSprite: String

  def startGame(): Unit

  def terminateGame(): Unit

  def pauseGame(): Unit

  def resumeGame(): Unit

  def resumeGameAtPokemonCenter(): Unit

  def moveTrainer(direction: Direction.Direction): Unit

  def trainerInteract(direction: Direction.Direction): Unit

  def showMenu(): Unit

  def showPokedex(): Unit = {}
}

abstract class GameController(private var view: View) extends GameViewObserver{
  private final val TRAINER_STEPS = 4

  protected var inGame = false
  protected var inPause = false
  protected val trainer: Trainer = new TrainerImpl("Ash", 1, 0)
  private var _trainerSprite: Sprite = _
  private var fistStep: Boolean = true

  override var trainerPosition: Coordinate = trainer.coordinate

  override var trainerIsMoving: Boolean = false

  override def isInGame: Boolean = this.inGame

  override def isInPause: Boolean = this.inPause

  override def trainerSprite: String = _trainerSprite.image

  override final def startGame(): Unit = {
    inGame = true
    doStart()
    view.showPanel(gamePanel)
  }

  protected def doStart(): Unit

  override final def terminateGame(): Unit = {
    inGame = false
    doTerminate()
  }

  protected def doTerminate(): Unit

  override final def pauseGame(): Unit = {
    inPause = true
    doPause()
    view.showPause()
  }

  protected def doPause(): Unit

  override final def resumeGame(): Unit = {
    inPause = false
    doResume()
    view.showPanel(gamePanel)
  }

  override def resumeGameAtPokemonCenter(): Unit = {
    //SPOSTA L'ALLENATORE DAVANTI AL CENTRO POKEMON
    resumeGame()
  }

  protected def doResume(): Unit

  override final def moveTrainer(direction: Direction): Unit = doMove(direction)

  protected def doMove(direction: Direction): Unit

  override final def trainerInteract(direction: Direction): Unit = doInteract(direction)

  protected def doInteract(direction: Direction) : Unit

  override def showMenu(): Unit = view.showGameMenuPanel(this)
  override def showPokedex(): Unit = view.showPokedex(trainer,this)

  protected def nextTrainerPosition(direction: Direction): Coordinate = direction match {
    case Direction.UP =>
      _trainerSprite = trainer.sprites.backS
      CoordinateImpl(trainerPosition.x, trainerPosition.y - 1)
    case Direction.DOWN =>
      _trainerSprite = trainer.sprites.frontS
      CoordinateImpl(trainerPosition.x, trainerPosition.y + 1)
    case Direction.RIGHT =>
      _trainerSprite = trainer.sprites.rightS
      CoordinateImpl(trainerPosition.x + 1, trainerPosition.y)
    case Direction.LEFT =>
      _trainerSprite = trainer.sprites.leftS
      CoordinateImpl(trainerPosition.x - 1, trainerPosition.y)
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

  protected def setTrainerSpriteFront(): Unit = _trainerSprite = trainer.sprites.frontS

  protected def setTrainerSpriteBack(): Unit = _trainerSprite = trainer.sprites.backS

}
