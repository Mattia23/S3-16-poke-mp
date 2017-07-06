package view;

import controller.GameViewObserver;
import model.environment.BuildingMap;
import utilities.Settings;

import java.awt.*;

public class BuildingPanel extends GamePanel {
    private int centerX;
    private int centerY;
    private BuildingMap buildingMap;
    private GameViewObserver gameController;
    private DialoguePanel dialoguePanel;

    public BuildingPanel(final GameViewObserver gameController, final BuildingMap buildingMap) {
        super(gameController);

        this.buildingMap = buildingMap;
        this.gameController = gameController;

        centerX = (Settings.SCREEN_WIDTH()/3 - buildingMap.image().getWidth(null))/2;
        centerY = (Settings.SCREEN_WIDTH()/3 - buildingMap.image().getHeight(null))/2;
        /*this.currentX = buildingMap.entryCoordinate().x() * Settings.TILE_PIXEL();
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
    protected synchronized void doPaint(final Graphics g) {
        g.drawImage(this.buildingMap.image(), centerX, centerY, this);

        g.drawImage(LoadImage.load(this.gameController.trainerSprite()), centerX+super.getCurrentX(), centerY+super.getCurrentY(), this);
        g.drawImage(buildingMap.npc().image(), centerX+(buildingMap.npc().coordinate().x())* Settings.TILE_PIXEL(),
                centerY+(buildingMap.npc().coordinate().y())*Settings.TILE_PIXEL() -
                        (buildingMap.npc().HEIGHT()-Settings.TILE_PIXEL()), this);

    }
/*
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

    }*/
}
