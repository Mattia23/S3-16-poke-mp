package view;

import controller.GameViewObserver;
import model.environment.PokemonCenterMap;

public class PokemonCenterPanel extends BuildingPanel {
    public PokemonCenterPanel(GameViewObserver gameController) {
        super(gameController, new PokemonCenterMap());
    }
}
