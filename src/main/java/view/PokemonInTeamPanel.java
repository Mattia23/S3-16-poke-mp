package view;

import controller.GameViewObserver;
import model.entities.PokemonWithLife;

public class PokemonInTeamPanel extends PokemonPanel{
    PokemonInTeamPanel(PokemonWithLife pokemonWithLife, GameViewObserver controller){
        setPokemon(pokemonWithLife);
        this.backButton.setVisible(true);
        this.backButton.addActionListener(e->{
            controller.showTeam();
        });
    }
}
