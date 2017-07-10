package view;

import controller.Controller;
import controller.SignInController;
import database.remote.DBConnect;
import utilities.Settings;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public class SignInPanel extends BasePanel {

    private SignInController controller;

    public SignInPanel(SignInController controller) {
        this.controller = controller;
        this.imagePanel = LoadImage.load(Settings.PANELS_FOLDER() + "sign-in.png");
        this.backButton.addActionListener(e -> this.controller.back());
        JButton submit  = new JButton(Settings.SUBMIT_BUTTON());
        Map<String,JTextField> accountData = new HashMap<>();

        for(AccountData data : AccountData.values()) {
            this.centralPanel.add(new JLabel(data.toString()), k);
            k.gridy++;
            JTextField textField = new JTextField(20);
            this.centralPanel.add(textField,k);
            k.gridy++;
            accountData.put(data.toString(),textField);
        }
        this.centralPanel.add(submit, k);

        submit.addActionListener(e -> this.controller.signIn(accountData));

    }

}
