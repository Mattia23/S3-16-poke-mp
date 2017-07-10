package view;

import controller.BattleController;
import controller.Controller;
import model.entities.PokemonWithLife;

import javax.swing.*;

public interface View {
    void showMenu();

    void showLogin();

    void showSignIn();

    void showPanel(JPanel panel);

    void showDialogue(JPanel dialoguePanel);

    void showBattle(PokemonWithLife myPokemon, PokemonWithLife otherPokemon, BattleController battleController);

    BattleView getBattlePanel();

    void showPokemonChoice();

    void showPause();

    void showError(final String error, final String title);

}
