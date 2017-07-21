package controller

import model.environment.{Audio, AudioImpl}
import utilities.Settings
import view.View

trait GameMenuController{
  def showPokedex(): Unit

  def showTeam(): Unit

  def showTrainer(): Unit

  def showRanking(): Unit

  def showKeyboardExplanation(): Unit

  def doLogout(): Unit

  def doExit(): Unit
}

class GameMenuControllerImpl(private var view: View, private val gameController: GameController) extends GameMenuController{

  override def showPokedex(): Unit = view.showPokedex(gameController)

  override def showTeam(): Unit = view.showTeamPanel(gameController)

  override def showTrainer(): Unit = view.showTrainerPanel(gameController)

  override def showRanking(): Unit = view.showRankingPanel(gameController)

  override def showKeyboardExplanation(): Unit = view.showKeyboardPanel(gameController)

  override def doLogout(): Unit = {
    gameController.terminate()
    view.showInitialMenu(new InitialMenuControllerImpl(view))
  }

  override def doExit(): Unit = {
    gameController.resume()
  }
}
