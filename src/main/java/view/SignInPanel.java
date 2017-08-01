package view;

import controller.SignInController;
import model.entities.TrainerSprites$;
import model.entities.Trainers;
import utilities.Settings;
import scala.Enumeration.Value;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.*;

public class SignInPanel extends BasePanel {

    private final static int BASIC_TRAINER_ID = 0;
    private final static String TRAINER_TEXT = "Trainer";

    private SignInController controller;
    private String trainerImage;
    private Value trainer;

    public SignInPanel(SignInController controller) {
        this.controller = controller;
        this.imagePanel = LoadImage.load(Settings.PANELS_FOLDER() + "sign-in.png");

        this.trainerImage = TrainerSprites$.MODULE$.selectTrainerSprite(BASIC_TRAINER_ID).frontS().image();
        this.trainer = Trainers.Boy1();

        JLabel label = new JLabel("", new ImageIcon(LoadImage.load(this.trainerImage)), JLabel.CENTER);
        JPanel trainerImagePanel = new JPanel(new BorderLayout());
        trainerImagePanel.add( label, BorderLayout.CENTER );
        trainerImagePanel.setOpaque(false);

        JComboBox<Value> trainersBox = new JComboBox<>();
        trainersBox.setMaximumRowCount(5);
        for(Value trainer : Trainers.valueSetAsJavaList()) {
            trainersBox.addItem(trainer);
        }
        trainersBox.setSelectedIndex(0);
        trainersBox.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                trainersBox.showPopup();
            }
        });
        JUtil.setEscClick(trainersBox, this.backButton);

        JButton submit  = new JButton(Settings.SUBMIT_BUTTON());
        Map<String,JTextField> accountData = new HashMap<>();

        for(AccountData data : AccountData.values()) {
            k.gridx = 0;
            this.centralPanel.add(new JLabel(data.toString()), k);
            k.gridx++;
            JTextField textField = new JTextField(20);
            JUtil.setSubmitEnterClick(textField, submit);
            JUtil.setEscClick(textField, this.backButton);
            this.centralPanel.add(textField,k);
            k.gridy++;
            accountData.put(data.toString(),textField);
        }
        k.gridx = 0;
        this.centralPanel.add(new JLabel(TRAINER_TEXT), k);
        k.gridx++;
        this.centralPanel.add(trainersBox, k);
        k.gridy++;
        this.centralPanel.add(trainerImagePanel, k);
        k.gridy++;
        this.centralPanel.add(submit, k);

        trainersBox.addActionListener(e -> {

            this.trainer = (Value)((JComboBox)e.getSource()).getSelectedItem();

            this.trainerImage = TrainerSprites$.MODULE$.selectTrainerSprite(this.trainer.id()).frontS().image();

            label.setIcon(new ImageIcon(LoadImage.load(this.trainerImage)));
        });

        submit.addActionListener(e -> this.controller.signIn(accountData, this.trainer.id()));
        this.backButton.addActionListener(e -> this.controller.back());
        JUtil.setFocus(accountData.get(AccountData.Name.toString()));
        JUtil.setEnterClick(submit);
        JUtil.setEscClick(submit, this.backButton);
    }
}
