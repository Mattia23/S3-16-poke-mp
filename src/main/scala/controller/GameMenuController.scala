package controller

import model.entities.PokemonWithLife
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

  def showGameMenu(): Unit

  def showPokemonInTeamPanel(pokemonWithLife: PokemonWithLife): Unit
}

class GameMenuControllerImpl(private var view: View, private val gameController: GameController) extends GameMenuController{

  private val audio: Audio = new AudioImpl(Settings.MENU_SONG)
  audio.loop()
  showGameMenu()

  override def showPokedex(): Unit = view.showPokedex(this, gameController)

  override def showTeam(): Unit = view.showTeamPanel(this, gameController)

  override def showTrainer(): Unit = view.showTrainerPanel(this, gameController)

  override def showRanking(): Unit = view.showRankingPanel(this, gameController)

  override def showKeyboardExplanation(): Unit = view.showKeyboardPanel(this, gameController)

  override def doLogout(): Unit = {
    gameController.logout()
    view.showInitialMenu(new InitialMenuControllerImpl(view))
  }

  override def doExit(): Unit = {
    audio.stop()
    gameController.resume()
  }

  override def showGameMenu(): Unit = {
    view.showGameMenuPanel(this)
  }

  override def showPokemonInTeamPanel(pokemonWithLife: PokemonWithLife): Unit =
    view.showPokemonInTeamPanel(pokemonWithLife, this)
}
