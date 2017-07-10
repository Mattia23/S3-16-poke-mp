package view;

import controller.Controller;
import controller.InitialMenuController;
import utilities.Settings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class InitialMenuPanel extends BasePanel implements ActionListener {

    private final static int INSETS = 10;
    private InitialMenuController controller;
   // private View parentView;
    private JPanel downPanel;
   // private JButton newgame;
    private JButton login;
    private JButton signIn;
    private JButton quit;

    public InitialMenuPanel(/*View view,*/ InitialMenuController controller) {
        this.controller = controller;
     //   this.parentView = view;
        this.setLayout(new BorderLayout());
        this.downPanel = new JPanel(new GridBagLayout());
       // this.newgame = new JButton("gioca (prova)");
        this.login = new JButton(Settings.LOGIN_BUTTON());
        this.signIn = new JButton(Settings.SIGN_IN_BUTTON());
        this.quit = new JButton(Settings.QUIT_BUTTON());

        /*  this.newgame.addActionListener(e -> {
            this.controller.newGame();
        });*/

        this.login.addActionListener(this);
        this.signIn.addActionListener(this);
        this.quit.addActionListener(this);

      /*  this.login.addActionListener(e -> this.parentView.showLogin());
        this.signIn.addActionListener(e -> this.parentView.showSignIn());
        this.quit.addActionListener(e -> System.exit(0));
       */
        k = new GridBagConstraints();
        k.gridy = 0;
        k.insets = new Insets(INSETS, INSETS, INSETS, INSETS);
        k.fill = GridBagConstraints.VERTICAL;
        /*this.downPanel.add(this.newgame, k);
        k.gridy++;*/
        this.downPanel.add(this.login, k);
        k.gridy++;
        this.downPanel.add(this.signIn, k);
        k.gridy++;
        this.downPanel.add(this.quit, k);
        this.downPanel.setOpaque(false);
        this.add(downPanel, BorderLayout.SOUTH);
    }

    public void actionPerformed(ActionEvent ev) {
        try {
            controller.processEvent(ev.getActionCommand());
        } catch (Exception ex) {
        }
    }

}
