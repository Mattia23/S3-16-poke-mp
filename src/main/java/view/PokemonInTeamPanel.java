package view;

import controller.GameController;
import controller.GameMenuController;
import model.entities.PokemonWithLife;

public class PokemonInTeamPanel extends PokemonPanel{
    PokemonInTeamPanel(PokemonWithLife pokemonWithLife, GameMenuController gameMenuController){
        setPokemon(pokemonWithLife);
        this.backButton.setVisible(true);
        this.backButton.addActionListener(e->{
            gameMenuController.showTeam();
        });
    }
}
