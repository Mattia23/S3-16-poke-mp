package view;

import model.entities.Pokemon;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PokemonPanel extends JPanel {
    private JLabel pokemonLabel;
    private final JLabel pokemonName;
    private final JLabel pokemonLevel;
    private final JLabel[] pokemonAttacks;
    private final JLabel pokemonExperiencePoints;
    private final JLabel pokemonLevelExperience;

    public PokemonPanel(){
        //pokemonLabel = new JLabel();
        pokemonName = new JLabel();
        pokemonLevel = new JLabel();
        pokemonAttacks = new JLabel[4];
        for(int i = 0; i<pokemonAttacks.length; i++){
            pokemonAttacks[i] = new JLabel();
        }
        pokemonExperiencePoints = new JLabel();
        pokemonLevelExperience = new JLabel();
    }

    public void setPokemon(Pokemon pokemon){
        BufferedImage myPicture = null;
        try {
            myPicture = ImageIO.read(new File(pokemon.imageName()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        pokemonLabel = new JLabel(new ImageIcon(myPicture));
        add(pokemonLabel);
        pokemonName.setText(pokemon.name());
        pokemonLevel.setText(""+pokemon.level());
        for(int i = 0; i < 4; i++){
            //qui c'è solo un numero e non l'attacco, si deve accedere al db
            pokemonAttacks[i].setText(pokemon.attacks()+"");
        }
        pokemonExperiencePoints.setText(""+pokemon.experiencePoints());
        pokemonLevelExperience.setText(""+pokemon.levelExperience());
    }
}
