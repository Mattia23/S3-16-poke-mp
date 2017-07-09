package view;

import controller.BattleController;
import database.remote.DBConnect;
import model.entities.Trainer;
import scala.Tuple2;
import utilities.Settings;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class PokemonChoicePanel extends BasePanel{
    private static final int FONT_SIZE = (int) (Settings.FRAME_SIDE() * 0.034);
    private BattleController controller;
    private ButtonGroup pokemonButtonGroup = new ButtonGroup();
    private List<Tuple2<JRadioButton,Integer>> buttonList = new ArrayList<>();
    private JButton submit;
    private static final int iconSide = (int) (Settings.FRAME_SIDE() * 0.1077);


    public PokemonChoicePanel(BattleController controller, Trainer trainer) {
        this.controller = controller;
        this.imagePanel = LoadImage.load(Settings.PANELS_FOLDER() + "pokemon-choice.png");
        this.backButton.setVisible(false);
        this.submit = new JButton("Choose!");
        List pokemonList = scala.collection.JavaConverters.seqAsJavaList(trainer.favouritePokemons());
        Boolean first = true;
        k.insets = new Insets(1,1,1,1);
        for(Object pokemon: pokemonList){
            if(Integer.parseInt(pokemon.toString()) != 0){
                Map pokemonMap = DBConnect.getPokemonFromDB(Integer.parseInt(pokemon.toString())).get();
                String s = pokemonMap.get("name").toString().toUpperCase() + "   Life: " + pokemonMap.get("lifePoints").toString() + "/" +
                        pokemonMap.get("experiencePoints").toString() + "   Lv:  " + pokemonMap.get("level").toString();
                Image myImage;
                ImageIcon myImageIcon = null;
                try {
                    myImage = ImageIO.read(getClass().getResource(Settings.POKEMON_IMAGES_ICON_FOLDER() + pokemonMap.get("id").toString() + ".png"));
                    myImageIcon = new ImageIcon(myImage.getScaledInstance(iconSide,iconSide,java.awt.Image.SCALE_SMOOTH));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                JRadioButton radioButton = new JRadioButton(s,myImageIcon);
                radioButton.setOpaque(false);
                radioButton.setFont(new Font("Verdana", Font.PLAIN, FONT_SIZE));
                if(Integer.parseInt(pokemonMap.get("lifePoints").toString()) == 0){
                    radioButton.setEnabled(false);
                }
                if(first && Integer.parseInt(pokemonMap.get("lifePoints").toString()) != 0){
                    radioButton.setSelected(true);
                    first = false;
                }
                buttonList.add(new Tuple2<>(radioButton,Integer.parseInt(pokemon.toString())));
                pokemonButtonGroup.add(radioButton);
                this.centralPanel.add(radioButton, k);
                k.gridy++;
            }
        }

        this.centralPanel.add(submit,k);
        this.submit.addActionListener(e -> {
            for(Tuple2<JRadioButton,Integer> radioButton: buttonList){
                if(radioButton._1().isSelected()){
                    controller.pokemonToChangeIsSelected(radioButton._2());
                }
            }
        });
    }
}
