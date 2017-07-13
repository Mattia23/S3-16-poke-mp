package view;

import controller.BattleController;
import database.remote.DBConnect;
import javafx.scene.control.RadioButton;
import model.entities.Trainer;
import scala.Tuple2;
import utilities.Settings;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class PokemonChoicePanel extends BasePanel{
    private static final int FONT_SIZE = (int) (Settings.FRAME_SIDE() * 0.034);
    private BattleController controller;
    private ButtonGroup pokemonButtonGroup = new ButtonGroup();
    private List<Tuple2<JRadioButton,Integer>> buttonList = new ArrayList<>();
    private JButton info;
    private JLabel infoText;
    private static final int iconSide = (int) (Settings.FRAME_SIDE() * 0.1177);
    private static final int infoSide = (int) (Settings.FRAME_SIDE() * 0.05);


    public PokemonChoicePanel(BattleController controller, Trainer trainer) {
        this.controller = controller;
        this.imagePanel = LoadImage.load(Settings.PANELS_FOLDER() + "pokemon-choice.png");
        this.backButton.setVisible(false);
        this.infoText = new JLabel("Use arrow keys to select your Pokemon, then Enter to choose it.");
        this.infoText.setVisible(false);
        this.downPanel.add(this.infoText, BorderLayout.CENTER);
        Image myImage;
        ImageIcon myImageIcon = null;
        try {
            myImage = ImageIO.read(getClass().getResource(Settings.IMAGES_FOLDER() + "info.png"));
            myImageIcon = new ImageIcon(myImage.getScaledInstance(infoSide,infoSide,java.awt.Image.SCALE_SMOOTH));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.info = new JButton(myImageIcon);
        this.info.setBorderPainted(false);
        this.info.setContentAreaFilled(false);
        this.info.setOpaque(false);
        this.info.addActionListener(e -> {
            if(!this.infoText.isVisible()){
                this.infoText.setVisible(true);
            } else {
                this.infoText.setVisible(false);
            }
        });
        this.info.setToolTipText("Use arrow keys to select your Pokemon, then Enter to choose it.");
        this.downPanel.add(this.info,BorderLayout.WEST);
        List pokemonList = scala.collection.JavaConverters.seqAsJavaList(trainer.favouritePokemons());
        Boolean first = true;
        k.insets = new Insets(1,1,1,1);
        for(Object pokemon: pokemonList){
            if(Integer.parseInt(pokemon.toString()) != 0){
                Map pokemonMap = DBConnect.getPokemonFromDB(Integer.parseInt(pokemon.toString())).get();
                String s = pokemonMap.get("name").toString().toUpperCase() + "   Life: " + pokemonMap.get("lifePoints").toString() + "/" +
                        pokemonMap.get("experiencePoints").toString() + "   Lv:  " + pokemonMap.get("level").toString();
                try {
                    myImage = ImageIO.read(getClass().getResource(Settings.POKEMON_IMAGES_ICON_FOLDER() + pokemonMap.get("id").toString() + ".png"));
                    myImageIcon = new ImageIcon(myImage.getScaledInstance(iconSide,iconSide,java.awt.Image.SCALE_SMOOTH));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                JRadioButton radioButton = new JRadioButton(s,myImageIcon);
                radioButton.setOpaque(false);
                radioButton.setFont(new Font("Verdana", Font.PLAIN, FONT_SIZE));
                radioButton.addItemListener(e -> {
                    JRadioButton button = (JRadioButton) e.getItem();
                    if (e.getStateChange() == ItemEvent.SELECTED) {
                        button.setFont(new Font("Verdana", Font.BOLD, FONT_SIZE));
                    }
                    else if (e.getStateChange() == ItemEvent.DESELECTED) {
                        button.setFont(new Font("Verdana", Font.PLAIN, FONT_SIZE));
                    }
                });
                radioButton.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyTyped(KeyEvent e) {
                        if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                            for(Tuple2<JRadioButton,Integer> radioButton: buttonList){
                                if(radioButton._1().isSelected()){
                                    controller.pokemonToChangeIsSelected(radioButton._2());
                                }
                            }
                        }
                    }
                });
                if(Integer.parseInt(pokemonMap.get("lifePoints").toString()) == 0){
                    radioButton.setEnabled(false);
                }
                if(first && Integer.parseInt(pokemonMap.get("lifePoints").toString()) != 0){
                    radioButton.requestFocus();
                    radioButton.setSelected(true);
                    radioButton.setFont(new Font("Verdana", Font.BOLD, FONT_SIZE));
                    radioButton.addAncestorListener(new AncestorListener() {
                        @Override
                        public void ancestorAdded(AncestorEvent ae) {
                            radioButton.requestFocus();
                        }

                        @Override
                        public void ancestorRemoved(AncestorEvent event) { }

                        @Override
                        public void ancestorMoved(AncestorEvent event) { }
                    });
                    first = false;
                }
                buttonList.add(new Tuple2<>(radioButton,Integer.parseInt(pokemon.toString())));
                pokemonButtonGroup.add(radioButton);
                this.centralPanel.add(radioButton, k);
                k.gridy++;
            }
        }
    }
}
