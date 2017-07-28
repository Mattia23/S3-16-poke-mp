package view;

import controller.GameController;

import javax.swing.*;
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
        buttonPanel.removeAll();
        buttons.clear();
        buttons.add(new JButton("bye"));
        buttons.get(0).addActionListener(e -> {
            response = buttons.get(0).getText();
            gameController.setFocusableOn();
            this.setVisible(false);
        });
        buttonPanel.add(buttons.get(0));
        buttons.get(0).requestFocus();
        JUtil.setFocus(buttons.get(0));
        repaint();
    }
}
