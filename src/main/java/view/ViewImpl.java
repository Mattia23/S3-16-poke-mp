package view;
import controller.BattleController;
import controller.Controller;
import database.remote.DBConnect;
import model.entities.PokemonWithLife;
import utilities.Settings;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.*;

public class ViewImpl extends JFrame implements View {

    private static final String WINDOW_TITLE = "Pokemon MP";
    private Controller controller;
    private BuildingPanel buildingPanel;
    private Dimension frameDim;
    private BattleView battlePanel;

    public ViewImpl(Controller controller) {
        this.controller = controller;
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frameDim = new Dimension(Settings.FRAME_SIDE(), Settings.FRAME_SIDE());
        this.setSize(frameDim);
        this.setMinimumSize(frameDim);
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
    public void showBattle (PokemonWithLife myPokemon, PokemonWithLife otherPokemon, BattleController battleController) {
        this.battlePanel = new BattlePanel(myPokemon,otherPokemon,this,battleController);
        BattlePanel panel = (BattlePanel) this.battlePanel;
        this.setPanel(panel);
    }

    @Override
    public  BattleView getBattlePanel(){
        return this.battlePanel;
    }

    @Override
    public void showPokemonChoice() { this.setPanel(new PokemonChoicePanel(this, this.controller, DBConnect.getTrainerFromDB("Ash").get())); }

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
