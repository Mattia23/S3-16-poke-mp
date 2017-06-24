package app

import controller.{Controller, ControllerImpl}
import model.game.{Model, ModelImpl}
import view.{View, ViewImpl}

object Main extends App {
  val controller: Controller = ControllerImpl.getControllerInstance
  val model: Model = new ModelImpl
  val view: View = new ViewImpl(controller)

  controller.setModel_=(model)
  controller.setView_=(view)
}
