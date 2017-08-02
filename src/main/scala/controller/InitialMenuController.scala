package controller

import model.environment.Audio
import utilities.Settings
import view.View

/**
  * InitialMenuController manages the interaction with the initial game menu.
  */
trait InitialMenuController {
  /**
    * Shows InitialMenuPanel
    */
  def show(): Unit

  /**
    * Manages possible actions that the user can do in the menu
    * @param event event to manage
    */
  def processEvent(event: String): Unit

  /**
    * Stops tha initial game menu music
    */
  def stopMainMusic(): Unit
}

object InitialMenuController {
  def apply(view: View): InitialMenuController = new InitialMenuControllerImpl(view)
}

/**
  * @inheritdoc
  * @param view instance of the view
  */
class InitialMenuControllerImpl(private val view: View) extends InitialMenuController{

  private val audio: Audio = Audio(Settings.Audio.MAIN_SONG)
  audio.loop()

  show()

  /**
    * @inheritdoc
    */
  override def show(): Unit = view showInitialMenu this

  /**
    * @inheritdoc
    */
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

  /**
    * @inheritdoc
    */
  override def stopMainMusic(): Unit = audio.stop()
}
