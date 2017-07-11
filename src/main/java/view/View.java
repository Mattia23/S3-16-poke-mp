package view;

import controller.*;
import model.entities.Pokemon;
import model.entities.PokemonWithLife;
import model.environment.BuildingMap;
import model.map.GameMap;

import javax.swing.*;

public interface View {
    void setController(Controller controller);

    void showInitialMenu(InitialMenuController initialMenuController);

    void showLogin(LoginController loginController);

    void showSignIn(SignInController signInController);

    void showMap(GameController mapController, GameMap gameMap);

    GamePanel getMapPanel();

    void showPokemonCenter(GameController pokemonCenterController, BuildingMap buildingMap);

    GamePanel getPokemonCenterPanel();

    void showLaboratory(GameController laboratoryController, BuildingMap buildingMap, boolean emptyCaptures);

    GamePanel getLaboratoryPanel();

    void showBoxPanel(BuildingController buildingController);

    void showInitialPokemonPanel(BuildingController buildingController, Pokemon pokemon);

   // void showPanel(JPanel panel);

    void showDialogue(JPanel dialoguePanel);

    void showBattle(PokemonWithLife myPokemon, PokemonWithLife otherPokemon, BattleController battleController);

    BattleView getBattlePanel();

    void showPokemonChoice();

    void showPause();

    void showMessage(final String error, final String title, final int messageType);

}
