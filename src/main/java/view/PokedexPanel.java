package view;

import controller.GameViewObserver;
import database.local.PokedexConnect;
import model.entities.Trainer;
import utilities.Settings;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class PokedexPanel extends  BasePanel{
    private JPanel northPanel;

    PokedexPanel(Trainer trainer, GameViewObserver gameController){
        this.imagePanel = LoadImage.load(Settings.PANELS_FOLDER() + "pokedex.png");
        northPanel = new JPanel(new BorderLayout());
        northPanel.setOpaque(false);
        northPanel.setPreferredSize(new Dimension(Settings.FRAME_SIDE(),Settings.FRAME_SIDE()/6));
        this.add(northPanel, BorderLayout.NORTH);
        k.insets = new Insets(1,1,1,1);
        JLabel id, name, icon, captured, infoNumber;
        infoNumber = new JLabel("Caught: " + trainer.capturedPokemonId().length() + "   Seen: " + trainer.pokedex().pokedex().length());
        infoNumber.setHorizontalAlignment(JLabel.CENTER);
        infoNumber.setForeground(Color.WHITE);
        northPanel.add(infoNumber,BorderLayout.CENTER);
        Image myImage;
        ImageIcon myImageIcon = null;
        for(int i = 1; i < 152; i++) {
            id = new JLabel(i + ")");
            if(trainer.pokedex().pokedex().contains(i)){
                name = new JLabel(PokedexConnect.getPokemonName(i).get().toUpperCase());
                try {
                    myImage = ImageIO.read(getClass().getResource(Settings.POKEMON_IMAGES_ICON_FOLDER() + i + ".png"));
                    myImageIcon = new ImageIcon(myImage.getScaledInstance(50,50,java.awt.Image.SCALE_SMOOTH));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                icon = new JLabel(myImageIcon);
                if(trainer.capturedPokemonId().contains(i)){
                    try {
                        myImage = ImageIO.read(getClass().getResource(Settings.POKEBALL_IMAGES() + "pokeballIcon.png"));
                        myImageIcon = new ImageIcon(myImage.getScaledInstance(20,20,java.awt.Image.SCALE_SMOOTH));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    captured = new JLabel(myImageIcon);
                } else {
                    captured = new JLabel("");
                }
            } else {
                name = new JLabel("???");
                try {
                    myImage = ImageIO.read(getClass().getResource(Settings.POKEMON_IMAGES_ICON_FOLDER() + "0.png"));
                    myImageIcon = new ImageIcon(myImage.getScaledInstance(25,25,java.awt.Image.SCALE_SMOOTH));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                icon = new JLabel(myImageIcon);
                captured = new JLabel("");
            }
            this.centralPanel.add(id, k);
            this.centralPanel.add(name, k);
            this.centralPanel.add(icon, k);
            this.centralPanel.add(captured, k);
            k.gridy++;
        }

        this.backButton.addActionListener(e -> {gameController.resumeGame();});
        JScrollPane scroll = new JScrollPane(centralPanel,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(null);
        this.add(scroll);
    }

}
