package view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WaitingTrainerDialoguePanel extends DialoguePanel {

    public WaitingTrainerDialoguePanel(String trainerName) {
        super(new ArrayList<String>(Arrays.asList("Waiting for an answer from " + trainerName)));
        setFinalButtons();
    }

    @Override
    protected void setFinalButtons() {
        buttonPanel.removeAll();
        buttons.clear();
        repaint();
    }
}
