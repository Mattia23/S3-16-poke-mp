package view;

import controller.GameController;
import database.remote.DBConnect;
import utilities.Settings;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import java.util.List;

public class DoctorDialoguePanel extends DialoguePanel {
    private GameController gameController;

    public DoctorDialoguePanel(GameController gameController, List<String> dialogues) {
        super(dialogues);
        this.gameController = gameController;
        if(dialogues.size() == 1) setFinalButtons();
    }

    @Override
    protected void setFinalButtons() {
        buttonPanel.removeAll();
        buttons.clear();
        for(String text: Settings.DOCTOR_DIALOGUE_BUTTON()){
            final JButton button = new JButton(text);
            button.addKeyListener(this);
            button.addActionListener(e ->{
                gameController.resume();
                this.setVisible(false);
            });
            buttonPanel.add(button);
            buttons.add(button);
        }
        buttons.get(currentButton).requestFocus();
        buttons.get(0).addActionListener(e -> DBConnect.rechangeAllTrainerPokemon(gameController.trainer().id()));
    }
}
