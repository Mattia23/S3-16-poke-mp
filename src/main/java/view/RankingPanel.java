package view;

import controller.GameController;
import controller.GameMenuController;
import database.remote.DBConnect;
import scala.Tuple3;
import utilities.Settings;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.List;

public class RankingPanel extends BasePanel {

    public RankingPanel(GameMenuController gameMenuController, GameController gameController) {
        this.imagePanel = LoadImage.load(Settings.PANELS_FOLDER() + "pikachu.jpg");
        final JPanel mainPanel = new JPanel(new GridLayout(0,1));
        mainPanel.setOpaque(false);
        final JScrollPane scrollFrame = new JScrollPane(mainPanel,ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        mainPanel.setAutoscrolls(true);
        add(scrollFrame);

        List<Tuple3<String,Integer,String>> trainers = DBConnect.getRanking().get();

        int i = 1;
        for(Tuple3<String,Integer,String> object: trainers){
            final JPanel trainerPanel = new JPanel(new GridLayout(0,2));
            final JLabel info = new JLabel( i++ + "   " + object._1() + "  Level: "+calculateLevel(object._2()) + "  Exp Points: "+object._2());
            Image myImage;
            ImageIcon myImageIcon = null;
            int imgId = Integer.parseInt(object._3()) + 1;
            try {
                myImage = ImageIO.read(getClass().getResource(Settings.TRAINER_IMAGES_FOLDER() + imgId +"FS.png"));
                myImageIcon = new ImageIcon(myImage.getScaledInstance(32,32,java.awt.Image.SCALE_SMOOTH));
            } catch (IOException e) {
                e.printStackTrace();
            }
            trainerPanel.setOpaque(false);
            trainerPanel.add(info);
            trainerPanel.add(new JLabel(myImageIcon));
            if(object._1().equals(gameController.trainer().name())){
                setFontBold(info);
            }else{
                setFont(info);
            }
            mainPanel.add(trainerPanel);
        }

        this.backButton.addActionListener(e -> {
            gameController.resume();
            gameController.pause();
            gameMenuController.showGameMenu();
        });

    }

    private void setFontBold(final JLabel label){
        label.setFont(new Font("Verdana", Font.BOLD, 12));
    }

    private void setFont(final JLabel label){
        label.setFont(new Font("Verdana", Font.PLAIN, 12));
    }

    private int calculateLevel(int experiencePoints) {
        double level = Settings.INITIAL_TRAINER_LEVEL();
        double step = Settings.LEVEL_STEP();
        while(experiencePoints > step ){
            step = step + Settings.LEVEL_STEP() * Math.pow(2, level);
            level ++;
        }
        return (int) level;
    }
}