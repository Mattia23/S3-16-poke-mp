package view;

import controller.GameViewObserver;

import javax.swing.*;
import java.awt.*;

public class GameMenuPanel extends JPanel {


    public GameMenuPanel(GameViewObserver gameController){
        gameController.gamePanel().setFocusable(false);
        setLayout(new GridLayout(0,1));
        final JButton[] menuButtons = new JButton[5];
        menuButtons[0] = new JButton("Pokédex");
        menuButtons[1] = new JButton("Squadra");
        menuButtons[2] = new JButton("Allenatore");
        menuButtons[3] = new JButton("Tasti");
        menuButtons[4] = new JButton("Exit");
        menuButtons[0].addActionListener(e ->{
            gameController.showPokedex();
        });
        menuButtons[1].addActionListener(e ->{
            gameController.showTeam();
        });
        menuButtons[2].addActionListener(e ->{

        });
        menuButtons[3].addActionListener(e ->{
            gameController.showKeyboardExplanation();
        });
        menuButtons[4].addActionListener(e ->{
            this.setVisible(false);
            gameController.gamePanel().setFocusable(true);
        });
        for (JButton menuButton : menuButtons) {
            menuButton.setBackground(Color.WHITE);
            add(menuButton);
        }
    }
}
