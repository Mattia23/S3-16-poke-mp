package controller

import model.environment.Audio
import utilities.Settings
import view.View

trait InitialMenuController {
  def view: View

  def show(): Unit

  def processEvent(event: String): Unit

  def stopMainMusic(): Unit
}

object InitialMenuController {
  def apply(view: View): InitialMenuController = new InitialMenuControllerImpl(view)
}

class InitialMenuControllerImpl(override val view: View) extends InitialMenuController{

  private val audio: Audio = Audio(Settings.Audio.MAIN_SONG)
  audio.loop()

  show()

  override def show(): Unit = view showInitialMenu this

  override def processEvent(event: String): Unit = {
    new Thread(() => {
      event match {
        case Settings.Strings.LOGIN_BUTTON => LoginController(this, view)
        case Settings.Strings.SIGN_IN_BUTTON => SignInController(this, view)
        case Settings.Strings.QUIT_BUTTON =>
          stopMainMusic()
          System exit 0
        case _ =>
      }
    }).start()
  }

  override def stopMainMusic(): Unit = audio.stop()
}
