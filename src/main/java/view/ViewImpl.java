package view;
import controller.Controller;
import utilities.Settings;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.*;

public class ViewImpl extends JFrame implements View {

    private static final String WINDOW_TITLE = "Pokemon MP";
    private Controller controller;
    private BuildingPanel buildingPanel;

    public ViewImpl(Controller controller) {
        this.controller = controller;
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
    public void showGame(JPanel gamePanel) {
        this.setPanel(gamePanel);
    }

    @Override
    public void showPause() {

    }

    @Override
    public void showError(final String error, final String title) {
        JOptionPane.showMessageDialog(this, error, title, JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void showPokemonCenterMap() {
        if(this.controller.getGameController().isPresent()){
            this.buildingPanel = new BuildingPanel(this.controller.getGameController().get());
            this.setPanel(this.buildingPanel);
        }
    }
}
