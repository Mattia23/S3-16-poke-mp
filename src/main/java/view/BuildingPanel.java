package view;

import controller.Controller;
import controller.GameViewObserver;
import model.environment.CoordinateImpl;
import model.environment.EdificeMap;
import model.environment.LaboratoryMap;
import model.environment.PokemonCenterMap;
import utilities.Settings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class BuildingPanel extends JPanel implements KeyListener{
    private Controller controller;
    private int centerX;
    private int centerY;
    private EdificeMap edificeMap;
    private DialoguePanel dialoguePanel;
    private Semaphore semaphore = new Semaphore(0);
    private int up = 0;

    public BuildingPanel(GameViewObserver gameController) {
        this.setFocusable(true);
        this.requestFocusInWindow();
        /*this.keyListener = new GameKeyListener(gameController);
        this.addKeyListener(this.keyListener);*/
        this.addKeyListener(this);
        this.edificeMap = new PokemonCenterMap();
        centerX = (Settings.SCREEN_WIDTH()/3 - edificeMap.image().getWidth(null))/2;
        centerY = (Settings.SCREEN_WIDTH()/3 - edificeMap.image().getHeight(null))/2;
        this.setLayout(new BorderLayout());
        if(edificeMap instanceof LaboratoryMap){
            dialoguePanel = new DialoguePanel(semaphore, Settings.OK_BUTTON());
        }else{
            dialoguePanel = new DialoguePanel(semaphore, Settings.YES_NO_BUTTON());
        }
        dialoguePanel.setVisible(false);
        this.add(dialoguePanel, BorderLayout.SOUTH);
        dialoguePanel.setPreferredSize(new Dimension(0, Settings.SCREEN_WIDTH()/12));

    }

    @Override
    protected void paintComponent(final Graphics g) {
        setOpaque(false);
        this.requestFocusInWindow();
        g.drawImage(this.edificeMap.image(), centerX, centerY, this);

        if(edificeMap.userCoordinate().y()>edificeMap.npcCoordinate().y()){
            g.drawImage(edificeMap.npc().image(), centerX+(edificeMap.npcCoordinate().x())* Settings.TILE_PIXEL(),
                    centerY+(edificeMap.npcCoordinate().y())*Settings.TILE_PIXEL() -
                            (edificeMap.npc().HEIGHT()-Settings.TILE_PIXEL()), this);
            g.drawImage(LoadImage.load("/images/characters/oak.png"), centerX+(edificeMap.userCoordinate().x())* Settings.TILE_PIXEL(),
                    centerY+(edificeMap.userCoordinate().y())*Settings.TILE_PIXEL()-12, this);
        }else{
            g.drawImage(LoadImage.load("/images/characters/oak.png"), centerX+(edificeMap.userCoordinate().x())* Settings.TILE_PIXEL(),
                    centerY+(edificeMap.userCoordinate().y())*Settings.TILE_PIXEL()-12, this);
            g.drawImage(edificeMap.npc().image(), centerX+(edificeMap.npcCoordinate().x())* Settings.TILE_PIXEL(),
                    centerY+(edificeMap.npcCoordinate().y())*Settings.TILE_PIXEL() -
                            (edificeMap.npc().HEIGHT()-Settings.TILE_PIXEL()), this);
        }

        super.paintComponent(g);
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        try {
            if (e.getKeyCode() == KeyEvent.VK_UP) {
                if (edificeMap.map()[edificeMap.userCoordinate().x()][edificeMap.userCoordinate().y() - 1].walkable()) {
                    edificeMap.userCoordinate_$eq(new CoordinateImpl(edificeMap.userCoordinate().x(), edificeMap.userCoordinate().y() - 1));
                    this.repaint();
                }
            } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                if (edificeMap.userCoordinate().equals(edificeMap.entryCoordinate())) {
                    System.out.println("Sei uscito!");
                } else if (edificeMap.map()[edificeMap.userCoordinate().x()][edificeMap.userCoordinate().y() + 1].walkable()) {
                    edificeMap.userCoordinate_$eq(new CoordinateImpl(edificeMap.userCoordinate().x(), edificeMap.userCoordinate().y() + 1));
                    this.repaint();
                }
            } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                if (edificeMap.map()[edificeMap.userCoordinate().x() - 1][edificeMap.userCoordinate().y()].walkable()) {
                    edificeMap.userCoordinate_$eq(new CoordinateImpl(edificeMap.userCoordinate().x() - 1, edificeMap.userCoordinate().y()));
                    this.repaint();
                }
            } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                if (edificeMap.map()[edificeMap.userCoordinate().x() + 1][edificeMap.userCoordinate().y()].walkable()) {
                    edificeMap.userCoordinate_$eq(new CoordinateImpl(edificeMap.userCoordinate().x() + 1, edificeMap.userCoordinate().y()));
                    this.repaint();
                }
            } else if (e.getKeyCode() == KeyEvent.VK_Z) {
                //TODO dovrà essere controllata anche la direzione del personaggio
                if (edificeMap.userCoordinate().x() == edificeMap.npcCoordinate().x() &&
                        edificeMap.userCoordinate().y() - 1 == edificeMap.npcCoordinate().y()) {
                    speak();
                }
            }
        }catch (ArrayIndexOutOfBoundsException exception){
            //exception.printStackTrace();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    private void speak(){
        dialoguePanel.setText(edificeMap.npc().dialogue());
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
