package view;
import controller.GameKeyListener;
import controller.GameViewObserver;
import model.map.Building;
import model.map.GameMap;
import utilities.Settings;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel{

    private GameMap gameMap;
    private int currentX;
    private int currentY;
    private GameViewObserver gameController;

    public GamePanel(final GameViewObserver gameController, final GameMap gameMap) {
        this.gameMap = gameMap;
        this.setFocusable(true);
        this.requestFocusInWindow();
        this.gameController = gameController;
        GameKeyListener keyListener = new GameKeyListener(gameController);
        this.addKeyListener(keyListener);
        this.currentX = gameController.trainerPosition().x() * Settings.TILE_PIXEL();
        this.currentY = gameController.trainerPosition().y() * Settings.TILE_PIXEL();
    }

    @Override
    protected synchronized void paintComponent(final Graphics g) {
        super.paintComponent(g);
        this.requestFocusInWindow();
        for (int x = 0; x < Settings.MAP_WIDTH(); x++) {
            for (int y = 0; y < Settings.MAP_HEIGHT(); y++) {
                if (!(this.gameMap.map()[x][y] instanceof Building) ||
                        (this.gameMap.map()[x][y] instanceof Building
                                && (((Building) this.gameMap.map()[x][y]).topLeftCoordinate().x() == x)
                                && (((Building) this.gameMap.map()[x][y])).topLeftCoordinate().y() == y)) {
                    g.drawImage(LoadImage.load(this.gameMap.map()[x][y].image()),
                            ((x * Settings.TILE_PIXEL()) - this.currentX) + Settings.FRAME_WIDTH() / 2 ,
                            ((y  * Settings.TILE_PIXEL()) - this.currentY) + Settings.FRAME_WIDTH() / 2 ,
                            null);
                }
            }
        }
        g.drawImage(LoadImage.load(this.gameController.trainerSprite()),
                Settings.FRAME_WIDTH() / 2,
                Settings.FRAME_WIDTH() / 2,
                null);
    }

    public synchronized void updateCurrentX(double x) {
        this.currentX = (int)(x * Settings.TILE_PIXEL());
    }

    public synchronized void updateCurrentY(double y) {
        this.currentY = (int)(y * Settings.TILE_PIXEL());
    }
}
