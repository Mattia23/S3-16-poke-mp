package view;

import controller.GameController;
import controller.GameControllerImpl;

import java.util.List;

public class ClassicDialoguePanel extends DialoguePanel {
    private GameController gameController;

    public ClassicDialoguePanel(final GameController gameController, final List<String> dialogues) {
        super(dialogues);
        this.gameController = gameController;
        if(dialogues.size() == 1) setFinalButtons();
    }

    @Override
    protected void setFinalButtons() {
        buttons.get(0).setText("bye");
        buttons.get(0).addActionListener(e -> {
            response = buttons.get(0).getText();
            gameController.resume();
            this.setVisible(false);
        });
    }
}
