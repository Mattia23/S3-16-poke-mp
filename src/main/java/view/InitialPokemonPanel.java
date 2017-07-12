package view;

import controller.GameController;
import model.entities.Pokemon;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class InitialPokemonPanel extends JPanel {

    public InitialPokemonPanel(final GameController buildingController, final Pokemon pokemon){
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(10,100,10,100));
        final PokemonPanel pokemonPanel = new PokemonPanel();
        pokemonPanel.setPokemon(null);
        add(pokemonPanel, BorderLayout.CENTER);

        final JPanel buttonPanel = new JPanel();
        final JButton yes = new JButton("yes");
        final JButton no = new JButton("no");
        yes.addActionListener(e ->{
            //TODO correggere più avanti
            buildingController.trainer().addFavouritePokemon(pokemon.id());
            buildingController.resumeGame();
        });
        no.addActionListener(e -> buildingController.resumeGame());
        buttonPanel.add(new JLabel("Do you choose this Pokémon?"), buttonPanel);
        buttonPanel.add(yes, buttonPanel);
        buttonPanel.add(no, buttonPanel);
        add(buttonPanel, BorderLayout.SOUTH);
    }
}
