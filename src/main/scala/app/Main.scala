package app

import controller.{Controller, ControllerImpl}
import view.{View, ViewImpl}

object Main extends App {
  val controller: Controller = ControllerImpl.getControllerInstance
  val view: View = new ViewImpl(controller)

  controller.view_=(view)

}
