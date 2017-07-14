package view;

import controller.GameControllerImpl;
import database.remote.DBConnect;
import utilities.Settings;

import javax.swing.*;
import java.util.List;

public class DoctorDialoguePanel extends DialoguePanel {
    private GameControllerImpl gameController;

    public DoctorDialoguePanel(GameControllerImpl gameController, List<String> dialogues) {
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
        buttons.get(0).addActionListener(e -> DBConnect.rechangeAllTrainerPokemon(gameController.trainer().id()));
    }
}
