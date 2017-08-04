package view.dialogue;

import utilities.Settings;

import java.util.ArrayList;
import java.util.Collections;

public class WaitingTrainerDialoguePanel extends DialoguePanel {

    public WaitingTrainerDialoguePanel(String trainerName) {
        super(new ArrayList<>(Collections.singletonList(Settings.Strings$.MODULE$.WAITING_TRAINER_LABEL() + trainerName)));
    }

    @Override
    protected void setFinalButtons() {
        buttonPanel.removeAll();
        buttons.clear();
        repaint();
    }
}
