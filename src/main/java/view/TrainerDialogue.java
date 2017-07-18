package view;

import controller.GameController;
import utilities.Settings;

import javax.swing.*;
import java.util.List;

public class TrainerDialogue extends DialoguePanel {
    private GameController gameController;

    public TrainerDialogue(GameController gameController, List<String> dialogues) {
        super(dialogues);
        this.gameController = gameController;
        if(dialogues.size() == 1) setFinalButtons();
    }

    @Override
    protected void setFinalButtons() {
        buttonPanel.removeAll();
        buttons.clear();
        for(String text: Settings.TRAINER_DIALOGUE_BUTTON()){
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
    }
}
