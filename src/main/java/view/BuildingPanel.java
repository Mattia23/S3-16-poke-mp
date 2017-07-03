package view;

import controller.Controller;
import controller.GameKeyListener;
import controller.GameViewObserver;
import model.environment.CoordinateImpl;
import model.environment.BuildingMap;
import model.environment.LaboratoryMap;
import model.environment.PokemonCenterMap;
import utilities.Settings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.concurrent.Semaphore;

public class BuildingPanel extends JPanel {
    private int centerX;
    private int centerY;
    private int currentX;
    private int currentY;
    private BuildingMap buildingMap;
    private DialoguePanel dialoguePanel;
    private Semaphore semaphore = new Semaphore(0);

    public BuildingPanel(GameViewObserver gameController) {
        this.setFocusable(true);
        this.requestFocusInWindow();
        GameKeyListener keyListener = new GameKeyListener(gameController);
        this.addKeyListener(keyListener);

        this.buildingMap = new LaboratoryMap();
        centerX = (Settings.SCREEN_WIDTH()/3 - buildingMap.image().getWidth(null))/2;
        centerY = (Settings.SCREEN_WIDTH()/3 - buildingMap.image().getHeight(null))/2;
        this.currentX = buildingMap.entryCoordinate().x() * Settings.TILE_PIXEL();
        this.currentY = buildingMap.entryCoordinate().y() * Settings.TILE_PIXEL();
        /*this.setLayout(new BorderLayout());
        if(buildingMap instanceof LaboratoryMap){
            dialoguePanel = new DialoguePanel(semaphore, Settings.OK_BUTTON());
        }else{
            dialoguePanel = new DialoguePanel(semaphore, Settings.YES_NO_BUTTON());
        }
        dialoguePanel.setVisible(false);
        this.add(dialoguePanel, BorderLayout.SOUTH);
        dialoguePanel.setPreferredSize(new Dimension(0, Settings.SCREEN_WIDTH()/12));*/

    }



    @Override
    protected synchronized void paintComponent(final Graphics g) {
        super.paintComponent(g);
        this.requestFocusInWindow();
        g.drawImage(this.buildingMap.image(), centerX, centerY, this);

        g.drawImage(LoadImage.load("/images/characters/charmander.png"), centerX+currentX, centerY+currentY, this);
        g.drawImage(buildingMap.npc().image(), centerX+(buildingMap.npc().coordinate().x())* Settings.TILE_PIXEL(),
                centerY+(buildingMap.npc().coordinate().y())*Settings.TILE_PIXEL() -
                        (buildingMap.npc().HEIGHT()-Settings.TILE_PIXEL()), this);

    }

    public synchronized void updateCurrentX(double x) {
        this.currentX = (int)(x * Settings.TILE_PIXEL());
    }

    public synchronized void updateCurrentY(double y) {
        this.currentY = (int)(y * Settings.TILE_PIXEL());
    }

    /*

    public void keyPressed(KeyEvent e) {
        try {
            if (e.getKeyCode() == KeyEvent.VK_UP) {
                if (buildingMap.map()[buildingMap.userCoordinate().x()][buildingMap.userCoordinate().y() - 1].walkable()) {
                    buildingMap.userCoordinate_$eq(new CoordinateImpl(buildingMap.userCoordinate().x(), buildingMap.userCoordinate().y() - 1));
                    this.repaint();
                }
            } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                if (buildingMap.userCoordinate().equals(buildingMap.entryCoordinate())) {
                    System.out.println("Sei uscito!");
                } else if (buildingMap.map()[buildingMap.userCoordinate().x()][buildingMap.userCoordinate().y() + 1].walkable()) {
                    buildingMap.userCoordinate_$eq(new CoordinateImpl(buildingMap.userCoordinate().x(), buildingMap.userCoordinate().y() + 1));
                    this.repaint();
                }
            } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                if (buildingMap.map()[buildingMap.userCoordinate().x() - 1][buildingMap.userCoordinate().y()].walkable()) {
                    buildingMap.userCoordinate_$eq(new CoordinateImpl(buildingMap.userCoordinate().x() - 1, buildingMap.userCoordinate().y()));
                    this.repaint();
                }
            } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                if (buildingMap.map()[buildingMap.userCoordinate().x() + 1][buildingMap.userCoordinate().y()].walkable()) {
                    buildingMap.userCoordinate_$eq(new CoordinateImpl(buildingMap.userCoordinate().x() + 1, buildingMap.userCoordinate().y()));
                    this.repaint();
                }
            } else if (e.getKeyCode() == KeyEvent.VK_Z) {
                //TODO dovrà essere controllata anche la direzione del personaggio
                if (buildingMap.userCoordinate().x() == buildingMap.npc.coordinate.x() &&
                        buildingMap.userCoordinate().y() - 1 == buildingMap.npc.coordinate.y()) {
                    speak();
                }
            }
        }catch (ArrayIndexOutOfBoundsException exception){
            //exception.printStackTrace();
        }
    }*/


    private void speak(){
        dialoguePanel.setText(buildingMap.npc().dialogue());
        dialoguePanel.setVisible(true);
        this.setFocusable(false);
        new Thread(() -> {
            try {
                semaphore.acquire();
                dialoguePanel.setVisible(false);
                setFocusable(true);
                System.out.println(dialoguePanel.getValue());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

    }
}
