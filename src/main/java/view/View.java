package view;

import controller.Controller;
import model.game.Model;

public interface View {

    void setController(Controller controller);

    void drawModel(Model model);

    void showMenu();

    void showLogin();

    void showSignIn();

    void showGame();

    void showPause();

    void showError(final String error, final String title);

    void showPokemonCenterMap();
}
