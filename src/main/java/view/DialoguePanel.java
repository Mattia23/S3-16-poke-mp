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
    protected int currentButton = 0;
    private int currentDialogue = 0;
    protected final List<JButton> buttons = new ArrayList<>();
    protected final JPanel buttonPanel = new JPanel();

    public DialoguePanel(final List<String> dialogues){
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(10, 10, 10, 10));
        dialogueLabel = new JLabel();
        if(dialogues.size() != currentDialogue) dialogueLabel.setText(dialogues.get(0));
        dialogueLabel.setFont(new Font("Serif", Font.PLAIN, 24));
        add(dialogueLabel, BorderLayout.WEST);

        buttonPanel.setBackground(Color.WHITE);
        final JButton buttonNext = new JButton("next");
        buttonNext.addActionListener(e -> {
            if (dialogues.size() > currentDialogue) {
                currentDialogue++;
                dialogueLabel.setText(dialogues.get(currentDialogue));
            }
            if (dialogues.size() - 1 == currentDialogue) {
                currentDialogue++;
                setFinalButtons();
            }
        });
        buttonPanel.add(buttonNext);
        buttons.add(buttonNext);
        buttons.get(currentButton).addKeyListener(this);
        buttons.get(currentButton).requestFocus();
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
