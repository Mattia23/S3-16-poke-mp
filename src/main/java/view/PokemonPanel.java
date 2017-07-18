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
        Tuple2 attack1 = PokedexConnect.getPokemonAttack((int)moves._1()).get();
        Tuple2 attack2 = PokedexConnect.getPokemonAttack((int)moves._2()).get();
        Tuple2 attack3 = PokedexConnect.getPokemonAttack((int)moves._3()).get();
        Tuple2 attack4 = PokedexConnect.getPokemonAttack((int)moves._4()).get();
        pokemonAttacks[0].setText("    " + attack1._1().toString().toUpperCase()+" ("+attack1._2()+")    ");
        pokemonAttacks[0].setFont(new Font("Verdana",Font.PLAIN,Settings.FRAME_SIDE()/45));
        pokemonAttacks[0].setBorder(new LineBorder(Color.black,1,true));
        pokemonAttacks[1].setText("    " + attack2._1().toString().toUpperCase()+" ("+attack2._2()+")    ");
        pokemonAttacks[1].setFont(new Font("Verdana",Font.PLAIN,Settings.FRAME_SIDE()/45));
        pokemonAttacks[1].setBorder(new LineBorder(Color.black,1,true));
        pokemonAttacks[2].setText("    " + attack3._1().toString().toUpperCase()+" ("+attack3._2()+")    ");
        pokemonAttacks[2].setFont(new Font("Verdana",Font.PLAIN,Settings.FRAME_SIDE()/45));
        pokemonAttacks[2].setBorder(new LineBorder(Color.black,1,true));
        pokemonAttacks[3].setText("    " + attack4._1().toString().toUpperCase()+" ("+attack4._2()+")    ");
        pokemonAttacks[3].setFont(new Font("Verdana",Font.PLAIN,Settings.FRAME_SIDE()/45));
        pokemonAttacks[3].setBorder(new LineBorder(Color.black,1,true));
        revalidate();
        repaint();
    }
}
