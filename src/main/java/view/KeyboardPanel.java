package view;


import controller.GameController;
import utilities.Settings;

import javax.swing.*;
import java.awt.*;

public class KeyboardPanel extends BasePanel {

    public KeyboardPanel (GameController gameController) {
        this.imagePanel = LoadImage.load(Settings.PANELS_FOLDER() + "keyboard.png");

        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.setOpaque(false);
        JLabel expl = new JLabel("Keyboard explanation");
        expl.setFont(new Font("Verdana",Font.BOLD,45));
        expl.setHorizontalAlignment(JLabel.CENTER);
        northPanel.add(expl,BorderLayout.CENTER);
        this.add(northPanel,BorderLayout.NORTH);

        this.centralPanel.add(createLabel("Chat with people: Space"), k);
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
            //gameController.gamePanel().setFocusable(true);
        });

    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Verdana",Font.PLAIN,18));
        return label;
    }
}
