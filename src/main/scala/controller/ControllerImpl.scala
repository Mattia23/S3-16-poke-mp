package controller

import java.util.Optional

import model.game.Model
import view.View

trait Controller{
  def newGame: Unit

  def model_=(model: Model): Unit

  def model: Model

  def view_=(view: View): Unit

  def view: View

  def getGameController: Optional[GameViewObserver]

  def quit: Unit

}

object ControllerImpl {
  private var controllerInstance: ControllerImpl = null

  def getControllerInstance: Controller = {
    if (controllerInstance == null) {
      controllerInstance = new ControllerImpl
    }
    controllerInstance
  }
}

class ControllerImpl extends Controller {
  override var model: Model = _
  override var view: View = _
  private var gameController: GameViewObserver = _

  private def checkInitializzation: Unit = {
    if (this.model == null || this.view == null) {
      throw new IllegalStateException
    }
  }

  override def newGame: Unit = {
    this.checkInitializzation

    if (this.gameController == null) {
      this.gameController = new GameController(this.model, this.view)
    }

    this.gameController.startGame
  }

  override def getGameController: Optional[GameViewObserver] = Optional.ofNullable(this.gameController)
  override def quit: Unit = System.exit(0)

}

