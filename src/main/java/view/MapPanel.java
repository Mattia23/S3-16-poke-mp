package view;

import controller.DistributedMapController;
import controller.GameController;
import distributed.Player;
import distributed.PlayerPositionDetails;
import model.map.Building;
import model.map.GameMap;
import utilities.Settings;

import java.awt.*;
import java.util.concurrent.ConcurrentMap;

public class MapPanel extends GamePanel{

    private GameMap gameMap;
    private GameController mapController;
    private DistributedMapController distributedMapController;
    private static final int OFFSET = 2;

    public MapPanel(GameController mapController, DistributedMapController distributedMapController, GameMap gameMap) {
        super(mapController);
        this.gameMap = gameMap;
        this.mapController = mapController;
        this.distributedMapController = distributedMapController;
    }

    @Override
    protected void doPaint(Graphics g) {
        long time = System.currentTimeMillis();
        drawMapElements(g);
        drawBuildings(g);
        drawOtherTrainers(g);
        drawTrainer(g);
    }

    private void drawMapElements(Graphics g){
        int initialX = calculateInitialCoordinate(this.getCurrentX());
        int finalX = calculateFinalCoordinate(this.getCurrentX());
        int initialY = calculateInitialCoordinate(this.getCurrentY());
        int finalY = calculateFinalCoordinate(this.getCurrentY());

        int initialMapX = (initialX <= 0 ) ? 0 : initialX;
        int finalMapX = (finalX >= Settings.MAP_WIDTH() ) ? Settings.MAP_WIDTH() : finalX;
        int initialMapY = (initialY <= 0 ) ? 0 : initialY;
        int finalMapY = (finalY >= Settings.MAP_HEIGHT() ) ? Settings.MAP_HEIGHT() : finalY;

        for (int x = initialMapX; x < finalMapX; x++) {
            for (int y = initialMapY; y < finalMapY; y++) {
                if (!(this.gameMap.map()[x][y] instanceof Building)) {
                    g.drawImage(LoadImage.load(this.gameMap.map()[x][y].image()),
                            this.calculateCoordinate(x, this.getCurrentX()),
                            this.calculateCoordinate(y, this.getCurrentY()),
                            null);
                }
            }
        }
    }

    private int calculateInitialCoordinate(int centerCoordinate){
        return ((centerCoordinate - Settings.FRAME_SIDE() / 2) / Settings.TILE_PIXEL()) - OFFSET;
    }

    private int calculateFinalCoordinate(int centerCoordinate){
        return ((centerCoordinate + Settings.FRAME_SIDE() / 2) / Settings.TILE_PIXEL()) + OFFSET;
    }

    private void drawBuildings(Graphics g) {
        for (int x = 0; x < Settings.MAP_WIDTH(); x++) {
            for (int y = 0; y < Settings.MAP_HEIGHT(); y++) {
                if ((this.gameMap.map()[x][y] instanceof Building
                    && (((Building) this.gameMap.map()[x][y]).topLeftCoordinate().x() == x)
                    && (((Building) this.gameMap.map()[x][y])).topLeftCoordinate().y() == y)) {
                    g.drawImage(LoadImage.load(this.gameMap.map()[x][y].image()),
                            this.calculateCoordinate(x, this.getCurrentX()),
                            this.calculateCoordinate(y, this.getCurrentY()),
                            null);
                }
            }
        }
    }

    private void drawOtherTrainers(Graphics g){
        ConcurrentMap<Object, PlayerPositionDetails> map = this.distributedMapController.playersPositionDetails();
        if(!map.isEmpty()){
            for(Player player : this.distributedMapController.connectedPlayers().getAll().values()){
                if(player.isVisible()) {
                    PlayerPositionDetails positionDetails = map.get(player.userId());
                    g.drawImage(LoadImage.load((positionDetails.currentSprite().image())),
                            this.calculateCoordinate(positionDetails.coordinateX(), this.getCurrentX()),
                            this.calculateCoordinate(positionDetails.coordinateY(), this.getCurrentY()),
                            null);
                }
            }
        }
    }

    private int calculateCoordinate(double coordinate, int centerCoordinate) {
        return this.coordinateInPixels(coordinate) - centerCoordinate + Settings.FRAME_SIDE() / 2;
    }

    private int coordinateInPixels(double currentCoordinate) {
        return (int)(currentCoordinate * Settings.TILE_PIXEL());
    }

    private void drawTrainer(Graphics g){
        g.drawImage(LoadImage.load(this.mapController.trainer().currentSprite().image()),
                Settings.FRAME_SIDE() / 2,
                Settings.FRAME_SIDE() / 2,
                null);
    }

}
