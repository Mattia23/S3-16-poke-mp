package controller

import java.awt.event.{ActionEvent, ActionListener, KeyEvent, KeyListener}

import model.environment.Direction

class GameKeyListener(val gameController: GameViewObserver) extends KeyListener with ActionListener {
  private var direction: Direction.Direction = _
  private var isMoving: Boolean = false
  private var lastPressed: Int = _

  private val MOVE_RIGHT: Int = KeyEvent.VK_RIGHT
  private val MOVE_LEFT: Int = KeyEvent.VK_LEFT
  private val MOVE_UP: Int = KeyEvent.VK_UP
  private val MOVE_DOWN: Int = KeyEvent.VK_DOWN
  private val PAUSE_BUTTON: Int = KeyEvent.VK_ESCAPE


  override def keyTyped(e: KeyEvent): Unit = {
    catchButton(e.getKeyCode)
  }

  override def keyPressed(e: KeyEvent): Unit = {
    val key: Int = e.getKeyCode
    if ((key == MOVE_LEFT) || (key == MOVE_RIGHT) || (key == MOVE_UP) || (key == MOVE_DOWN)) {
      this.isMoving = true
      this.lastPressed = key
    }
    catchButton(key)
  }

  override def keyReleased(e: KeyEvent): Unit = {
    val key = e.getKeyCode
    if ((key == MOVE_LEFT || key == MOVE_RIGHT || key == MOVE_UP || key == MOVE_DOWN) && key == lastPressed){
      this.isMoving = false
    }
  }

  override def actionPerformed(e: ActionEvent): Unit = {
    if (this.isMoving) {
      this.gameController.moveTrainer(this.direction)
    }
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
}
