package view;

import controller.GameViewObserver;
import model.environment.LaboratoryMap;

public class LaboratoryPanel extends BuildingPanel {
    public LaboratoryPanel(GameViewObserver gameController) {
        super(gameController, new LaboratoryMap());
    }
}
