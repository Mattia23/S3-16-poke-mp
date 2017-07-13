package view;

import controller.GameController;
import database.remote.DBConnect;
import model.entities.PokemonBehaviour;
import model.entities.PokemonBehaviourImpl;
import model.entities.PokemonWithLife;
import scala.Int;
import scala.Tuple2;
import scala.collection.JavaConverters;
import scala.collection.immutable.*;
import scala.collection.immutable.List;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.Optional;

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
            int autoIncrementCaptured = DBConnect.getAutoIncrement("pokemon");
            final PokemonBehaviour pokemonBehaviour = new PokemonBehaviourImpl(pokemonWithLife);
            buildingController.trainer().addMetPokemon(pokemonWithLife.pokemon().id());
            pokemonBehaviour.insertPokemonIntoDB(buildingController.trainer().id());
            //TODO sistemare qua il codice
            /*List<Tuple2<Int,Int>> capturedPokemonList = DBConnect.getCapturedPokemonList(buildingController.trainer().id()).get();
            java.util.List<Tuple2<Object,Object>> capturedPokemonObject = new ArrayList<>();
            for(int i = 0; i<capturedPokemonList.length(); i++){
                Tuple2<Object, Object> objectTuple = new Tuple2(capturedPokemonList.apply(i)._1(), capturedPokemonList.apply(i)._2());
                capturedPokemonObject.add(objectTuple);
            }
            buildingController.trainer().capturedPokemons_$eq(scala.collection.JavaConverters.seqAsJavaList(capturedPokemonObject));*/
            buildingController.trainer().addFavouritePokemon(autoIncrementCaptured);
            buildingController.resumeGame();
        });
        no.addActionListener(e -> buildingController.resumeGame());
        buttonPanel.add(new JLabel("Do you choose this Pokémon?"), buttonPanel);
        buttonPanel.add(yes, buttonPanel);
        buttonPanel.add(no, buttonPanel);
        add(buttonPanel, BorderLayout.SOUTH);
    }
}
