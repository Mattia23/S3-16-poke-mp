package view;
import controller.*;
import database.remote.DBConnect;
import model.entities.Pokemon;
import model.entities.PokemonWithLife;
import model.environment.BuildingMap;
import model.map.GameMap;
import utilities.Settings;

import javax.swing.*;
import java.awt.*;

public class ViewImpl extends JFrame implements View {

    private static final String WINDOW_TITLE = "Pokemon MP";
    private Controller controller;
    private Dimension frameDiminsion;
    private BattleView battlePanel;
    private GamePanel mapPanel;
    private GamePanel pokemonCenterPanel;
    private GamePanel laboratoryPanel;

    public ViewImpl() {
       // this.controller = controller;
        this.setTitle(WINDOW_TITLE);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frameDiminsion = new Dimension(Settings.FRAME_SIDE(), Settings.FRAME_SIDE());
        this.setSize(frameDiminsion);
        this.setMinimumSize(frameDiminsion);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setAlwaysOnTop(true);
     //   this.getContentPane().add(new InitialMenuPanel(this, this.controller));
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
    public void showInitialMenu(InitialMenuController initialMenuController) {
        this.setPanel(new InitialMenuPanel(initialMenuController));
    }

    @Override
    public void showLogin(LoginController loginController) {
        this.setPanel(new LoginPanel(loginController));
    }

    @Override
    public void showSignIn(SignInController signInController) {
        this.setPanel(new SignInPanel(signInController));
    }

    @Override
    public void showMap(GameController mapController, GameMap gameMap) {
        this.mapPanel = new MapPanel(mapController, gameMap);
        this.setPanel(this.mapPanel);
    }

    @Override
    public GamePanel getMapPanel() {
        return this.mapPanel;
    }

    @Override
    public void showPokemonCenter(GameController pokemonCenterController, BuildingMap buildingMap) {
        this.pokemonCenterPanel = new PokemonCenterPanel(pokemonCenterController, buildingMap);
        this.setPanel(this.pokemonCenterPanel);
    }

    @Override
    public GamePanel getPokemonCenterPanel() {
        return this.pokemonCenterPanel;
    }

    @Override
    public void showLaboratory(GameController laboratoryController, BuildingMap buildingMap, boolean emptyCaptures) {
        this.laboratoryPanel = new LaboratoryPanel(laboratoryController, buildingMap, emptyCaptures);
        this.setPanel(this.laboratoryPanel);
    }

    @Override
    public GamePanel getLaboratoryPanel() {
        return this.laboratoryPanel;
    }

    @Override
    public void showBoxPanel(BuildingController buildingController) {
        this.setPanel(new BoxPanel(buildingController));
    }

    @Override
    public void showInitialPokemonPanel(BuildingController buildingController, Pokemon pokemon) {
        this.setPanel(new InitialPokemonPanel(buildingController, pokemon));
    }

    /*
        @Override
        public void showPanel(JPanel panel) {
            this.setPanel(panel);
        }
    */
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
    public void showDialogue(JPanel dialoguePanel){
        this.setDialogue(dialoguePanel);
    }

    @Override
    public void showPause() {

    }
/*
    @Override
    public void showError(final String error, final String title) {
        JOptionPane.showMessageDialog(this, error, title, JOptionPane.ERROR_MESSAGE);
    }
*/
    @Override
    public void showMessage(final String message, final String title, final int messageType) {
        JOptionPane.showMessageDialog(this, message, title, messageType);
    }

}
