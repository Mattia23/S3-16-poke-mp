package view;

import controller.InitialMenuController;
import utilities.Settings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class InitialMenuPanel extends BasePanel implements ActionListener, KeyListener {

    private final static int INSETS = 10;
    private InitialMenuController controller;
    private JPanel downPanel;
    private JButton[] buttons = new JButton[3];
    private int currentButton = 0;

    public InitialMenuPanel(InitialMenuController controller) {
        this.controller = controller;
        this.setLayout(new BorderLayout());
        this.downPanel = new JPanel(new GridBagLayout());
        this.buttons[0] = new JButton(Settings.Strings$.MODULE$.LOGIN_BUTTON());
        this.buttons[1] = new JButton(Settings.Strings$.MODULE$.SIGN_IN_BUTTON());
        this.buttons[2] = new JButton(Settings.Strings$.MODULE$.QUIT_BUTTON());

        this.buttons[0].addActionListener(this);
        this.buttons[1].addActionListener(this);
        this.buttons[2].addActionListener(this);

        k = new GridBagConstraints();
        k.gridy = 0;
        k.insets = new Insets(INSETS, INSETS, INSETS, INSETS);
        k.fill = GridBagConstraints.VERTICAL;
        for(JButton button: buttons){
            this.downPanel.add(button, k);
            k.gridy++;
            JUtil.setEnterClick(button);
            button.addKeyListener(this);
        }
        this.downPanel.setOpaque(false);
        this.add(downPanel, BorderLayout.SOUTH);
        JUtil.setFocus(this.buttons[0]);
        repaint();
    }

    public void actionPerformed(ActionEvent ev) {
        try {
            controller.processEvent(ev.getActionCommand());
        } catch (Exception ex) {
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_DOWN:
                if (currentButton < buttons.length-1) {
                    buttons[++currentButton].requestFocus();
                }
                break;
            case KeyEvent.VK_UP:
                if (currentButton > 0) {
                    buttons[--currentButton].requestFocus();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
