package view;

import controller.GameController;
import distributed.client.TrainerDialogueClientManager;
import utilities.Settings;

import javax.swing.*;
import java.util.List;

public class TrainerDialoguePanel extends DialoguePanel {
    private GameController gameController;
    private TrainerDialogueClientManager trainerDialogueClientManager;
    private Thread countDown;

    public TrainerDialoguePanel(GameController gameController, TrainerDialogueClientManager trainerDialogueClientManager, List<String> dialogues) {
        super(dialogues);
        this.gameController = gameController;
        this.trainerDialogueClientManager = trainerDialogueClientManager;
        if(dialogues.size() == 1) setFinalButtons();
        countDown = new Thread(() -> {
            for(int i = 15; i > 0; i--){
                buttons.get(1).setText(Settings.TRAINER_DIALOGUE_BUTTON().get(1) + "(" + i + ")");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            buttons.get(1).doClick();
        });
        countDown.start();
    }

    @Override
    protected void setFinalButtons() {
        buttonPanel.removeAll();
        buttons.clear();
        for(String text: Settings.TRAINER_DIALOGUE_BUTTON()){
            final JButton button = new JButton(text);
            button.addKeyListener(this);
            button.addActionListener(e ->{
                countDown.interrupt();
                this.setVisible(false);
                gameController.setFocusableOn();
            });
            buttonPanel.add(button);
            buttons.add(button);
        }
        buttons.get(currentButton).requestFocus();
        buttons.get(0).addActionListener(e ->{
            trainerDialogueClientManager.sendDialogueRequest(trainerDialogueClientManager.player(), trainerDialogueClientManager.otherPlayer(), true);
            //TODO PARTE LA SFIDA
        });
        buttons.get(1).addActionListener(e ->{
            trainerDialogueClientManager.sendDialogueRequest(trainerDialogueClientManager.player(), trainerDialogueClientManager.otherPlayer(), false);
        });
    }
}
