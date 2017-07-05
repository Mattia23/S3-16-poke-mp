package view;

import database.local.PokedexConnect;
import model.entities.Owner;
import model.entities.PokemonWithLife;
import utilities.Settings;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class BattlePanel extends ImagePanel {
    private final static int IMAGE_POKEMON_SIZE = Settings.FRAME_SIDE() / 4;
    private final static int FONT_SIZE = (int)(Settings.FRAME_SIDE() * 0.034);
    private final static int FONT_LIFE_SIZE = (int)(Settings.FRAME_SIDE() * 0.02);
    private final static int PROGRESS_BAR_W = (int)(Settings.FRAME_SIDE() * 0.23437);
    private final static int PROGRESS_BAR_H = (int)(Settings.FRAME_SIDE() * 0.021648);
    private final static Dimension[] POKEMON_IMG_POSE = {newDimension(0.65,0.10),newDimension(0.11,0.48)};
    private final static Dimension[] POKEMON_NAME_POSE = {newDimension(0.08,0.165),newDimension(0.57,0.565)};
    private final static Dimension[] POKEMON_LEVEL_POSE = {newDimension(0.06,0.205),newDimension(0.55,0.61)};
    private final static Dimension[] POKEMON_PROGRESS_BAR_POSE = {newDimension(0.214,0.2155),newDimension(0.7,0.613)};
    private final static Dimension POKEMON_LIFE_POSE = newDimension(0.845,0.625);
    private final static Dimension POKEMON_LEV_EXP_POSE = newDimension(0.64,0.666);
    private PokemonWithLife[] pokemonEntities;
    private JButton[] pokemonImages = new JButton[]{new JButton(),new JButton()};
    private String[] pokemonSide = new String[]{Settings.POKEMON_IMAGES_FRONT_FOLDER(),Settings.POKEMON_IMAGES_BACK_FOLDER()};
    private JTextField[] pokemonNames = new JTextField[]{new JTextField(),new JTextField()};
    private JTextField[] pokemonLevels = new JTextField[]{new JTextField(),new JTextField()};
    private JProgressBar[] pokemonProgressBar = new JProgressBar[]{new JProgressBar(0,100),new JProgressBar(0,100)};
    private JTextField myPokemonLife = new JTextField();
    private JProgressBar pokemonLevExpBar = new JProgressBar(0, 100);
    private Map<String,JButton> trainerChoices = new LinkedHashMap<>();
    private String[] myPokemonAttacks = new String[4];
    private Map<String,JButton> attacks = new LinkedHashMap<>();

    public BattlePanel(PokemonWithLife myPokemon, PokemonWithLife otherPokemon, JFrame frame) {
        this.imagePanel = LoadImage.load(Settings.PANELS_FOLDER() + "battle.png");
        this.pokemonEntities = new PokemonWithLife[]{otherPokemon, myPokemon};
        this.setLayout(null);
        this.setFocusable(true);
        this.requestFocusInWindow();
        createBattleGraphic();
        createTrainerChoicesGraphic();
        catchTrainerChoices(frame);
    }

    private void createBattleGraphic() {
        for(int i=0;i<pokemonEntities.length;i++) {
            createJTextField(pokemonNames[i],pokemonEntities[i].pokemon().name().toUpperCase());
            pokemonNames[i].setBounds(POKEMON_NAME_POSE[i].width,POKEMON_NAME_POSE[i].height,200,30);
            pokemonNames[i].setFont(new Font("Verdana", Font.BOLD, FONT_SIZE));
            createJTextField(pokemonLevels[i],"L."+pokemonEntities[i].pokemon().level());
            pokemonLevels[i].setBounds(POKEMON_LEVEL_POSE[i].width,POKEMON_LEVEL_POSE[i].height,70,30);
            pokemonLevels[i].setFont(new Font("Verdana", Font.PLAIN, FONT_SIZE));
            Image myImage;
            ImageIcon myImageIcon = null;
            try {
                myImage = ImageIO.read(getClass().getResource(pokemonSide[i] + pokemonEntities[i].pokemon().id() + ".png"));
                myImageIcon = new ImageIcon(myImage.getScaledInstance(IMAGE_POKEMON_SIZE,IMAGE_POKEMON_SIZE,java.awt.Image.SCALE_SMOOTH));
            } catch (IOException e) {
                e.printStackTrace();
            }
            pokemonImages[i].setIcon(myImageIcon);
            pokemonImages[i].setBounds(POKEMON_IMG_POSE[i].width,POKEMON_IMG_POSE[i].height,IMAGE_POKEMON_SIZE,IMAGE_POKEMON_SIZE);
            pokemonImages[i].setBorder(null);
            pokemonImages[i].setContentAreaFilled(false);
            this.add(pokemonImages[i]);
            pokemonProgressBar[i].setBounds(POKEMON_PROGRESS_BAR_POSE[i].width,POKEMON_PROGRESS_BAR_POSE[i].height,PROGRESS_BAR_W,PROGRESS_BAR_H);
            pokemonProgressBar[i].setValue(100);
            this.add(pokemonProgressBar[i]);
        }
        createJTextField(myPokemonLife,pokemonEntities[1].pokemonLife()+"/"+pokemonEntities[1].pokemon().experiencePoints());
        myPokemonLife.setBounds(POKEMON_LIFE_POSE.width,POKEMON_LIFE_POSE.height,100,30);
        myPokemonLife.setFont(new Font("Verdana", Font.PLAIN, FONT_LIFE_SIZE));
        this.add(myPokemonLife);
        pokemonLevExpBar.setValue(pokemonEntities[1].pokemon().levelExperience());
        pokemonLevExpBar.setBounds(POKEMON_LEV_EXP_POSE.width,POKEMON_LEV_EXP_POSE.height,170,8);
        this.add(pokemonLevExpBar);
    }

    private void createTrainerChoicesGraphic() {
        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.setBounds(Settings.FRAME_SIDE()/40,(int)(Settings.FRAME_SIDE()*0.72),(int)(Settings.FRAME_SIDE()*0.9),Settings.FRAME_SIDE()/5);
        southPanel.setOpaque(false);
        JPanel southWestPanel = new JPanel();
        GridLayout gridLayout = new GridLayout(4,1);
        gridLayout.setVgap(10);
        southWestPanel.setLayout(gridLayout);
        southWestPanel.setOpaque(false);
        trainerChoices.put("Attack",new JButton("Attack"));
        trainerChoices.put("Change pokemon",new JButton("Change Pokemon"));
        trainerChoices.put("Pokeball",new JButton("Pokeball (x3)"));
        trainerChoices.put("Escape",new JButton("Escape"));
        for(String s : trainerChoices.keySet()) {
            southWestPanel.add(trainerChoices.get(s));
        }
        JPanel southEastPanel = new JPanel();
        gridLayout = new GridLayout(2,2);
        gridLayout.setVgap(10);
        gridLayout.setHgap(10);
        southEastPanel.setLayout(gridLayout);
        southEastPanel.setBounds(Settings.FRAME_SIDE()/35,(int)(Settings.FRAME_SIDE()*0.72),(int)Settings.FRAME_SIDE(),Settings.FRAME_SIDE()/5);
        southEastPanel.setOpaque(false);
        int[] pokemonAttacks = {(int)pokemonEntities[1].pokemon().attacks()._1(),(int)pokemonEntities[1].pokemon().attacks()._2(),
                (int)pokemonEntities[1].pokemon().attacks()._3(),(int)pokemonEntities[1].pokemon().attacks()._4()};
        for(int i=0;i<4;i++) {
            this.myPokemonAttacks[i] = PokedexConnect.getPokemonAttack(pokemonAttacks[i]).get()._1().toUpperCase();
            attacks.put(myPokemonAttacks[i],new JButton(myPokemonAttacks[i]));
            southEastPanel.add(attacks.get(myPokemonAttacks[i]));
            attacks.get(myPokemonAttacks[i]).setVisible(false);
        }
        southPanel.add(southWestPanel,BorderLayout.WEST);
        southPanel.add(southEastPanel,BorderLayout.CENTER);
        this.add(southPanel);
    }

    private void catchTrainerChoices(JFrame frame) {
        frame.getRootPane().setDefaultButton(trainerChoices.get(trainerChoices.entrySet().iterator().next().getKey()));
        trainerChoices.get("Attack").addActionListener(e -> {
            showAttacks(true);
        });
        trainerChoices.get("Change pokemon").addActionListener(e -> {System.out.println("2");});
        trainerChoices.get("Pokeball").addActionListener(e -> {System.out.println("3");});
        trainerChoices.get("Escape").addActionListener(e -> {System.out.println("4");});
        for(String att : attacks.keySet()) {
            attacks.get(att).addActionListener(e -> {
                //passa l'attacco al controller
                showAttacks(false);
            });
        }
        this.addKeyListener(new KeyListener() {
            int i = 0;
            @Override
            public void keyTyped(KeyEvent e) {

            }
            @Override
            public void keyPressed(KeyEvent e) {
                switch(e.getKeyCode()) {
                    case KeyEvent.VK_DOWN:
                        if(i<trainerChoices.size()-1){
                            i++;
                        }
                        break;
                    case KeyEvent.VK_UP:
                        if(i>0) {
                            i--;
                        }
                        break;
                    default: break;
                }
                Object[] choices = trainerChoices.keySet().toArray();
                frame.getRootPane().setDefaultButton(trainerChoices.get(choices[i].toString()));
            }
            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
    }

    private static Dimension newDimension(Double d1, Double d2) {
        return new Dimension((int)(Settings.FRAME_SIDE()*d1),(int)(Settings.FRAME_SIDE()*d2));
    }

    private void createJTextField(JTextField jTextField,String text) {
        jTextField.setText(text);
        jTextField.setOpaque(false);
        jTextField.setBorder(null);
        this.add(jTextField);
    }

    private void showAttacks(boolean flag) {
        for(String c : trainerChoices.keySet()) {
            trainerChoices.get(c).setVisible(!flag);
        }
        for(String att : attacks.keySet()) {
            attacks.get(att).setVisible(flag);
        }
    }

    public void setPokemonLife(int life) {
        myPokemonLife.setText(pokemonEntities[1].pokemonLife()+"/"+pokemonEntities[1].pokemon().experiencePoints());
    }

    public void setPokemonLifeProgressBar(int life, int owner) {
        if (owner == Owner.TRAINER().id()) { pokemonProgressBar[1].setValue(life); }
        else { pokemonProgressBar[0].setValue(life); }
    }
}
