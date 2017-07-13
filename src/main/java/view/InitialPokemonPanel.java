package view;

import controller.GameController;
import model.entities.PokemonWithLife;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class InitialPokemonPanel extends JPanel {

    public InitialPokemonPanel(final GameController buildingController, final PokemonWithLife pokemonWithLife){
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(10,100,10,100));
        final PokemonPanel pokemonPanel = new PokemonPanel();
        pokemonPanel.setPokemon(pokemonWithLife);
        add(pokemonPanel, BorderLayout.CENTER);

        final JPanel buttonPanel = new JPanel();
        final JButton yes = new JButton("yes");
        final JButton no = new JButton("no");
        yes.addActionListener(e ->{
            buildingController.trainer().addMetPokemon(pokemonWithLife.pokemon().id());
            buildingController.trainer().addFavouritePokemon(pokemonWithLife.pokemon().id());
            buildingController.resumeGame();
        });
        no.addActionListener(e -> buildingController.resumeGame());
        buttonPanel.add(new JLabel("Do you choose this Pokémon?"), buttonPanel);
        buttonPanel.add(yes, buttonPanel);
        buttonPanel.add(no, buttonPanel);
        add(buttonPanel, BorderLayout.SOUTH);
    }
}
