package view;
import controller.GameKeyListener;
import controller.GameViewObserver;
import model.environment.Coordinate;
import model.map.Building;
import model.map.GameMap;
import utilities.Settings;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel{

    private GameMap gameMap;
    private Coordinate currentPosition;

    public GamePanel(final GameViewObserver gameController, final GameMap gameMap) {
        this.gameMap = gameMap;
        this.setFocusable(true);
        this.requestFocusInWindow();
        GameKeyListener keyListener = new GameKeyListener(gameController);
        this.addKeyListener(keyListener);
        this.currentPosition = gameController.trainerPosition();
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
                    g.drawImage(LoadImage.load(this.gameMap.map()[x][y].image()), x * Settings.TILE_PIXEL() - this.currentPosition.x(), y * Settings.TILE_PIXEL() - this.currentPosition.y(), null);
                }
            }
        }
    }

    public synchronized void  updateCurrentPosition(Coordinate position) {
        this.currentPosition = position;
    }
}
