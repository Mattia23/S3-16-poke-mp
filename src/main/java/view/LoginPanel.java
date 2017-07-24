package view;

import controller.LoginController;
import utilities.Settings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class LoginPanel extends BasePanel {
    private LoginController controller;

    public LoginPanel(LoginController controller) {
        this.controller = controller;
        JLabel usernameLabel = new JLabel(Settings.USERNAME());
        JLabel passwordLabel = new JLabel(Settings.PASSWORD());
        JTextField usernameField = new JTextField(20);
        JTextField passwordField = new JPasswordField(20);
        JButton submit = new JButton(Settings.SUBMIT_BUTTON());
        this.imagePanel = LoadImage.load(Settings.PANELS_FOLDER() + "log-in.png");
        this.backButton.addActionListener(e -> this.controller.back());
        usernameLabel.setForeground(Color.WHITE);
        passwordLabel.setForeground(Color.WHITE);

        this.centralPanel.add(usernameLabel, k);
        k.gridy++;
        this.centralPanel.add(usernameField, k);
        k.gridy++;
        this.centralPanel.add(passwordLabel, k);
        k.gridy++;
        this.centralPanel.add(passwordField, k);
        k.gridy++;
        this.centralPanel.add(submit,k);
        submit.addActionListener(e -> this.controller.login(usernameField.getText(), passwordField.getText()));

        JUtil.setFocus(usernameField);
        JUtil.setSubmitEnterClick(usernameField, submit);
        JUtil.setSubmitEnterClick(passwordField, submit);
        JUtil.setEnterClick(submit);
        JUtil.setEscClick(usernameField, this.backButton);
        JUtil.setEscClick(passwordField, this.backButton);
        JUtil.setEscClick(submit, this.backButton);
        JUtil.setEscClick(this.backButton, this.backButton);
        JUtil.setEnterClick(this.backButton);
    }

}
