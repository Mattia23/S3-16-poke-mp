package view;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class JUtil {
    public static void setFocus(JComponent jComponent){
        jComponent.addAncestorListener(new AncestorListener() {
            @Override
            public void ancestorAdded(AncestorEvent event) {
                jComponent.requestFocus();
            }

            @Override
            public void ancestorRemoved(AncestorEvent event) {

            }

            @Override
            public void ancestorMoved(AncestorEvent event) {

            }
        });
    }

    public static void setEnterClick(JButton jButton){
        jButton.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                    jButton.doClick();
                }
            }
        });
    }

    public static void setSubmitEnterClick(JComponent jComponent, JButton jButton){
        jComponent.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                    jButton.doClick();
                }
            }
        });
    }

    public static void setEscClick(JComponent jComponent, JButton jButton){
        jComponent.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == KeyEvent.VK_ESCAPE) {
                    jButton.doClick();
                }
            }
        });
    }
}
