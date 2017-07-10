package view;

import controller.LoginController;
import utilities.Settings;

import javax.swing.*;
import java.awt.*;

public class LoginPanel extends BasePanel {
   // private View parentView;
    private LoginController controller;
   // private DBController dbController;
    private JLabel usernameLabel;
    private JLabel passwordLabel;
    private JTextField usernameField;
    private JTextField passwordField;
    private JButton submit;


    public LoginPanel(LoginController controller) {
       // this.parentView = view;
        this.controller = controller;
        this.usernameLabel = new JLabel("Username");
        this.passwordLabel = new JLabel("Password");
        this.usernameField = new JTextField(20);
        this.passwordField = new JTextField(20);
        this.submit = new JButton(Settings.SUBMIT_BUTTON());
        this.imagePanel = LoadImage.load(Settings.PANELS_FOLDER() + "log-in.png");
        this.backButton.addActionListener(e -> this.controller.back());
        this.usernameLabel.setForeground(Color.WHITE);
        this.passwordLabel.setForeground(Color.WHITE);

        this.centralPanel.add(usernameLabel, k);
        k.gridy++;
        this.centralPanel.add(usernameField, k);
        k.gridy++;
        this.centralPanel.add(passwordLabel, k);
        k.gridy++;
        this.centralPanel.add(passwordField, k);
        k.gridy++;
        this.centralPanel.add(submit,k);
        this.submit.addActionListener(e -> this.controller.login(usernameField.getText(), passwordField.getText()));
    }

}
