package controller

import model.environment.{Audio, AudioImpl}
import utilities.Settings
import view.View

trait InitialMenuController {
  def view: View

  def show(): Unit

  def processEvent(event: String): Unit

  def stopMainMusic(): Unit
}

class InitialMenuControllerImpl(override val view: View) extends InitialMenuController{

  private val audio: Audio = new AudioImpl(Settings.MAIN_SONG)
  audio.loop()

  show()

  override def show(): Unit = view showInitialMenu this

  override def processEvent(event: String): Unit = {
    new Thread(() => {
      event match {
        case Settings.LOGIN_BUTTON => new LoginControllerImpl(this, view)
        case Settings.SIGN_IN_BUTTON => new SignInControllerImpl(this, view)
        case Settings.QUIT_BUTTON => {
          stopMainMusic()
          System exit 0
        }
        case _ =>
      }
    }).start()
  }

  override def stopMainMusic(): Unit = audio.stop()
}
