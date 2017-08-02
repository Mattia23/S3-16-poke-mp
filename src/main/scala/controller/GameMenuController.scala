package controller

import model.entities.PokemonWithLife
import model.environment.Audio
import utilities.Settings
import view.View

/**
  * GameMenuController manages all the possible action that the user can do using the lateral game menu.
  */
trait GameMenuController{
  /**
    * Show PokedexPanel
    */
  def showPokedex(): Unit

  /**
    * Show trainer's Team Panel
    */
  def showTeam(): Unit

  /**
    * Show trainer's Panel with his statistic
    */
  def showTrainer(): Unit

  /**
    * Show Ranking panel
    */
  def showRanking(): Unit

  /**
    * Show keyboard explanation Panel
    */
  def showKeyboardExplanation(): Unit

  /**
    * Do the logout from the game and show the initial menu
    */
  def doLogout(): Unit

  /**
    * Exit from the game menu
    */
  def doExit(): Unit

  /**
    * Show the game menu
    */
  def showGameMenu(): Unit

  /**
    * Show a panel with the specific Pokemon infos
    * @param pokemonWithLife Pokemon to show
    */
  def showPokemonInTeamPanel(pokemonWithLife: PokemonWithLife): Unit
}

/**
  * @inheritdoc
  * @param view instance of the view
  * @param gameController instance of the game controller
  */
class GameMenuControllerImpl(private var view: View, private val gameController: GameController) extends GameMenuController{

  private val audio: Audio = Audio(Settings.Audio.MENU_SONG)
  audio.loop()
  showGameMenu()

  /**
    * @inheritdoc
    */
  override def showPokedex(): Unit = view.showPokedex(this, gameController)

  /**
    * @inheritdoc
    */
  override def showTeam(): Unit = view.showTeamPanel(this, gameController)

  /**
    * @inheritdoc
    */
  override def showTrainer(): Unit = view.showTrainerPanel(this, gameController)

  /**
    * @inheritdoc
    */
  override def showRanking(): Unit = view.showRankingPanel(this, gameController)

  /**
    * @inheritdoc
    */
  override def showKeyboardExplanation(): Unit = view.showKeyboardPanel(this, gameController)

  /**
    * @inheritdoc
    */
  override def doLogout(): Unit = {
    audio.stop()
    gameController.logout()
    view.showInitialMenu(InitialMenuController(view))
  }

  /**
    * @inheritdoc
    */
  override def doExit(): Unit = {
    audio.stop()
    gameController.resume()
  }

  /**
    * @inheritdoc
    */
  override def showGameMenu(): Unit = {
    gameController.sendTrainerIsBusy(true)
    view.showGameMenuPanel(this)
  }

  /**
    * @inheritdoc
    * @param pokemonWithLife Pokemon to show
    */
  override def showPokemonInTeamPanel(pokemonWithLife: PokemonWithLife): Unit =
    view.showPokemonInTeamPanel(pokemonWithLife, this)
}
