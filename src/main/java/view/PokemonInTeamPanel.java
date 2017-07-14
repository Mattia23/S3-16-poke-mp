package view;

import controller.GameController;
import model.entities.PokemonWithLife;

public class PokemonInTeamPanel extends PokemonPanel{
    PokemonInTeamPanel(PokemonWithLife pokemonWithLife, GameController controller){
        setPokemon(pokemonWithLife);
        this.backButton.setVisible(true);
        this.backButton.addActionListener(e->{
            controller.showTeam();
        });
    }
}
