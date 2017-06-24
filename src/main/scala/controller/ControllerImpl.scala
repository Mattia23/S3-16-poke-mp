package controller

import java.util.Optional

import model.game.Model
import view.View

trait Controller{
  def newGame: Unit

  def setModel_=(model: Model): Unit

  def getModelInstance: Model

  def setView_=(view: View): Unit

  def getViewInstance: View

  def getGameController: Optional[GameViewObserver]

  def quit: Unit

}

object ControllerImpl {
  private var controllerInstance: ControllerImpl = null

  def getControllerInstance: Controller = synchronized {
    if (controllerInstance == null) {
      controllerInstance = new ControllerImpl
    }
    controllerInstance
  }
}

class ControllerImpl private() extends Controller {
  private var model: Model = _
  private var view: View = _
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

  override def getModelInstance: Model = this.model
  override def setModel_=(model: Model): Unit = this.model = model
  override def getViewInstance: View = this.view
  override def setView_=(view: View):Unit = this.view = view
  override def getGameController: Optional[GameViewObserver] = Optional.ofNullable(this.gameController)
  override def quit: Unit = System.exit(0)

}

