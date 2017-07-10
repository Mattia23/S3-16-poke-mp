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
   // private View parentView;
    private JButton submit;
    private Map<String,JTextField> accountData;

    public SignInPanel(SignInController controller) {
        //this.parentView = view;
        this.controller = controller;
        this.imagePanel = LoadImage.load(Settings.PANELS_FOLDER() + "sign-in.png");
        this.backButton.addActionListener(e -> this.controller.back());
        this.submit  = new JButton(Settings.SUBMIT_BUTTON());
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

        this.submit.addActionListener(e -> this.controller.signIn(accountData)
            /*if(this.accountData.get(AccountData.Name.toString()).getText().length() > 2  &&
                    this.accountData.get(AccountData.Surname.toString()).getText().length() > 2 &&
                    this.accountData.get(AccountData.Email.toString()).getText().contains(String.valueOf('@'))  &&
                    this.accountData.get(AccountData.Username.toString()).getText().length() > 3 &&
                    this.accountData.get(AccountData.Password.toString()).getText().length() > 7) {

                if(DBConnect.insertCredentials(this.accountData,1)) {
                    showMessage("You have registered correctly","SIGNIN SUCCEEDED",JOptionPane.INFORMATION_MESSAGE);
                } else {
                    showMessage("Username not available","SIGNIN FAILED",JOptionPane.ERROR_MESSAGE);
                }
            } else {
                this.parentView.showError("Error in entering data", "WRONG SINGIN");
            }*/
        );

    }

    private void showMessage(final String msg, final String title, final int msgType) {
        JOptionPane.showMessageDialog(this,msg,title,msgType);
    }

}
