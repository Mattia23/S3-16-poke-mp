package app

import controller.{InitialMenuController, InitialMenuControllerImpl}
import view.{View, ViewImpl}

object Main extends App {
  val view: View = new ViewImpl()
  val controller: InitialMenuController = new InitialMenuControllerImpl(view)
}
