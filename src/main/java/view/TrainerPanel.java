package view;

import controller.GameViewObserver;
import model.entities.Trainer;
import utilities.Settings;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class TrainerPanel extends BasePanel {

    public TrainerPanel(Trainer trainer, GameViewObserver gameController) {
        this.imagePanel = LoadImage.load(Settings.PANELS_FOLDER() + "trainer.jpg");
        Image myImage;
        ImageIcon myImageIcon = null;
        try {
            myImage = ImageIO.read(getClass().getResource(gameController.trainerSprite()));
            myImageIcon = new ImageIcon(myImage.getScaledInstance(32,32,java.awt.Image.SCALE_SMOOTH));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.centralPanel.add(new JLabel(myImageIcon));
        this.centralPanel.add(new JLabel(trainer.name()),k);
        k.gridy++;
        this.centralPanel.add(new JLabel("Livello:"),k);
        this.centralPanel.add(new JLabel(""+trainer.level()),k);
        k.gridy++;
        this.centralPanel.add(new JLabel("Esperienza"),k);
        this.centralPanel.add(new JLabel(""+trainer.experiencePoints()),k);
        k.gridy++;
        this.centralPanel.add(new JLabel("Posizione in classifica:"),k);
        this.centralPanel.add(new JLabel(),k);
        k.gridy++;
        this.centralPanel.add(new JLabel("Battaglie vinte:"),k);
        this.centralPanel.add(new JLabel(),k);
        this.backButton.addActionListener(e -> {
            gameController.resumeGame();
            gameController.gamePanel().setFocusable(true);
        });
    }
}
