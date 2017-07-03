package view;

import controller.GameViewObserver;
import utilities.Settings;

public class BattlePanel extends ImagePanel {

    public BattlePanel() {
        this.imagePanel = LoadImage.load(Settings.PANELS_FOLDER() + "battle.png");
    }

}
