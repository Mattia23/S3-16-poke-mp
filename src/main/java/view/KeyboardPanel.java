package view;

import controller.GameController;
import controller.GameMenuController;
import utilities.Settings;

import javax.swing.*;
import java.awt.*;

public class KeyboardPanel extends BasePanel {

    public KeyboardPanel (GameMenuController gameMenuController, GameController gameController) {
        this.imagePanel = LoadImage.load(Settings.PANELS_FOLDER() + "keyboard.png");

        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.setOpaque(false);
        JLabel expl = new JLabel("Keyboard explanation");
        expl.setFont(new Font("Verdana",Font.BOLD,45));
        expl.setHorizontalAlignment(JLabel.CENTER);
        northPanel.add(expl,BorderLayout.CENTER);
        this.add(northPanel,BorderLayout.NORTH);

        this.centralPanel.add(createLabel("Space: Chat with people"), k);
        k.gridy++;
        this.centralPanel.add(createLabel("Up arrow: trainer moves up/browse choices during battle"), k);
        k.gridy++;
        this.centralPanel.add(createLabel("Right arrow: trainer moves right"), k);
        k.gridy++;
        this.centralPanel.add(createLabel("Down arrow: trainer moves down/select choices during battle"), k);
        k.gridy++;
        this.centralPanel.add(createLabel("Left arrow: trainer moves left"), k);
        k.gridy++;
        this.centralPanel.add(createLabel("Enter: confirm choice during battle"), k);
        k.gridy++;

        this.backButton.addActionListener(e -> {
            gameController.resume();
            gameController.pause();
            gameMenuController.showGameMenu();
        });

    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Verdana",Font.PLAIN,18));
        return label;
    }
}
