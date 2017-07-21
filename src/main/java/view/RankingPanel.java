package view;

import controller.GameController;
import controller.GameMenuController;
import scala.Tuple3;
import utilities.Settings;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RankingPanel extends BasePanel {

    public RankingPanel(GameMenuController gameMenuController, GameController gameController) {
        this.imagePanel = LoadImage.load(Settings.PANELS_FOLDER() + "pikachu.jpg");
        final JPanel mainPanel = new JPanel(new GridLayout(0,1));
        final JScrollPane scrollFrame = new JScrollPane(mainPanel,ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        mainPanel.setAutoscrolls(true);
        add(scrollFrame);

        //richiesta al db per la classifica
        List<Tuple3> trainers = new ArrayList<>();
        for(int i=0; i<100; i++){
            trainers.add(new Tuple3("gianni", "8", "immagine2"));
        }
        trainers.add(new Tuple3("gian", "8", "immagine2"));

        int i = 1;
        for(Tuple3 object: trainers){
            final JPanel trainerPanel = new JPanel(new GridLayout(0,5));
            final JLabel rank = new JLabel(""+i++);
            final JLabel name = new JLabel(object._1().toString());
            final JLabel level = new JLabel("Level: "+calculateLevel(Integer.parseInt(object._2().toString())));
            final JLabel exp = new JLabel("Experience Points: "+object._2().toString());
            Image myImage;
            ImageIcon myImageIcon = null;
            try {
                myImage = ImageIO.read(getClass().getResource(gameController.trainer().sprites().frontS().image()));
                myImageIcon = new ImageIcon(myImage.getScaledInstance(32,32,java.awt.Image.SCALE_SMOOTH));
            } catch (IOException e) {
                e.printStackTrace();
            }
            trainerPanel.add(rank);
            trainerPanel.add(new JLabel(myImageIcon));
            trainerPanel.add(name);
            trainerPanel.add(level);
            trainerPanel.add(exp);
            setFontBold(rank);
            if(object._1().toString().equals(gameController.trainer().name())){
                setFontBold(name);
                setFontBold(level);
                setFontBold(exp);
            }else{
                setFont(name);
                setFont(level);
                setFont(exp);
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
        label.setFont(new Font("Courier", Font.BOLD, 12));
    }

    private void setFont(final JLabel label){
        label.setFont(new Font("Courier", Font.PLAIN, 12));
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