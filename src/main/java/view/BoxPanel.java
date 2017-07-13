package view;

import controller.BuildingController;
import controller.GameController;
import database.remote.DBConnect;
import model.entities.Pokemon;
import model.entities.PokemonWithLife;
import scala.Int;
import scala.Tuple2;
import scala.collection.JavaConverters;
import utilities.Settings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;

public class BoxPanel extends JPanel {
    private final static int POKEMON_NAME = 0;
    private final static int POKEMON_LEVEL = 1;
    private final JPanel teamPanel;
    private final JPanel boxPanel;
    private final JLabel teamLabel = new JLabel();
    private final JLabel boxLabel = new JLabel();
    private final PokemonPanel pokemonPanel;
    private List<Object> favoritePokemon;
    private List<Tuple2<Object, Object>> capturedPokemon;

    public BoxPanel(BuildingController buildingController){
        this.favoritePokemon = new ArrayList<>();
        List<Object> favoritePokemon = scala.collection.JavaConverters.seqAsJavaList(buildingController.trainer().favouritePokemons());
        this.favoritePokemon = new ArrayList<>();
        this.favoritePokemon.addAll(favoritePokemon);
        this.capturedPokemon = scala.collection.JavaConverters.seqAsJavaList(buildingController.trainer().capturedPokemons());
        setBackground(Color.WHITE);
        setLayout(new BorderLayout());
        JLabel title = new JLabel("Pokémon Box");
        title.setHorizontalAlignment(JLabel.CENTER);
        add(title, BorderLayout.NORTH);

        teamPanel = new JPanel(new GridLayout(0,2));
        boxPanel = new JPanel(new GridLayout(0,2));

        pokemonPanel = new PokemonPanel();

        final JScrollPane scrollFrame = new JScrollPane(boxPanel,ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        boxPanel.setAutoscrolls(true);

        final JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setSize(new Dimension(Settings.SCREEN_WIDTH()/6, 0));
        leftPanel.add(teamLabel, BorderLayout.NORTH);
        leftPanel.add(teamPanel, BorderLayout.CENTER);
        add(leftPanel, BorderLayout.WEST);

        final JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(boxLabel, BorderLayout.NORTH);

        /*final JPanel orderPanel = new JPanel();
        String[] orderStrings = {"Name", "Level"};
        JComboBox<String> comboBox = new JComboBox<>(orderStrings);
        comboBox.addActionListener( e -> {
            if(comboBox.getSelectedIndex() == POKEMON_NAME){
                box.sort(Comparator.comparing(Pokemon::name));
            }else if(comboBox.getSelectedIndex() == POKEMON_LEVEL){
                box.sort(Comparator.comparing(Pokemon::level));
            }
        });
        orderPanel.add(new JLabel("Order by"));
        orderPanel.add(comboBox);

        rightPanel.add(orderPanel, BorderLayout.SOUTH);*/
        rightPanel.add(scrollFrame, BorderLayout.CENTER);
        add(rightPanel, BorderLayout.EAST);

        add(pokemonPanel, BorderLayout.CENTER);
        pokemonPanel.setVisible(false);
        final JButton close = new JButton("close");
        close.addActionListener(e -> {
            buildingController.trainer().setAllFavouritePokemon(this.favoritePokemon);
            buildingController.resumeGame();
        });
        add(close, BorderLayout.SOUTH);

        paintBox();
    }

    private void paintBox(){
        teamPanel.removeAll();
        boxPanel.removeAll();

        for(Object pokemonObject: this.favoritePokemon){
            Integer pokemonId = Integer.parseInt(pokemonObject.toString());
            if(pokemonId != 0) {
                Map pokemon = DBConnect.getPokemonFromDB(pokemonId).get();
                final JLabel pokemonLabel = new JLabel(pokemon.get("name") + " Lv." + pokemon.get("level"));
                pokemonLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                pokemonLabel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        pokemonPanel.setPokemon(pokemon);
                        if (!pokemonPanel.isVisible()) pokemonPanel.setVisible(true);
                    }

                });
                teamPanel.add(pokemonLabel);
                final JButton button = new JButton(">>");
                teamPanel.add(button);
                button.addActionListener(e -> {
                    if (Collections.frequency(this.favoritePokemon, 0) < favoritePokemon.size() - 1) {
                        this.favoritePokemon.set(this.favoritePokemon.indexOf(pokemonId), 0);
                        paintBox();
                    }
                });
            }
        }

        for(Tuple2<Object, Object> pokemonObject: this.capturedPokemon){
            Integer pokemonId = Integer.parseInt(pokemonObject._1().toString());
            if(!this.favoritePokemon.contains(pokemonId)){
                Map pokemon = DBConnect.getPokemonFromDB(pokemonId).get();
                final JButton button = new JButton("<<");
                final JLabel pokemonLabel = new JLabel(pokemon.get("name")+" Lv."+pokemon.get("level"));
                pokemonLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                pokemonLabel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        pokemonPanel.setPokemon(pokemon);
                        if(!pokemonPanel.isVisible()) pokemonPanel.setVisible(true);
                    }
                });
                boxPanel.add(pokemonLabel);
                boxPanel.add(button);
                button.addActionListener(e ->{
                    if(this.favoritePokemon.contains(0)){
                        this.favoritePokemon.set(this.favoritePokemon.indexOf(0),pokemonId);
                        paintBox();
                    }
                });
            }
        }
        refreshTeamAndBoxNumber();
        revalidate();
        repaint();

    }

    public void refreshTeamAndBoxNumber(){
        teamLabel.setText("TEAM "+ (this.favoritePokemon.size() - Collections.frequency(this.favoritePokemon, 0)) +"/"+favoritePokemon.size());
        boxLabel.setText("BOX: "+(this.capturedPokemon.size() - (this.favoritePokemon.size() - Collections.frequency(this.favoritePokemon, 0)))+" Pokemon");
    }

}
