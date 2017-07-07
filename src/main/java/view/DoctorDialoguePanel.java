package view;

import controller.GameController;
import model.entities.Pokemon;
import utilities.Settings;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class DoctorDialoguePanel extends DialoguePanel {
    private GameController gameController;

    public DoctorDialoguePanel(GameController gameController, List<String> dialogues) {
        super(dialogues);
        this.gameController = gameController;
        this.gameController.gamePanel().setFocusable(false);
        if(dialogues.size() == 1) setFinalButtons();
    }

    @Override
    protected void setFinalButtons() {
        buttonPanel.removeAll();
        buttons.clear();
        for(String text: Settings.DOCTOR_DIALOGUE()){
            final JButton button = new JButton(text);
            button.addKeyListener(this);
            button.addActionListener(e ->{
                gameController.gamePanel().setFocusable(true);
                this.setVisible(false);
            });
            buttonPanel.add(button);
            buttons.add(button);
        }
        buttons.get(currentButton).requestFocus();
        buttons.get(0).addActionListener(e ->{
            /*TODO cura tutti i pokemon della squadra
            for(Object pokemon: gameController.trainer().favouritePokemons()){

            }*/

        });
    }
}
