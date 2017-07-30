package view;

import database.local.PokedexConnect;
import model.entities.PokemonWithLife;
import scala.Tuple2;
import scala.Tuple4;
import utilities.Settings;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.IOException;

/**
 * PokemonPanel draws every info that a Pokemon captured by the trainer has: name, image, level, experience points,
 * life and attacks.
 */
public class PokemonPanel extends BasePanel {
    private final JLabel pokemonImage = new JLabel();
    private final JLabel pokemonName = new JLabel();
    private final JLabel columnTable = new JLabel();
    private final JLabel[] pokemonAttacks = new JLabel[4];
    private final JLabel pokemonLevelExperience = new JLabel();

    private static final int iconSide = (int) (Settings.FRAME_SIDE() * 0.2);

    PokemonPanel(){
        setBackground(Color.WHITE);
        this.imagePanel = LoadImage.load(Settings.PANELS_FOLDER() + "pikachu.jpg");
        this.backButton.setVisible(false);
        this.centralPanel.add(pokemonImage);
        k.gridy++;
        this.centralPanel.add(pokemonName,k);
        k.gridy++;
        this.centralPanel.add(pokemonLevelExperience,k);
        k.gridy++;
        this.centralPanel.add(columnTable,k);
        k.gridy++;
        for(int i = 0; i<pokemonAttacks.length; i++){
            pokemonAttacks[i] = new JLabel();
            this.centralPanel.add(pokemonAttacks[i],k);
            k.gridy++;
        }
    }

    /**
     * Set the pokemon passed as parameter and print all his info
     * @param pokemonWithLife the pokemon that the trainer wants info about
     */
    public void setPokemon(final PokemonWithLife pokemonWithLife){
        Image myImage;
        ImageIcon myImageIcon = null;
        try {
            myImage = ImageIO.read(getClass().getResource(Settings.POKEMON_IMAGES_FRONT_FOLDER() + pokemonWithLife.pokemon().imageName()));
            myImageIcon = new ImageIcon(myImage.getScaledInstance(iconSide,iconSide,java.awt.Image.SCALE_SMOOTH));
        } catch (IOException e) {
            e.printStackTrace();
        }
        pokemonImage.setIcon(myImageIcon);
        pokemonName.setText(pokemonWithLife.pokemon().name().toUpperCase() + " Lv."+pokemonWithLife.pokemon().level() +
                "  " + pokemonWithLife.pokemonLife()+"/"+pokemonWithLife.pokemon().experiencePoints()+" PS");
        pokemonName.setFont(new Font("Verdana",Font.PLAIN,Settings.FRAME_SIDE()/35));
        pokemonLevelExperience.setText("Level experience: "+pokemonWithLife.pokemon().levelExperience());
        pokemonLevelExperience.setFont(new Font("Verdana",Font.PLAIN,Settings.FRAME_SIDE()/35));
        columnTable.setText("ATTACKS    (Power):");
        columnTable.setFont(new Font("Verdana",Font.BOLD,Settings.FRAME_SIDE()/45));
        Tuple4 moves = pokemonWithLife.pokemon().attacks();
        Tuple2[] attacks = new Tuple2[4];
        attacks[0] = PokedexConnect.getPokemonAttack((int)moves._1()).get();
        attacks[1] = PokedexConnect.getPokemonAttack((int)moves._2()).get();
        attacks[2] = PokedexConnect.getPokemonAttack((int)moves._3()).get();
        attacks[3] = PokedexConnect.getPokemonAttack((int)moves._4()).get();
        int i = 0;
        for(JLabel pokemonAttack: pokemonAttacks) {
            pokemonAttack.setText("    " + attacks[i]._1().toString().toUpperCase()+" ("+attacks[i]._2()+")    ");
            pokemonAttack.setFont(new Font("Verdana",Font.PLAIN,Settings.FRAME_SIDE()/45));
            pokemonAttack.setBorder(new LineBorder(Color.black,1,true));
            i++;
        }
        revalidate();
        repaint();
    }
}
