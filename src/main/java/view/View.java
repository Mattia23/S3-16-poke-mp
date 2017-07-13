package view;

import controller.*;
import model.entities.Pokemon;
import model.entities.PokemonWithLife;
import model.environment.BuildingMap;
import model.map.GameMap;
import model.entities.Trainer;

import javax.swing.*;

public interface View {
    void setController(Controller controller);

    void showInitialMenu(InitialMenuController initialMenuController);

    void showLogin(LoginController loginController);

    void showSignIn(SignInController signInController);

    void showMap(GameController mapController, GameMap gameMap);

    //GamePanel getMapPanel();

    void showPokemonCenter(GameController pokemonCenterController, BuildingMap buildingMap);

    //GamePanel getPokemonCenterPanel();

    void showLaboratory(GameController laboratoryController, BuildingMap buildingMap, boolean emptyCaptures);

    //GamePanel getLaboratoryPanel();

    GamePanel getGamePanel();

    void showBoxPanel(BuildingController buildingController);

    void showInitialPokemonPanel(BuildingController buildingController, Pokemon pokemon);

    void showDialogue(JPanel dialoguePanel);

    void showGameMenuPanel(GameController controller);

    void showBattle(PokemonWithLife myPokemon, PokemonWithLife otherPokemon, BattleController battleController);

    BattleView getBattlePanel();

    void showPokemonChoice(BattleController battleController, Trainer trainer);

    void showPokedex(Trainer trainer, GameController gameController);

    void showPause();

    void showMessage(final String error, final String title, final int messageType);

}
