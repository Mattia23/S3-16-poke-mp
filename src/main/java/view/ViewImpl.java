package view;
import controller.Controller;
import utilities.Settings;

import javax.swing.*;
import java.awt.*;

public class ViewImpl extends JFrame implements View {

    private static final String WINDOW_TITLE = "Pokemon MP";
    private Controller controller;

    public ViewImpl(Controller controller) {
        this.controller = controller;
        this.setTitle(WINDOW_TITLE);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setSize(Settings.FRAME_WIDTH(), Settings.FRAME_WIDTH());
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setAlwaysOnTop(true);
        this.getContentPane().add(new MenuPanel(this, this.controller));
        this.validate();
        this.setVisible(true);
    }

    private void setPanel(final JPanel panel) {
        this.getContentPane().removeAll();
        this.getContentPane().add(panel);
        panel.setBackground(Color.black);
        this.revalidate();
        this.repaint();
    }

    private void setDialogue(final JPanel panel){
        final DialoguePanel dialoguePanel = (DialoguePanel) panel;
        this.getContentPane().add(dialoguePanel, BorderLayout.SOUTH);
        dialoguePanel.setPreferredSize(new Dimension(0, Settings.SCREEN_WIDTH()/12));
        this.revalidate();
        this.repaint();
    }

    @Override
    public void setController(Controller controller) { this.controller = controller; }
    

    @Override
    public void showMenu() {
        this.setPanel(new MenuPanel(this, this.controller));
    }

    @Override
    public void showLogin() { this.setPanel(new LoginPanel(this, this.controller)); }

    @Override
    public void showSignIn() { this.setPanel(new SignInPanel(this, this.controller)); }

    @Override
    public void showPanel(JPanel gamePanel) {
        this.setPanel(gamePanel);
    }

    @Override
    public void showDialogue(JPanel dialoguePanel){
        this.setDialogue(dialoguePanel);
    }

    @Override
    public void showPause() {

    }

    @Override
    public void showError(final String error, final String title) {
        JOptionPane.showMessageDialog(this, error, title, JOptionPane.ERROR_MESSAGE);
    }

}
