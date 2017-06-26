package view;
import controller.GameKeyListener;
import controller.GameViewObserver;
import model.game.Model;
import javax.swing.*;
import java.awt.event.ActionEvent;

public class GamePanel extends JPanel {

    private Model model;
    private GameKeyListener keyListener;

    public GamePanel(final GameViewObserver gameController) {
        this.setFocusable(true);
        this.requestFocusInWindow();
        this.keyListener = new GameKeyListener(gameController);
        this.addKeyListener(this.keyListener);
    }


    public void drawModel(final Model model) {
        this.model = model;
        this.requestFocusInWindow();
        SwingUtilities.invokeLater(() -> super.repaint());
        this.keyListener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "Aggiorna il protagonista"));
    }

}
