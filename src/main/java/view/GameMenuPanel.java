package view;

import controller.GameController;

import javax.swing.*;
import java.awt.*;

public class GameMenuPanel extends JPanel {
    private static final String JOPTIONPANE_TITLE = "Logout";
    private static final String JOPTIONPANE_MESSAGE = "Do you really want to log out?";

    public GameMenuPanel(GameController gameController){
        setLayout(new GridLayout(0,1));
        final JButton[] menuButtons = new JButton[7];
        menuButtons[0] = new JButton("Pokédex");
        menuButtons[1] = new JButton("Team");
        menuButtons[2] = new JButton("Trainer");
        menuButtons[3] = new JButton("Ranking");
        menuButtons[4] = new JButton("Keyboard");
        menuButtons[5] = new JButton("Logout");
        menuButtons[6] = new JButton("Exit");
        menuButtons[0].addActionListener(e -> gameController.showPokedex());
        menuButtons[1].addActionListener(e -> gameController.showTeam());
        menuButtons[2].addActionListener(e -> gameController.showTrainer());
        menuButtons[3].addActionListener(e -> gameController.showRanking());
        menuButtons[4].addActionListener(e -> gameController.showKeyboardExplanation());
        menuButtons[5].addActionListener(e ->{
            int reply = JOptionPane.showConfirmDialog(null, JOPTIONPANE_MESSAGE, JOPTIONPANE_TITLE, JOptionPane.YES_NO_OPTION);
            if (reply == JOptionPane.YES_OPTION) {
                gameController.terminate();
                gameController.doLogout();
            }
            else {
                gameController.resume();
            }
        });
        menuButtons[6].addActionListener(e ->{
            gameController.resume();
            this.setVisible(false);
        });
        for (JButton menuButton : menuButtons) {
            menuButton.setBackground(Color.WHITE);
            add(menuButton);
        }
    }
}
