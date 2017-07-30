package view;

import controller.GameMenuController;
import model.entities.PokemonWithLife;

/**
 * PokemonInTeamPanel extends PokemonPanel adding the back button to go back in the menu.
 */
class PokemonInTeamPanel extends PokemonPanel{
    PokemonInTeamPanel(PokemonWithLife pokemonWithLife, GameMenuController gameMenuController){
        setPokemon(pokemonWithLife);
        this.backButton.setVisible(true);
        this.backButton.addActionListener(e-> gameMenuController.showTeam());
    }
}
