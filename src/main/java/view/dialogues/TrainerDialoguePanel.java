package view.dialogues;

import controller.GameController;
import distributed.client.TrainerDialogueClientManager;
import utilities.Settings;
import view.JUtil;

import javax.swing.*;
import java.util.List;

public class TrainerDialoguePanel extends DialoguePanel {
    private GameController gameController;
    private TrainerDialogueClientManager trainerDialogueClientManager;
    private Boolean running = true;

    public TrainerDialoguePanel(GameController gameController, TrainerDialogueClientManager trainerDialogueClientManager, List<String> dialogues) {
        super(dialogues);
        this.gameController = gameController;
        this.trainerDialogueClientManager = trainerDialogueClientManager;
        if(dialogues.size() == 1) setFinalButtons();
        final Thread countDown = new Thread(() -> {
            int i = Settings.Constants$.MODULE$.SECONDS_WAITING_TIME_FIGHT_REQUEST();
            while (running) {
                buttons.get(1).setText(Settings.Strings$.MODULE$.TRAINER_DIALOGUE_BUTTON().get(1) + "(" + i + ")");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                i--;
                if (i == 0) buttons.get(1).doClick();
            }
        });
        countDown.start();
    }

    @Override
    protected void setFinalButtons() {
        buttonPanel.removeAll();
        buttons.clear();
        for(String text: Settings.Strings$.MODULE$.TRAINER_DIALOGUE_BUTTON()){
            final JButton button = new JButton(text);
            button.addKeyListener(this);
            button.addActionListener(e ->{
                running = false;
                this.setVisible(false);
                gameController.setFocusableOn();
            });
            buttonPanel.add(button);
            buttons.add(button);
        }
        buttons.get(0).addActionListener(e ->{
           trainerDialogueClientManager.sendDialogueRequest(trainerDialogueClientManager.otherPlayerId(), true, false);
           trainerDialogueClientManager.createBattle();
        });
        buttons.get(1).addActionListener(e ->{
            trainerDialogueClientManager.sendDialogueRequest(trainerDialogueClientManager.otherPlayerId(), false, false);
        });
        buttons.get(currentButton).requestFocus();
        JUtil.setFocus(buttons.get(currentButton));
    }
}
