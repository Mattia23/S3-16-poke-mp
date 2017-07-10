package view;

import controller.BattleController;
import controller.Controller;
import controller.GameController;
import model.entities.PokemonWithLife;

import javax.swing.*;

public interface View {

    void setController(Controller controller);

    void showMenu();

    void showLogin();

    void showSignIn();

    void showPanel(JPanel gamePanel);

    void showDialogue(JPanel dialoguePanel);

    void showGameMenuPanel(JPanel gameMenuPanel);

    void showBattle(PokemonWithLife myPokemon, PokemonWithLife otherPokemon, BattleController battleController);

    BattleView getBattlePanel();

    void showPokemonChoice(BattleController battleController);

    void showPause();

    void showError(final String error, final String title);

}
