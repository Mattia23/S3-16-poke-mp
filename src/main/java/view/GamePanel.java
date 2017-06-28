package view;
import controller.GameKeyListener;
import controller.GameViewObserver;
import model.game.Model;
import model.map.Building;
import model.map.GameMap;
import utilities.Settings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class GamePanel extends JPanel {

    private Model model;
    private GameKeyListener keyListener;
    private GameMap gameMap;
    private int panelCenterX;
    private int panelCenterY;

    public GamePanel(final GameViewObserver gameController, final GameMap gameMap) {
        this.gameMap = gameMap;
        this.setFocusable(true);
        this.requestFocusInWindow();
        this.keyListener = new GameKeyListener(gameController);
        this.addKeyListener(this.keyListener);
        this.panelCenterX = (Settings.FRAME_WIDTH() - Settings.MAP_WIDTH())/2;
        this.panelCenterY = (Settings.FRAME_HEIGHT() - Settings.MAP_HEIGHT())/2;
    }

    @Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);
        this.requestFocusInWindow();
        for (int x = 0; x < Settings.MAP_WIDTH(); x++) {
            for (int y = 0; y < Settings.MAP_HEIGHT(); y++) {
                if (!(this.gameMap.map()[x][y] instanceof Building) ||
                        (this.gameMap.map()[x][y] instanceof Building
                                && (((Building)this.gameMap.map()[x][y]).topLeftCoordinate().x() == x)
                                && (((Building)this.gameMap.map()[x][y])).topLeftCoordinate().y() == y)) {
                    g.drawImage(LoadImage.load(this.gameMap.map()[x][y].image()),x*Settings.TILE_PIXEL() - panelCenterX,y*Settings.TILE_PIXEL() - panelCenterY,null);
                }
            }
        }
    }

/*
    public void drawModel(final Model model) {
        this.model = model;
        this.requestFocusInWindow();
        SwingUtilities.invokeLater(() -> super.repaint());
        this.keyListener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "Aggiorna il protagonista"));
    }
*/
}
