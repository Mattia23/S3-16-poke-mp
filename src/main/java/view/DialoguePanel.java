package view;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

public abstract class DialoguePanel extends JPanel implements KeyListener{
    protected final JLabel dialogueLabel;
    protected String response;
    protected int currentButton = 0;
    private int currentDialogue = 0;
    protected final List<JButton> buttons = new ArrayList<>();
    protected final JPanel buttonPanel = new JPanel();

    public DialoguePanel(final List<String> dialogues){
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createRaisedBevelBorder(), BorderFactory.createLoweredBevelBorder()));
        dialogueLabel = new JLabel("", SwingConstants.CENTER);
        if(dialogues.size() != currentDialogue) dialogueLabel.setText(dialogues.get(0));
        dialogueLabel.setFont(new Font("Serif", Font.PLAIN, 24));
        add(dialogueLabel, BorderLayout.CENTER);

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
        buttons.add(buttonNext);
        buttonNext.addKeyListener(this);
        buttonPanel.add(buttonNext);
        add(buttonPanel, BorderLayout.SOUTH);
        buttonNext.requestFocus();
        JUtil.setFocus(buttonNext);
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
            default:
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
