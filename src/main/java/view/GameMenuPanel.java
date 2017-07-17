package view;

import controller.GameController;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GameMenuPanel extends JPanel{
    private static final String JOPTIONPANE_TITLE = "Logout";
    private static final String JOPTIONPANE_MESSAGE = "Do you really want to log out?";
    private ButtonGroup buttonGroup = new ButtonGroup();
    private final JRadioButton[] menuButtons;

    public GameMenuPanel(GameController gameController){
        setLayout(new GridLayout(0,1));
        menuButtons = new JRadioButton[7];
        menuButtons[0] = new JRadioButton("Pokédex");
        menuButtons[1] = new JRadioButton("Team");
        menuButtons[2] = new JRadioButton("Trainer");
        menuButtons[3] = new JRadioButton("Ranking");
        menuButtons[4] = new JRadioButton("Keyboard");
        menuButtons[5] = new JRadioButton("Logout");
        menuButtons[6] = new JRadioButton("Exit");
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
        for (JRadioButton menuButton : menuButtons) {
            menuButton.setBackground(Color.WHITE);
            menuButton.addKeyListener(new KeyAdapter() {
                @Override
                public void keyTyped(KeyEvent e) {
                    if (e.getKeyChar() == KeyEvent.VK_SPACE) {
                        menuButton.doClick();
                    }
                }
            });
            buttonGroup.add(menuButton);
            add(menuButton);
        }
        menuButtons[0].requestFocus();
        menuButtons[0].addAncestorListener(new AncestorListener() {
            @Override
            public void ancestorAdded(AncestorEvent ae) {
                menuButtons[0].requestFocus();
            }

            @Override
            public void ancestorRemoved(AncestorEvent event) {
            }

            @Override
            public void ancestorMoved(AncestorEvent event) {
            }
        });
    }
}
