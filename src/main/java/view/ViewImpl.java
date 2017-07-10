package view;
import controller.BattleController;
import controller.Controller;
import database.remote.DBConnect;
import model.entities.PokemonWithLife;
import utilities.Settings;

import javax.swing.*;
import java.awt.*;

public class ViewImpl extends JFrame implements View {

    private static final String WINDOW_TITLE = "Pokemon MP";
    private Controller controller;
    private Dimension frameDiminsion;
    private BattleView battlePanel;

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
    public void showMenu() {
        /*this.setPanel(new InitialMenuPanel(this, this.controller));*/
    }

    @Override
    public void showPanel(JPanel panel) {
        this.setPanel(panel);
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
    public void showDialogue(JPanel dialoguePanel){
        this.setDialogue(dialoguePanel);
    }

    @Override
    public void showPause() {

    }

    @Override
    public void showError(final String error, final String title) {
        JOptionPane.showMessageDialog(this, error, title, JOptionPane.ERROR_MESSAGE);
    }

}
