package view;

import controller.Controller;
import database.DBConnect;
import utilities.Settings;

import javax.swing.*;
import java.awt.*;

public class LoginPanel extends BasePanel {
    private View parentView;
    private Controller controller;
    private JLabel usernameLabel;
    private JLabel passwordLabel;
    private JTextField usernameField;
    private JTextField passwordField;
    private JButton submit;


    public LoginPanel(View view, Controller controller) {
        this.parentView = view;
        this.controller = controller;
        this.usernameLabel = new JLabel("Username");
        this.passwordLabel = new JLabel("Password");
        this.usernameField = new JTextField(20);
        this.passwordField = new JTextField(20);
        this.submit = new JButton("Submit");
        this.imagePanel = LoadImage.load(Settings.PANELS_FOLDER() + "log-in.png");
        this.backButton.addActionListener(e -> this.parentView.showMenu());
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
        this.submit.addActionListener(e -> {
            DBConnect connect = new DBConnect();
            if(!connect.checkCredentials(usernameField.getText(),passwordField.getText())) {
                JOptionPane.showMessageDialog(this,"Wrong username or password","LOGIN FAILED",JOptionPane.ERROR_MESSAGE);
            }
        });
    }

}
