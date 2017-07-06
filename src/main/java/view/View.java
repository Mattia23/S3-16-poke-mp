package view;

import controller.Controller;

import javax.swing.*;

public interface View {

    void setController(Controller controller);

    void showMenu();

    void showLogin();

    void showSignIn();

    void showPanel(JPanel gamePanel);

    void showPause();

    void showError(final String error, final String title);

}
