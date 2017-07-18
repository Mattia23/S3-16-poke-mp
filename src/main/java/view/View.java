package view;

import controller.*;
import model.entities.Pokemon;
import model.entities.PokemonWithLife;
import model.environment.BuildingMap;
import model.map.GameMap;
import model.entities.Trainer;

import javax.swing.*;

public interface View {
    void showInitialMenu(InitialMenuController initialMenuController);

    void showLogin(LoginController loginController);

    void showSignIn(SignInController signInController);

    void showMap(GameController mapController, GameMap gameMap);

    void showPokemonCenter(GameController pokemonCenterController, BuildingMap buildingMap);

    void showLaboratory(GameController laboratoryController, BuildingMap buildingMap, boolean emptyCaptures);

    GamePanel getGamePanel();

    void showBoxPanel(BuildingController buildingController);

    void showInitialPokemonPanel(BuildingController buildingController, PokemonWithLife pokemon);

    void showDialogue(JPanel dialoguePanel);

    void showGameMenuPanel(GameController controller);

    void showBattle(PokemonWithLife myPokemon, PokemonWithLife otherPokemon, BattleController battleController);

    BattleView getBattlePanel();

    void showPokemonChoice(BattleController battleController, Trainer trainer);

    void showPokedex(Trainer trainer, GameController gameController);

    void showTeamPanel(Trainer trainer, GameController gameController);

    void showPokemonInTeamPanel(PokemonWithLife pokemonWithLife, GameController gameController);

    void showTrainerPanel(Trainer trainer, GameController gameController);

    void showRankingPanel(Trainer trainer, GameController gameController);

    void showKeyboardPanel(GameController gameController);

    void showPause();

    void showMessage(final String error, final String title, final int messageType);

}
