package view;

import controller.Controller;

import javax.swing.*;
import java.awt.*;


public class MenuPanel extends BasePanel {

    private final static int INSETS = 10;
    private Controller controller;
    private View parentView;
    private JPanel downPanel;
    private JButton newgame;
    private JButton login;
    private JButton signIn;
    private JButton quit;

    public MenuPanel (View view, Controller ctrl) {
        this.controller = ctrl;
        this.parentView = view;
        this.setLayout(new BorderLayout());
        this.downPanel = new JPanel(new GridBagLayout());
        this.newgame = new JButton("gioca (prova)");
        this.login = new JButton("Login");
        this.signIn = new JButton("Sign in");
        this.quit = new JButton("Quit");

        this.newgame.addActionListener(e -> {
            this.controller.newGame();
            this.parentView.showGame();
        });
        this.login.addActionListener(e -> this.parentView.showLogin());
        this.signIn.addActionListener(e -> this.parentView.showSignIn());
        this.quit.addActionListener(e -> {
            this.controller.newGame();
            this.parentView.showPokemonCenterMap();
        });

        k = new GridBagConstraints();
        k.gridy = 0;
        k.insets = new Insets(INSETS, INSETS, INSETS, INSETS);
        k.fill = GridBagConstraints.VERTICAL;
        this.downPanel.add(this.newgame, k);
        k.gridy++;
        this.downPanel.add(this.login, k);
        k.gridy++;
        this.downPanel.add(this.signIn, k);
        k.gridy++;
        this.downPanel.add(this.quit, k);
        this.downPanel.setOpaque(false);
        this.add(downPanel, BorderLayout.SOUTH);
    }

}
