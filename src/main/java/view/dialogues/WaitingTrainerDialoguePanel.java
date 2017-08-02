package view.dialogues;

import java.util.ArrayList;
import java.util.Arrays;

public class WaitingTrainerDialoguePanel extends DialoguePanel {

    public WaitingTrainerDialoguePanel(String trainerName) {
        super(new ArrayList<>(Arrays.asList("Waiting for an answer from " + trainerName)));
        setFinalButtons();
    }

    @Override
    protected void setFinalButtons() {
        buttonPanel.removeAll();
        buttons.clear();
        repaint();
    }
}
