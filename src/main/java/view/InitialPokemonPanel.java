package view;

import controller.GameController;
import database.remote.DBConnect;
import model.entities.PokemonBehaviour;
import model.entities.PokemonBehaviourImpl;
import model.entities.PokemonWithLife;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class InitialPokemonPanel extends JPanel {

    public InitialPokemonPanel(final GameController buildingController, final PokemonWithLife pokemonWithLife){
        setLayout(new BorderLayout());
        final PokemonPanel pokemonPanel = new PokemonPanel();
        pokemonPanel.setPokemon(pokemonWithLife);
        add(pokemonPanel, BorderLayout.CENTER);
        ButtonGroup buttonGroup = new ButtonGroup();

        final JPanel buttonPanel = new JPanel();
        final JRadioButton yes = new JRadioButton("yes");
        final JRadioButton no = new JRadioButton("no");

        yes.requestFocus();
        yes.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == KeyEvent.VK_SPACE) {
                    int autoIncrementCaptured = DBConnect.getAutoIncrement("pokemon");
                    final PokemonBehaviour pokemonBehaviour = new PokemonBehaviourImpl(pokemonWithLife);
                    buildingController.trainer().addMetPokemon(pokemonWithLife.pokemon().id());
                    pokemonBehaviour.insertPokemonIntoDB(buildingController.trainer().id());
                    buildingController.trainer().updateTrainer(0);
                    buildingController.trainer().addFavouritePokemon(autoIncrementCaptured);
                    buildingController.resume();
                }
            }
        });
        no.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == KeyEvent.VK_SPACE) {
                    buildingController.resume();
                }
            }
        });
        buttonGroup.add(yes);
        buttonGroup.add(no);
        buttonPanel.add(new JLabel("Do you choose this Pokémon?"), buttonPanel);
        buttonPanel.add(yes, buttonPanel);
        buttonPanel.add(no, buttonPanel);
        add(buttonPanel, BorderLayout.SOUTH);
        yes.setSelected(true);
        yes.addAncestorListener(new AncestorListener() {
            @Override
            public void ancestorAdded(AncestorEvent ae) {
                yes.requestFocus();
            }

            @Override
            public void ancestorRemoved(AncestorEvent event) { }

            @Override
            public void ancestorMoved(AncestorEvent event) { }
        });
    }
}
