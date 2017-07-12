package view;

import controller.SignInController;
import model.entities.Trainer1;
import utilities.Settings;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class SignInPanel extends BasePanel {

    private SignInController controller;
    private String trainerImage;

    public SignInPanel(SignInController controller) {
        this.controller = controller;
        this.imagePanel = LoadImage.load(Settings.PANELS_FOLDER() + "sign-in.png");

        this.trainerImage = new Trainer1().frontS().image();
        JLabel label = new JLabel("", new ImageIcon(LoadImage.load(this.trainerImage)), JLabel.CENTER);
        JPanel trainerImagePanel = new JPanel(new BorderLayout());
        trainerImagePanel.add( label, BorderLayout.CENTER );
        trainerImagePanel.setOpaque(false);

        JComboBox<Trainers> trainersBox = new JComboBox<>();
        trainersBox.setMaximumRowCount(5);

        for(Trainers trainer : Trainers.values()) {
            trainersBox.addItem(trainer);
        }
        trainersBox.setSelectedIndex(0);

        JButton submit  = new JButton(Settings.SUBMIT_BUTTON());
        Map<String,JTextField> accountData = new HashMap<>();

        for(AccountData data : AccountData.values()) {
            k.gridx = 0;
            this.centralPanel.add(new JLabel(data.toString()), k);
            k.gridx++;
            JTextField textField = new JTextField(20);
            this.centralPanel.add(textField,k);
            k.gridy++;
            accountData.put(data.toString(),textField);
        }
        k.gridx = 0;
        this.centralPanel.add(new JLabel("Trainer"), k);
        k.gridx++;
        this.centralPanel.add(trainersBox, k);
        k.gridy++;
        this.centralPanel.add(trainerImagePanel, k);
        k.gridy++;
        this.centralPanel.add(submit, k);
/*
        trainersBox.addActionListener(e -> {

           //Enumeration.Value trainer = (Enumeration.Value)((JComboBox)e.getSource()).getSelectedItem();

            this.trainerImage = new Trainer1().frontS().image();

            label.setIcon(new ImageIcon(LoadImage.load(this.trainerImage)));
        });
*/
        submit.addActionListener(e -> this.controller.signIn(accountData));
        this.backButton.addActionListener(e -> this.controller.back());

    }
}
