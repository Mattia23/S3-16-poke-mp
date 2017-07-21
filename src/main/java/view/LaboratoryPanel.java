package view;

import controller.GameController;
import model.map.BuildingMap;
import utilities.Settings;

import java.awt.*;

public class LaboratoryPanel extends BuildingPanel {

    private boolean emptyCaptures;
    private BuildingMap laboratoryMap;

    public LaboratoryPanel(final GameController gameController, final BuildingMap laboratoryMap, final boolean emptyCaptures) {
        super(gameController, laboratoryMap);
        this.laboratoryMap = laboratoryMap;
        this.emptyCaptures = emptyCaptures;
    }

    @Override
    protected synchronized void doPaint(final Graphics g){
        super.doPaint(g);
        if(emptyCaptures){
            for(int i = 0; i < laboratoryMap.pokemonNpc().length(); i++){
                g.drawImage(laboratoryMap.pokemonNpc().apply(i).image(),
                        super.centerX + laboratoryMap.pokemonNpc().apply(i).coordinate().x()* Settings.TILE_PIXEL(),
                        super.centerY + laboratoryMap.pokemonNpc().apply(i).coordinate().y()*Settings.TILE_PIXEL(), this);
            }
        }
    }
}
