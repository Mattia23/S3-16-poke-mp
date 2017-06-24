package view;

import controller.Controller;
import utilities.Settings;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public class SignInPanel extends BasePanel {

    private Controller controller;
    private View parentView;
    private JButton submit;
    private Map<String,JTextField> accountData;

    public SignInPanel(View view, Controller ctrl) {
        this.parentView = view;
        this.controller = ctrl;
        this.imagePanel = LoadImage.load(Settings.PANELS_FOLDER() + "sign-in.png");
        this.backButton.addActionListener(e -> this.parentView.showMenu());
        this.submit  = new JButton("Submit");
        this.accountData = new HashMap<>();

        for(AccountData data : AccountData.values()) {
            this.centralPanel.add(new JLabel(data.toString()), k);
            k.gridy++;
            JTextField textField = new JTextField(20);
            this.centralPanel.add(textField,k);
            k.gridy++;
            this.accountData.put(data.toString(),textField);
        }
        this.centralPanel.add(this.submit, k);

        this.submit.addActionListener(e -> {
            if(this.accountData.get(AccountData.Name.toString()).getText().length() > 2  &&
                    this.accountData.get(AccountData.Surname.toString()).getText().length() > 2 &&
                    this.accountData.get(AccountData.Email.toString()).getText().contains(String.valueOf('@'))  &&
                    this.accountData.get(AccountData.Username.toString()).getText().length() > 3 &&
                    this.accountData.get(AccountData.Username.toString()).getText().length() > 7) {

                //esegui controlli sull'username!
            } else {
                this.parentView.showError("Error in entering data", "WRONG SINGIN");
            }
        });

    }



}
