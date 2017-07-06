package app

import java.util.Optional
import javax.swing._

import model.entities.{Owner, PokemonFactory, PokemonWithLife}
import utilities.Settings
import view.BattlePanel

object CreazionePokemonMain extends App {
  val frame: JFrame = new JFrame()
  frame.setSize(Settings.FRAME_SIDE, Settings.FRAME_SIDE)
  frame.setResizable(false)
  frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
  var myPokemon: PokemonWithLife = PokemonFactory.createPokemon(Owner.WILD,Optional.empty(),Optional.of(25)).get()
  val panel: BattlePanel = new BattlePanel(myPokemon,
    PokemonFactory.createPokemon(Owner.WILD,Optional.empty(),Optional.of(25)).get(),frame)
  frame.add(panel)
  myPokemon.loseLifePoints(30)
  panel.setPokemonLife()
  val lifeRatio: Double = myPokemon.pokemonLife.toDouble/myPokemon.pokemon.experiencePoints.toDouble
  panel.setPokemonLifeProgressBar((lifeRatio*100).toInt,Owner.TRAINER.id)
  frame.setVisible(true)
  panel.requestFocusInWindow()

}