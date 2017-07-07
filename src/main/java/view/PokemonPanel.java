package view;

import model.entities.Pokemon;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PokemonPanel extends JPanel {
    //private JLabel pokemonLabel;
    private final JLabel pokemonName;
    private final JLabel pokemonLevel;
    private final JLabel[] pokemonAttacks;
    private final JLabel pokemonExperiencePoints;
    private final JLabel pokemonLevelExperience;

    public PokemonPanel(){
        setLayout(new GridLayout(0,2));
        //pokemonLabel = new JLabel();
        pokemonName = new JLabel();
        pokemonLevel = new JLabel();
        pokemonAttacks = new JLabel[4];
        for(int i = 0; i<pokemonAttacks.length; i++){
            pokemonAttacks[i] = new JLabel();
        }
        pokemonExperiencePoints = new JLabel();
        pokemonLevelExperience = new JLabel();
        add(pokemonName);
        add(pokemonLevel);
        final JPanel attackPanel = new JPanel();
        attackPanel.setLayout(new GridLayout(4,0));
        for (final JLabel pokemonAttack : pokemonAttacks) {
            attackPanel.add(pokemonAttack);
        }
        add(new JLabel("Attacks:"));
        add(attackPanel);
    }

    public void setPokemon(Pokemon pokemon){
        /*BufferedImage myPicture = null;
        try {
            myPicture = ImageIO.read(new File(pokemon.imageName()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        final JLabel pokemonLabel = new JLabel(new ImageIcon(myPicture));
        add(pokemonLabel);*/
        pokemonName.setText(pokemon.name());
        pokemonLevel.setText(" Lv."+pokemon.level());
        for(int i = 0; i < 4; i++){
            //qui c'è solo un numero e non l'attacco, si deve accedere al db
            pokemonAttacks[i].setText(pokemon.attacks()+"");
        }
        pokemonExperiencePoints.setText(""+pokemon.experiencePoints());
        pokemonLevelExperience.setText(""+pokemon.levelExperience());
    }
}
