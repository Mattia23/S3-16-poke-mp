package controller

import utilities.Settings
import view.{InitialMenuPanel, LoginPanel, View}

trait InitialMenuController {
  def view: View

  def show(): Unit

  def processEvent(event: String): Unit
}

class InitialMenuControllerImpl(override val view: View) extends InitialMenuController{

  private val initialMenuPanel: InitialMenuPanel = new InitialMenuPanel(this)
  show()

  override def show(): Unit = view showPanel initialMenuPanel

  override def processEvent(event: String): Unit = {
    new Thread(() => {
      event match {
        case Settings.LOGIN_BUTTON => new LoginControllerImpl(this, view)
        case Settings.SIGN_IN_BUTTON => new SignInControllerImpl(this, view)
        case Settings.QUIT_BUTTON => System exit 0
        case _ =>
      }
    }).start()
  }
}
