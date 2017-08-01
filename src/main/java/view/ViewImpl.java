package view;

import controller.BattleController;
import controller.GameController;
import controller.*;
import model.entities.PokemonWithLife;
import model.map.BuildingMap;
import model.map.GameMap;
import model.entities.Trainer;
import utilities.Settings;

import javax.swing.*;
import java.awt.*;

public class ViewImpl extends JFrame implements View {

    private static final String WINDOW_TITLE = "Pokemon MP";
    private Dimension frameDiminsion;
    private BattleView battlePanel;
    private GamePanel gamePanel;

    public ViewImpl() {
        this.setTitle(WINDOW_TITLE);
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.frameDiminsion = new Dimension(Settings.FRAME_SIDE(), Settings.FRAME_SIDE());
        this.setSize(frameDiminsion);
        this.setMinimumSize(frameDiminsion);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        //this.setAlwaysOnTop(true);
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

    private void setGameMenuPanel(GameMenuController gameMenuController) {
        this.getContentPane().add(new GameMenuPanel(gameMenuController), BorderLayout.EAST);
        this.revalidate();
        this.repaint();
    }

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
    public void showMap(GameController mapController, DistributedMapController distributedMapController, GameMap gameMap) {
        this.gamePanel = new MapPanel(mapController, distributedMapController, gameMap);
        this.setPanel(this.gamePanel);
    }

    @Override
    public void showPokemonCenter(GameController pokemonCenterController, BuildingMap buildingMap) {
        this.gamePanel = new PokemonCenterPanel(pokemonCenterController, buildingMap);
        this.setPanel(this.gamePanel);
    }

    @Override
    public void showLaboratory(GameController laboratoryController, BuildingMap buildingMap, boolean emptyCaptures) {
        this.gamePanel = new LaboratoryPanel(laboratoryController, buildingMap, emptyCaptures);
        this.setPanel(this.gamePanel);
    }

    @Override
    public GamePanel getGamePanel() { return this.gamePanel; }

    @Override
    public void showBoxPanel(BuildingController buildingController) {
        this.setPanel(new BoxPanel(buildingController));
    }

    @Override
    public void showInitialPokemonPanel(BuildingController buildingController, PokemonWithLife pokemon) {
        this.setPanel(new InitialPokemonPanel(buildingController, pokemon));
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
    public void showPokemonChoice(BattleController battleController, Trainer trainer) { this.setPanel(new PokemonChoicePanel(battleController, trainer)); }

    @Override
    public void showDialogue(JPanel dialoguePanel){
        this.setDialogue(dialoguePanel);
    }

    @Override
    public void showPokedex(GameMenuController gameMenuController, GameController gameController) {
        this.setPanel(new PokedexPanel(gameMenuController, gameController));
    }

    @Override
    public void showTeamPanel(GameMenuController gameMenuController, GameController gameController) {
        this.setPanel(new TeamPanel(gameMenuController, gameController));
    }

    @Override
    public void showTrainerPanel(GameMenuController gameMenuController, GameController gameController) {
        this.setPanel(new TrainerPanel(gameMenuController, gameController));
    }

    @Override
    public void showRankingPanel(GameMenuController gameMenuController, GameController gameController) {
        this.setPanel(new RankingPanel(gameMenuController, gameController));
    }

    @Override
    public void showKeyboardPanel(GameMenuController gameMenuController, GameController gameController) {
        this.setPanel(new KeyboardPanel(gameMenuController, gameController));
    }

    @Override
    public void showGameMenuPanel(GameMenuController gameMenuController) {
        this.setGameMenuPanel(gameMenuController);
    }

    @Override
    public void showPokemonInTeamPanel(PokemonWithLife pokemonWithLife, GameMenuController gameMenuController) {
        this.setPanel(new PokemonInTeamPanel(pokemonWithLife, gameMenuController));
    }

    @Override
    public void showMessage(final String message, final String title, final int messageType) {
        JOptionPane.showMessageDialog(this, message, title, messageType);
    }

}
