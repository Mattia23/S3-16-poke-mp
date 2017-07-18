package view;

import controller.DistributedMapController;
import distributed.User;
import model.map.Building;
import model.map.GameMap;
import utilities.Settings;

import java.awt.*;

public class MapPanel extends GamePanel{

    private GameMap gameMap;
    private DistributedMapController gameController;

    public MapPanel(final DistributedMapController gameController, final GameMap gameMap) {
        super(gameController);
        this.gameMap = gameMap;
        this.gameController = gameController;
    }

    @Override
    protected void doPaint(final Graphics g) {
        for (int x = 0; x < Settings.MAP_WIDTH(); x++) {
            for (int y = 0; y < Settings.MAP_HEIGHT(); y++) {
                if (!(this.gameMap.map()[x][y] instanceof Building) ||
                        (this.gameMap.map()[x][y] instanceof Building
                                && (((Building) this.gameMap.map()[x][y]).topLeftCoordinate().x() == x)
                                && (((Building) this.gameMap.map()[x][y])).topLeftCoordinate().y() == y)) {
                    g.drawImage(LoadImage.load(this.gameMap.map()[x][y].image()),
                            ((x * Settings.TILE_PIXEL()) - super.getCurrentX()) + Settings.FRAME_SIDE() / 2 ,
                            ((y  * Settings.TILE_PIXEL()) - super.getCurrentY()) + Settings.FRAME_SIDE() / 2 ,
                            null);
                }
            }
        }
        g.drawImage(LoadImage.load(this.gameController.trainerSprite()),
                Settings.FRAME_SIDE() / 2,
                Settings.FRAME_SIDE() / 2,
                null);

        if(!this.gameController.usersTrainerSprites().isEmpty()){
            for(User user: this.gameController.connectedUsers().values()){
                g.drawImage(LoadImage.load((this.gameController.usersTrainerSprites().get(user.userId()))),
                        (( user.position().x() * Settings.TILE_PIXEL()) - super.getCurrentX()) + Settings.FRAME_SIDE() / 2 ,
                        (( user.position().y() * Settings.TILE_PIXEL()) - super.getCurrentY()) + Settings.FRAME_SIDE() / 2 ,
                        null);
            }
        }
    }
}
