package controller

import java.awt.event.{KeyEvent, KeyListener}

import model.environment.Direction

object GameKeyListener{
  private final val MOVE_RIGHT: Int = KeyEvent.VK_RIGHT
  private final val MOVE_LEFT: Int = KeyEvent.VK_LEFT
  private final val MOVE_UP: Int = KeyEvent.VK_UP
  private final val MOVE_DOWN: Int = KeyEvent.VK_DOWN
  private final val PAUSE_BUTTON: Int = KeyEvent.VK_ESCAPE

  def apply(gameController: GameController): KeyListener = new GameKeyListener(gameController)
}

class GameKeyListener(private val gameController: GameController) extends KeyListener{
  import GameKeyListener._

  private var direction: Direction.Direction = _
  private var lastPressed: Int = _

  override def keyPressed(e: KeyEvent): Unit = {
    if(!gameController.trainerIsMoving) {
      e.getKeyCode match {
        case MOVE_DOWN | MOVE_LEFT | MOVE_RIGHT | MOVE_UP =>
          gameController.trainerIsMoving = true
          lastPressed = e.getKeyCode
          catchButton(e.getKeyCode)
          gameController moveTrainer direction
        case KeyEvent.VK_SPACE => gameController trainerInteract direction
        case KeyEvent.VK_ESCAPE =>
          gameController.pause()
          gameController.showGameMenu()
        case _ =>
      }
    }
  }

  override def keyReleased(e: KeyEvent): Unit = {}

  private def catchButton(button: Int) = {
    button match {
      case MOVE_LEFT => direction = Direction.LEFT
      case MOVE_RIGHT => direction = Direction.RIGHT
      case MOVE_UP => direction = Direction.UP
      case MOVE_DOWN => direction = Direction.DOWN
      case PAUSE_BUTTON => gameController.pause()
    }
  }

  override def keyTyped(e: KeyEvent): Unit = {}
}
