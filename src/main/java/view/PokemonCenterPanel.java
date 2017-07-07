package view;

import controller.GameViewObserver;
import model.environment.BuildingMap;
import model.environment.PokemonCenterMap;

public class PokemonCenterPanel extends BuildingPanel {
    public PokemonCenterPanel(final GameViewObserver gameController, final BuildingMap pokemonCenterMap) {
        super(gameController, pokemonCenterMap);
    }
}
