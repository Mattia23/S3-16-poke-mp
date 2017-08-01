package view;

import controller.GameMenuController;
import utilities.Settings;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;

public class GameMenuPanel extends JPanel{
    private static final String JOPTIONPANE_TITLE = "Logout";
    private static final String JOPTIONPANE_MESSAGE = "Do you really want to log out?";
    private ButtonGroup buttonGroup = new ButtonGroup();
    private final JRadioButton[] menuButtons;

    public GameMenuPanel(GameMenuController gameMenuController){
        setLayout(new GridLayout(0,1));
        menuButtons = new JRadioButton[7];
        menuButtons[0] = new JRadioButton("Pokédex",getImageIconByName("pokedex.png"));
        menuButtons[1] = new JRadioButton("Team",getImageIconByName("team.png"));
        menuButtons[2] = new JRadioButton("Trainer",getImageIconByName("trainer.png"));
        menuButtons[3] = new JRadioButton("Ranking",getImageIconByName("ranking.png"));
        menuButtons[4] = new JRadioButton("Keyboard",getImageIconByName("keyboard.png"));
        menuButtons[5] = new JRadioButton("Logout",getImageIconByName("logout.png"));
        menuButtons[6] = new JRadioButton("Exit",getImageIconByName("exit.png"));
        menuButtons[0].addActionListener(e -> gameMenuController.showPokedex());
        menuButtons[1].addActionListener(e -> gameMenuController.showTeam());
        menuButtons[2].addActionListener(e -> gameMenuController.showTrainer());
        menuButtons[3].addActionListener(e -> gameMenuController.showRanking());
        menuButtons[4].addActionListener(e -> gameMenuController.showKeyboardExplanation());
        menuButtons[5].addActionListener(e ->{
            int reply = JOptionPane.showConfirmDialog(null, JOPTIONPANE_MESSAGE, JOPTIONPANE_TITLE, JOptionPane.YES_NO_OPTION);
            if (reply == JOptionPane.YES_OPTION) {
                gameMenuController.doLogout();
            }
            else {
                gameMenuController.doExit();
            }
        });
        menuButtons[6].addActionListener(e -> gameMenuController.doExit());
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
        menuButtons[0].setSelected(true);
        JUtil.setFocus(menuButtons[0]);
    }

    private ImageIcon getImageIconByName(String imageName){
        final Image myImage;
        ImageIcon myImageIcon = null;
        try {
            myImage = ImageIO.read(getClass().getResource(Settings.Images$.MODULE$.GAME_MENU_IMAGES() + imageName));
            myImageIcon = new ImageIcon(myImage.getScaledInstance(32,32,java.awt.Image.SCALE_SMOOTH));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  myImageIcon;
    }
}
