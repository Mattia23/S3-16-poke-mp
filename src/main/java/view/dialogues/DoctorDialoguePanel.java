package view.dialogues;

import controller.GameController;
import database.remote.DBConnect;
import model.entities.Doctor$;
import model.environment.AudioImpl;
import utilities.Settings;

import javax.swing.*;
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
        for(String text: Settings.Strings$.MODULE$.DOCTOR_DIALOGUE_BUTTON()){
            final JButton button = new JButton(text);
            button.addKeyListener(this);
            buttonPanel.add(button);
            buttons.add(button);
        }
        buttons.get(currentButton).requestFocus();
        buttons.get(0).addActionListener(e -> {
            new AudioImpl(Settings.Audio$.MODULE$.HEALING_SOUND()).play();
            DBConnect.rechangeAllTrainerPokemon(gameController.trainer().id());
            dialogueLabel.setText(Doctor$.MODULE$.DIALOGUE_AFTER_HEAL());
            buttonPanel.removeAll();
            buttons.clear();
            final JButton button = new JButton("bye");
            button.addActionListener(e2 -> gameController.resume());
            button.addKeyListener(this);
            buttonPanel.add(button);
            buttons.add(button);
            button.requestFocus();
            repaint();
        });
        buttons.get(1).addActionListener(e -> {
            this.setVisible(false);
            gameController.setFocusableOn();
        });

        repaint();
    }
}
