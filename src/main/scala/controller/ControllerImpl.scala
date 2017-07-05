package controller

import java.util.Optional

import view.View

trait Controller{
  def newGame(): Unit

  def view_=(view: View): Unit

  def view: View

  def getGameController: Optional[GameViewObserver]

  def quit(): Unit

}

object ControllerImpl {
  private var controllerInstance: ControllerImpl = _


  def getControllerInstance: Controller = {

    if (controllerInstance == null) {
      controllerInstance = new ControllerImpl
    }
    controllerInstance
  }
}

class ControllerImpl extends Controller {
  override var view: View = _

  private var gameController: GameViewObserver = _

  private def checkInitializzation(): Unit = {
    if (this.view == null) {
      throw new IllegalStateException
    }
  }

  override def newGame(): Unit = {
    this.checkInitializzation()

    if (this.gameController == null) {
      this.gameController = new MapController(this.view)
    }

    this.gameController.startGame()
  }

  override def getGameController: Optional[GameViewObserver] = Optional.ofNullable(this.gameController)
  override def quit(): Unit = System.exit(0)

}

