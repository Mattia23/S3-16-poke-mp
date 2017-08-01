package view;

import controller.GameKeyListener;
import controller.GameController;
import utilities.Settings;

import javax.swing.*;
import java.awt.*;

public abstract class GamePanel extends JPanel{

    private int currentX;
    private int currentY;

    protected GamePanel(final GameController gameController) {
        this.setFocusable(true);
        this.requestFocusInWindow();
        GameKeyListener keyListener = new GameKeyListener(gameController);
        this.addKeyListener(keyListener);
        this.currentX = gameController.trainer().coordinate().x() * Settings.Constants$.MODULE$.TILE_PIXEL();
        this.currentY = gameController.trainer().coordinate().y() * Settings.Constants$.MODULE$.TILE_PIXEL();
    }

    @Override
    protected final synchronized void paintComponent(final Graphics g) {
        super.paintComponent(g);
        this.requestFocusInWindow();
        this.doPaint(g);
    }

    protected abstract void doPaint(final Graphics g);

    public synchronized void updateCurrentX(double x) {
        this.currentX = (int)(x * Settings.Constants$.MODULE$.TILE_PIXEL());
    }

    public synchronized void updateCurrentY(double y) {
        this.currentY = (int)(y * Settings.Constants$.MODULE$.TILE_PIXEL());
    }

    protected int getCurrentX() {
        return this.currentX;
    }

    protected int getCurrentY() {
        return this.currentY;
    }


}
