package view;

import controller.BuildingController;
import model.entities.Pokemon;
import utilities.Settings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class BoxPanel extends JPanel {
    private final static int POKEMON_NAME = 0;
    private final static int POKEMON_LEVEL = 1;
    private final static int MIN_POKEMON_IN_TEAM = 1;
    private final static int MAX_POKEMON_IN_TEAM = 6;
    private final JPanel teamPanel;
    private final JPanel boxPanel;
    private final PokemonPanel pokemonPanel;

    private List<Pokemon> team = new ArrayList<>();
    private List<Pokemon> box = new ArrayList<>();

    public BoxPanel(BuildingController buildingController){
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
        leftPanel.add(new JLabel("TEAM "+team.size()+"/"+MAX_POKEMON_IN_TEAM), BorderLayout.NORTH);
        leftPanel.add(teamPanel, BorderLayout.CENTER);
        add(leftPanel, BorderLayout.WEST);

        final JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(new JLabel("BOX: "+box.size()+" Pokemon"), BorderLayout.NORTH);

        final JPanel orderPanel = new JPanel();
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

        rightPanel.add(orderPanel, BorderLayout.SOUTH);
        rightPanel.add(scrollFrame, BorderLayout.CENTER);
        add(rightPanel, BorderLayout.EAST);

        add(pokemonPanel, BorderLayout.CENTER);

        final JButton close = new JButton("close");
        close.addActionListener(e -> buildingController.resumeGame());
        add(close, BorderLayout.SOUTH);

        paintBox();
    }

    private void paintBox(){
        teamPanel.removeAll();
        boxPanel.removeAll();

        for(Pokemon pokemon: team){
            final JLabel pokemonLabel = new JLabel(pokemon.name()+" lv."+pokemon.level());
            pokemonLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            pokemonLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    pokemonPanel.setPokemon(pokemon);
                }

            });
            teamPanel.add(pokemonLabel);
            final JButton button = new JButton(">>");
            teamPanel.add(button);
            button.addActionListener(e ->{
                if(team.size() > MIN_POKEMON_IN_TEAM) {
                    //TODO pokemon. metti la vita al massimo
                    team.remove(pokemon);
                    box.add(pokemon);
                    paintBox();
                }
            });
        }

        for(Pokemon pokemon: box){
            final JButton button = new JButton("<<");
            final JLabel pokemonLabel = new JLabel(pokemon.name()+" lv."+pokemon.level());
            pokemonLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            pokemonLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    pokemonPanel.setPokemon(pokemon);
                }
            });
            boxPanel.add(pokemonLabel);
            boxPanel.add(button);
            button.addActionListener(e ->{
                if(team.size() < MAX_POKEMON_IN_TEAM){
                    box.remove(pokemon);
                    team.add(pokemon);
                    paintBox();
                }
            });
        }

        revalidate();
        repaint();

    }

}
