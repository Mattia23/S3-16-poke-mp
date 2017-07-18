package view;

import controller.GameController;
import controller.UsersTrainerSpritesMapImpl;
import distributed.ConnectedUsersImpl;
import distributed.User;
import model.map.GameMap;
import utilities.Settings;

import java.awt.*;

public class DistributedMapPanel extends MapPanel{

    public DistributedMapPanel(GameController gameController, GameMap gameMap) {
        super(gameController, gameMap);
    }

    @Override
    protected void doPaint(final Graphics g) {
        super.doPaint(g);
        if(!UsersTrainerSpritesMapImpl.map().isEmpty()){
            for(User user: ConnectedUsersImpl.map().values()){
                System.out.println("x: "+ user.position().x() + " y: "+user.position().y());
                g.drawImage(LoadImage.load((UsersTrainerSpritesMapImpl.map().get(user.userId()))),
                        (( user.position().x() * Settings.TILE_PIXEL()) - super.getCurrentX()) + Settings.FRAME_SIDE() / 2 ,
                        (( user.position().y() * Settings.TILE_PIXEL()) - super.getCurrentY()) + Settings.FRAME_SIDE() / 2 ,
                        null);
            }
        }
    }

}
