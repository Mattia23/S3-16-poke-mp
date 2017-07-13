package view;

import scala.Tuple4;
import utilities.Settings;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Map;

public class PokemonPanel extends BasePanel {
    private final JLabel pokemonImage = new JLabel();
    private final JLabel pokemonName = new JLabel();
    private final JLabel pokemonLevel = new JLabel();
    private final JLabel pokemonLife = new JLabel();
    private final JLabel[] pokemonAttacks = new JLabel[4];
    private final JLabel pokemonExperiencePoints = new JLabel();
    private final JLabel pokemonLevelExperience = new JLabel();

    private static final int iconSide = (int) (Settings.FRAME_SIDE() * 0.2);

    public PokemonPanel(){
        setBackground(Color.WHITE);
        this.imagePanel = LoadImage.load(Settings.PANELS_FOLDER() + "trainer.jpg");
        this.backButton.setVisible(false);
        //this.backButton.setBorderPainted(false);
        //this.backButton.setFocusPainted(false);
        this.centralPanel.add(pokemonImage);
        k.gridy++;
        this.centralPanel.add(pokemonName,k);
        this.centralPanel.add(pokemonLevel,k);
        this.centralPanel.add(pokemonLife,k);
        k.gridy++;
        for(int i = 0; i<pokemonAttacks.length; i++){
            pokemonAttacks[i] = new JLabel();
            this.centralPanel.add(pokemonAttacks[i],k);
            k.gridy++;
        }
        k.gridy++;
        this.centralPanel.add(pokemonExperiencePoints,k);
        this.centralPanel.add(pokemonLevelExperience,k);
    }

    public void setPokemon(Map pokemon){
        Image myImage;
        ImageIcon myImageIcon = null;
        try {
            myImage = ImageIO.read(getClass().getResource(Settings.POKEMON_IMAGES_FRONT_FOLDER() + pokemon.get("id").toString() + ".png"));
            myImageIcon = new ImageIcon(myImage.getScaledInstance(iconSide,iconSide,java.awt.Image.SCALE_SMOOTH));
        } catch (IOException e) {
            e.printStackTrace();
        }
        pokemonImage.setIcon(myImageIcon);
        pokemonName.setText(pokemon.get("name").toString().toUpperCase());
        pokemonLevel.setText(" Lv."+pokemon.get("level"));
        pokemonLife.setText(pokemon.get("life")+"/"+pokemon.get("experiencePoints")+" PS");
        Tuple4 moves = (Tuple4) pokemon.get("attacks");
        pokemonAttacks[0].setText(moves._1().toString()+"");
        pokemonAttacks[1].setText(moves._2()+"");
        pokemonAttacks[2].setText(moves._3()+"");
        pokemonAttacks[3].setText(moves._4()+"");
        pokemonLevelExperience.setText("Level experience: "+pokemon.get("levelExperience"));
        revalidate();
        repaint();
    }
}
