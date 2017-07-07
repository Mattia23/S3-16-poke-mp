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

public abstract class DialoguePanel extends JPanel implements KeyListener{
    private final JLabel dialogueLabel;
    protected String response;
    private int currentButton = 0;
    private int currentDialogue = 0;
    protected final List<JButton> buttons = new ArrayList<>();

    public DialoguePanel(final Semaphore semaphore, final List<String> dialogues, final List<String> buttonText){
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(10, 10, 10, 10));
        dialogueLabel = new JLabel();
        if(dialogues.size() != currentDialogue) dialogueLabel.setText(dialogues.get(0));
        dialogueLabel.setFont(new Font("Serif", Font.PLAIN, 24));
        add(dialogueLabel, BorderLayout.WEST);

        final JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);

        for(String text: buttonText){
            final JButton button = new JButton(text);
            button.setBackground(Color.WHITE);
            button.addActionListener(e -> {
                if (dialogues.size() > currentDialogue) {
                    dialogueLabel.setText(dialogues.get(++currentDialogue));
                }
                if (dialogues.size() == currentDialogue) {
                    setFinalButtons();
                }
            });
            button.addKeyListener(this);
            buttonPanel.add(button);
            buttons.add(button);
            buttons.get(currentButton).requestFocus();
        }
        add(buttonPanel, BorderLayout.EAST);
    }

    protected abstract void setFinalButtons();

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
