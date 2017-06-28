package view;
import controller.GameKeyListener;
import controller.GameViewObserver;
import model.game.Model;
import model.map.GameMap;
import utilities.Settings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class GamePanel extends JPanel {

    private Model model;
    private GameKeyListener keyListener;
    private GameMap gameMap;

    public GamePanel(final GameViewObserver gameController, final GameMap gameMap) {
        this.gameMap = gameMap;
        this.setFocusable(true);
        this.requestFocusInWindow();
        this.keyListener = new GameKeyListener(gameController);
        this.addKeyListener(this.keyListener);
    }

    @Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);
        setOpaque(false);
        this.requestFocusInWindow();
        for (int x = 0; x < Settings.MAP_WIDTH(); x++) {
            for (int y = 0; y < Settings.MAP_HEIGHT(); y++) {
                g.drawImage(LoadImage.load(this.gameMap.map()[x][y].image()),x*32,y*32,null);
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
