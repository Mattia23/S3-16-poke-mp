package view;

import controller.GameController;
import database.local.PokedexConnect;
import utilities.Settings;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class PokedexPanel extends  BasePanel{
    private JPanel northPanel;
    private static final int INFO_FONT_SIZE = Settings.FRAME_SIDE()/32;
    private static final int FONT_SIZE = Settings.FRAME_SIDE()/25;
    private static final int POKEMON_SIDE = (int) (Settings.FRAME_SIDE()/5.8);
    private static final int POKEBALL_SIDE = Settings.FRAME_SIDE()/22;

    public PokedexPanel(GameController gameController){
        this.imagePanel = LoadImage.load(Settings.PANELS_FOLDER() + "pokedex.png");
        northPanel = new JPanel(new BorderLayout());
        northPanel.setOpaque(false);
        northPanel.setPreferredSize(new Dimension(Settings.FRAME_SIDE(),Settings.FRAME_SIDE()/6));
        this.add(northPanel, BorderLayout.NORTH);
        k.insets = new Insets(0,0,0,10);
        JLabel id, name, icon, captured, infoNumber;
        infoNumber = new JLabel("      INFO:           CAUGHT: " + gameController.trainer().capturedPokemonId().length() + "   SEEN: " + gameController.trainer().pokedex().pokedex().length());
        infoNumber.setFont(new Font("Verdana", Font.PLAIN, INFO_FONT_SIZE));
        infoNumber.setHorizontalAlignment(JLabel.CENTER);
        infoNumber.setForeground(Color.WHITE);
        northPanel.add(infoNumber,BorderLayout.CENTER);
        Image myImage;
        ImageIcon myImageIcon = null;
        for(int i = 1; i < 152; i++) {
            id = new JLabel(i + ")");
            if(gameController.trainer().pokedex().pokedex().contains(i)){
                name = new JLabel(PokedexConnect.getPokemonName(i).get().toUpperCase());
                try {
                    myImage = ImageIO.read(getClass().getResource(Settings.POKEMON_IMAGES_FRONT_FOLDER() + i + ".png"));
                    myImageIcon = new ImageIcon(myImage.getScaledInstance(POKEMON_SIDE,POKEMON_SIDE,java.awt.Image.SCALE_SMOOTH));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                icon = new JLabel(myImageIcon);
                if(gameController.trainer().capturedPokemonId().contains(i)){
                    try {
                        myImage = ImageIO.read(getClass().getResource(Settings.POKEBALL_IMAGES() + "pokeballIcon.png"));
                        myImageIcon = new ImageIcon(myImage.getScaledInstance(POKEBALL_SIDE,POKEBALL_SIDE,java.awt.Image.SCALE_SMOOTH));
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
                    myImageIcon = new ImageIcon(myImage.getScaledInstance(POKEMON_SIDE,POKEMON_SIDE,java.awt.Image.SCALE_SMOOTH));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                icon = new JLabel(myImageIcon);
                captured = new JLabel("");
            }
            id.setFont(new Font("Verdana", Font.PLAIN, FONT_SIZE));
            name.setFont(new Font("Verdana", Font.PLAIN, FONT_SIZE));
            this.centralPanel.add(id, k);
            this.centralPanel.add(name, k);
            this.centralPanel.add(icon, k);
            this.centralPanel.add(captured, k);
            k.gridy++;
        }

        this.backButton.addActionListener(e -> {
            gameController.resume();
           // gameController.gamePanel().setFocusable(true);
        });
        JScrollPane scroll = new JScrollPane(centralPanel,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(null);
        this.add(scroll);
    }

}
