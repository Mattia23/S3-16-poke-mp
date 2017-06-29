package controller

import java.awt.event.{KeyEvent, KeyListener}

import model.environment.Direction

class GameKeyListener(val gameController: GameViewObserver) extends KeyListener{
  private var direction: Direction.Direction = _
  private var lastPressed: Int = _

  private val MOVE_RIGHT: Int = KeyEvent.VK_RIGHT
  private val MOVE_LEFT: Int = KeyEvent.VK_LEFT
  private val MOVE_UP: Int = KeyEvent.VK_UP
  private val MOVE_DOWN: Int = KeyEvent.VK_DOWN
  private val PAUSE_BUTTON: Int = KeyEvent.VK_ESCAPE

  override def keyPressed(e: KeyEvent): Unit = e.getKeyCode match {
    case MOVE_DOWN | MOVE_LEFT | MOVE_RIGHT | MOVE_UP =>
      this.gameController.trainerIsMoving = true
      this.lastPressed = e.getKeyCode
      catchButton(e.getKeyCode)
      this.gameController.moveTrainer(this.direction)
    case _ =>
  }

  override def keyReleased(e: KeyEvent): Unit = e.getKeyCode match {
    case (MOVE_DOWN | MOVE_LEFT | MOVE_RIGHT | MOVE_UP) if e.getKeyCode == lastPressed => this.gameController.trainerIsMoving = false
    case _ =>
  }

  def catchButton(button: Int): Unit = {
    button match {
      case MOVE_LEFT => direction = Direction.LEFT
      case MOVE_RIGHT => direction = Direction.RIGHT
      case MOVE_UP => direction = Direction.UP
      case MOVE_DOWN => direction = Direction.DOWN
      case PAUSE_BUTTON => gameController.pauseButton
    }
  }

  override def keyTyped(e: KeyEvent): Unit = {}
}
