package view;

import controller.GameController;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;
import java.util.List;

public class GameMenuPanel extends JPanel implements KeyListener{
    private static final String JOPTIONPANE_TITLE = "Logout";
    private static final String JOPTIONPANE_MESSAGE = "Do you really want to log out?";
    private int currentButton = 0;
    private final List<JButton> menuButtons;

    public GameMenuPanel(GameController gameController){
        setLayout(new GridLayout(0,1));
        menuButtons = new ArrayList<>();
        menuButtons.add(new JButton("Pokédex"));
        menuButtons.add(new JButton("Team"));
        menuButtons.add(new JButton("Trainer"));
        menuButtons.add(new JButton("Ranking"));
        menuButtons.add(new JButton("Keyboard"));
        menuButtons.add(new JButton("Logout"));
        menuButtons.add(new JButton("Exit"));
        menuButtons.get(0).addActionListener(e -> gameController.showPokedex());
        menuButtons.get(1).addActionListener(e -> gameController.showTeam());
        menuButtons.get(2).addActionListener(e -> gameController.showTrainer());
        menuButtons.get(3).addActionListener(e -> gameController.showRanking());
        menuButtons.get(4).addActionListener(e -> gameController.showKeyboardExplanation());
        menuButtons.get(5).addActionListener(e ->{
            int reply = JOptionPane.showConfirmDialog(null, JOPTIONPANE_MESSAGE, JOPTIONPANE_TITLE, JOptionPane.YES_NO_OPTION);
            if (reply == JOptionPane.YES_OPTION) {
                gameController.terminate();
                gameController.doLogout();
            }
            else {
                gameController.resume();
            }
        });
        menuButtons.get(6).addActionListener(e ->{
            gameController.resume();
            this.setVisible(false);
        });
        for (JButton menuButton : menuButtons) {
            menuButton.setBackground(Color.WHITE);
            menuButton.addKeyListener(this);
            add(menuButton);
        }
        menuButtons.get(0).requestFocus();
        menuButtons.get(0).addAncestorListener(new AncestorListener() {
            @Override
            public void ancestorAdded(AncestorEvent ae) {
                menuButtons.get(0).requestFocus();
            }

            @Override
            public void ancestorRemoved(AncestorEvent event) {
            }

            @Override
            public void ancestorMoved(AncestorEvent event) {
            }
        });
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_DOWN:
                if (currentButton < menuButtons.size()-1) {
                    menuButtons.get(++currentButton).requestFocus();
                }
                break;
            case KeyEvent.VK_UP:
                if (currentButton > 0) {
                    menuButtons.get(--currentButton).requestFocus();
                }
                break;
            case KeyEvent.VK_SPACE:
                menuButtons.get(currentButton).doClick();
            default:
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
