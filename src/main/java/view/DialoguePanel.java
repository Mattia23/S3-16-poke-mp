package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Semaphore;

public class DialoguePanel extends JPanel implements KeyListener{
    private final JLabel dialogueLabel;
    private String response;
    private int currentButton = 0;
    private final List<JButton> buttons = new ArrayList<>();

    public DialoguePanel(Semaphore semaphore, List<String> buttonText){
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(10, 10, 10, 10));
        dialogueLabel = new JLabel();
        dialogueLabel.setFont(new Font("Serif", Font.PLAIN, 24));
        setLayout(new BorderLayout());
        add(dialogueLabel, BorderLayout.WEST);


        final JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);

        for(String text: buttonText){
            final JButton button = new JButton(text);
            button.setBackground(Color.WHITE);
            button.addActionListener(e -> {
                        response = button.getText();
                        semaphore.release();
                    }
            );
            button.addKeyListener(this);
            buttonPanel.add(button);
            buttons.add(button);
        }
        add(buttonPanel, BorderLayout.EAST);
    }

    @Override
    public void setVisible(boolean visible){
        super.setVisible(visible);
        currentButton = 0;
        buttons.get(currentButton).requestFocus();
    }

    public void setText(String dialogue){
        dialogueLabel.setText(dialogue);
    }

    public String getValue(){
        return response;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_RIGHT:
                if (currentButton < buttons.size()-1) {
                    buttons.get(++currentButton).requestFocus();
                }
                break;
            case KeyEvent.VK_LEFT:
                if (currentButton > 0) {
                    buttons.get(--currentButton).requestFocus();
                }
                break;
            case KeyEvent.VK_Z:
                buttons.get(currentButton).doClick();
            default:
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
