package view;

import controller.GameController;

import javax.swing.*;
import java.awt.*;

public class GameMenuPanel extends JPanel {


    public GameMenuPanel(GameController gameController){
        //gameController.gamePanel().setFocusable(false);
        setLayout(new GridLayout(0,1));
        final JButton[] menuButtons = new JButton[6];
        menuButtons[0] = new JButton("Pokédex");
        menuButtons[1] = new JButton("Squadra");
        menuButtons[2] = new JButton("Pokémon Catturati");
        menuButtons[3] = new JButton("Allenatore");
        menuButtons[4] = new JButton("Tasti");
        menuButtons[5] = new JButton("Exit");
        menuButtons[0].addActionListener(e ->{
            gameController.showPokedex();
        });
        menuButtons[1].addActionListener(e ->{});
        menuButtons[2].addActionListener(e ->{});
        menuButtons[3].addActionListener(e ->{});
        menuButtons[4].addActionListener(e ->{});
        menuButtons[5].addActionListener(e ->{
            this.setVisible(false);
            //gameController.gamePanel().setFocusable(true);
        });
        for (JButton menuButton : menuButtons) {
            menuButton.setBackground(Color.WHITE);
            add(menuButton);
        }
    }
}
