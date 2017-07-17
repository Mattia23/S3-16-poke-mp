package view;

import controller.GameController;
import controller.GameControllerImpl;

import java.util.List;

public class TrainerDialogue extends DialoguePanel {


    public TrainerDialogue(GameController gameController, List<String> dialogues) {
        super(dialogues);
    }

    @Override
    protected void setFinalButtons() {

    }
}
