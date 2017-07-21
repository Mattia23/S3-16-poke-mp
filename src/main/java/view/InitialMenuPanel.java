package view;

import controller.InitialMenuController;
import utilities.Settings;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class InitialMenuPanel extends BasePanel implements ActionListener {

    private final static int INSETS = 10;
    private InitialMenuController controller;
    private JPanel downPanel;
   // private JButton newgame;
    private JButton login;
    private JButton signIn;
    private JButton quit;

    public InitialMenuPanel(InitialMenuController controller) {
        this.controller = controller;
        this.setLayout(new BorderLayout());
        this.downPanel = new JPanel(new GridBagLayout());
       // this.newgame = new JButton("gioca (prova)");
        this.login = new JButton(Settings.LOGIN_BUTTON());
        this.signIn = new JButton(Settings.SIGN_IN_BUTTON());
        this.quit = new JButton(Settings.QUIT_BUTTON());

        this.login.addActionListener(this);
        this.signIn.addActionListener(this);
        this.quit.addActionListener(this);

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
        this.login.addAncestorListener(new AncestorListener() {
            @Override
            public void ancestorAdded(AncestorEvent ae) {
                login.requestFocus();
            }

            @Override
            public void ancestorRemoved(AncestorEvent event) { }

            @Override
            public void ancestorMoved(AncestorEvent event) { }
        });
        repaint();
    }

    public void actionPerformed(ActionEvent ev) {
        try {
            controller.processEvent(ev.getActionCommand());
        } catch (Exception ex) {
        }
    }

}
