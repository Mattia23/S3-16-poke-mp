package controller

import java.util.concurrent.Semaphore
import javax.swing.SwingUtilities

import database.remote.DBConnect
import model.entities._
import model.environment.Direction.Direction
import model.environment.{Audio, Coordinate, CoordinateImpl, Direction}
import model.map.{MainTrainerMovement, Movement}
import utilities.Settings
import view._

trait GameController {
  def trainer: Trainer

  def trainerIsMoving: Boolean

  def trainerIsMoving_=(isMoving: Boolean): Unit

  def isInGame: Boolean

  def isInPause: Boolean

  def start(): Unit

  def terminate(): Unit

  def pause(): Unit

  def resume(): Unit

  def moveTrainer(direction: Direction.Direction): Unit

  def trainerInteract(direction: Direction.Direction): Unit

  def showGameMenu(): Unit

  def logout(): Unit
}

abstract class GameControllerImpl(private var view: View, override val trainer: Trainer) extends GameController {
  private var agent: GameControllerAgent = _
  //private var fistStep: Boolean = true
  protected var inGame = false
  protected var inPause = false
  protected var audio: Audio = _
  protected var gamePanel: GamePanel = _
  protected val semaphore: Semaphore = new Semaphore(1)
  protected var trainerMovement: Movement = _

  override var trainerIsMoving: Boolean = false

  override def isInGame: Boolean = this.inGame

  override def isInPause: Boolean = this.inPause

  override final def start(): Unit = {
    inGame = true
    doStart()
    agent = new GameControllerAgent
    agent.start()
  }

  protected def doStart(): Unit

  override final def terminate(): Unit = {
    inGame = false
    doTerminate()
    if (agent != null) agent.terminate()
  }

  protected def doTerminate(): Unit

  override final def pause(): Unit = {
    inPause = true
    doPause()
    if (agent != null) agent.terminate()
  }

  protected def doPause(): Unit

  override final def resume(): Unit = {
    inPause = false
    doResume()
    agent = new GameControllerAgent
    agent.start()
  }

  protected def doResume(): Unit

  override final def moveTrainer(direction: Direction): Unit = doMove(direction)

  protected def doMove(direction: Direction): Unit

  override final def trainerInteract(direction: Direction): Unit = doInteract(direction)

  protected def doInteract(direction: Direction): Unit

  override def showGameMenu(): Unit = new GameMenuControllerImpl(view, this)

  override def logout(): Unit = {
    DBConnect.closeConnection()
    doLogout()
  }

  protected def doLogout(): Unit

  protected def nextTrainerPosition(direction: Direction): Coordinate = direction match {
    case Direction.UP =>
      trainer.currentSprite = trainer.sprites.backS
      CoordinateImpl(trainer.coordinate.x, trainer.coordinate.y - 1)
    case Direction.DOWN =>
      trainer.currentSprite = trainer.sprites.frontS
      CoordinateImpl(trainer.coordinate.x, trainer.coordinate.y + 1)
    case Direction.RIGHT =>
      trainer.currentSprite = trainer.sprites.rightS
      CoordinateImpl(trainer.coordinate.x + 1, trainer.coordinate.y)
    case Direction.LEFT =>
      trainer.currentSprite = trainer.sprites.leftS
      CoordinateImpl(trainer.coordinate.x - 1, trainer.coordinate.y)
  }

  protected def walk(direction: Direction, nextPosition: Coordinate): Unit = {
    new Thread(() => {
      semaphore.acquire()
      trainerMovement.walk(trainer.coordinate, direction, nextPosition)
     /* var actualX: Double = trainer.coordinate.x
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
      */
      trainerIsMoving = false
      semaphore.release()
    }).start()
  }

  protected def setTrainerSpriteFront(): Unit = trainer.currentSprite = trainer.sprites.frontS

  protected def setTrainerSpriteBack(): Unit = trainer.currentSprite = trainer.sprites.backS

  private class GameControllerAgent extends Thread {
    private var stopped: Boolean = false

    override def run(): Unit = {
      while (isInGame && !stopped) {
        if (!isInPause) {
          try
            SwingUtilities.invokeAndWait(() => gamePanel.repaint())
          catch {
            case e: Exception => println(e)
          }
        }

        try
          Thread.sleep(Settings.GAME_REFRESH_TIME)
        catch {
          case e: InterruptedException => println(e)
        }
      }
    }

    def terminate(): Unit = stopped = true

  }

}
