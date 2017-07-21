package view;

import controller.GameController;
import model.map.BuildingMap;
import utilities.Settings;

import java.awt.*;

public class BuildingPanel extends GamePanel {
    protected int centerX;
    protected int centerY;
    private BuildingMap buildingMap;
    private GameController gameController;

    public BuildingPanel(final GameController gameController, final BuildingMap buildingMap) {
        super(gameController);

        this.buildingMap = buildingMap;
        this.gameController = gameController;

        centerX = (Settings.SCREEN_WIDTH()/3 - buildingMap.image().getWidth(null))/2;
        centerY = (Settings.SCREEN_WIDTH()/3 - buildingMap.image().getHeight(null))/2;

    }

    @Override
    protected void doPaint(final Graphics g) {
        g.drawImage(this.buildingMap.image(), centerX, centerY, this);

        g.drawImage(LoadImage.load(this.gameController.trainer().currentSprite().image()),
                centerX+super.getCurrentX(), centerY+super.getCurrentY(), this);

        g.drawImage(buildingMap.npc().image(), centerX+(buildingMap.npc().coordinate().x())* Settings.TILE_PIXEL(),
                centerY+(buildingMap.npc().coordinate().y())*Settings.TILE_PIXEL() -
                        (buildingMap.npc().HEIGHT()-Settings.TILE_PIXEL()), this);
    }

}
