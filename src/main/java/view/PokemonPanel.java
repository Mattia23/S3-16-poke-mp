package view;

import database.remote.DBConnect;
import model.entities.Pokemon;
import scala.Tuple4;
import utilities.Settings;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;

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
        Map pokemonMap = DBConnect.getPokemonFromDB(Integer.parseInt(pokemon.toString())).get();
        /*String s = pokemonMap.get("name").toString().toUpperCase() + "   Life: " + pokemonMap.get("lifePoints").toString() + "/" +
                pokemonMap.get("experiencePoints").toString() + "   Lv:  " + pokemonMap.get("level").toString();
        Image myImage;
        ImageIcon myImageIcon = null;
        try {
            myImage = ImageIO.read(getClass().getResource(Settings.POKEMON_IMAGES_ICON_FOLDER() + pokemonMap.get("id").toString() + ".png"));
            myImageIcon = new ImageIcon(myImage.getScaledInstance(iconSide,iconSide,java.awt.Image.SCALE_SMOOTH));
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*BufferedImage myPicture = null;
        try {
            myPicture = ImageIO.read(new File(pokemon.imageName()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        final JLabel pokemonLabel = new JLabel(new ImageIcon(myPicture));
        add(pokemonLabel);*/
        pokemonName.setText(pokemonMap.get("name").toString().toUpperCase());
        pokemonLevel.setText(" Lv."+pokemonMap.get("level").toString());
        /*Tuple4 moves = pokemonMap.get("attacks");
        for(int i = 0; i < 4; i++){
            //qui c'è solo un numero e non l'attacco, si deve accedere al db
            pokemonAttacks[i].setText(pokemon.attacks()+"");
        }*/
    }
}
