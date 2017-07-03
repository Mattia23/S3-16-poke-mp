package app

import java.awt.{BorderLayout, Color, Dimension, GridLayout}
import java.util.Optional
import javax.swing._
import javax.imageio.ImageIO

import com.sun.xml.internal.ws.api.config.management.policy.ManagementAssertion.Setting
import model.entities.{Owner, PokemonFactory, PokemonWithLife}
import utilities.Settings
import view.BattlePanel

object CreazionePokemon extends App {
  val frame: JFrame = new JFrame()
  val mainPanel: BattlePanel = new BattlePanel()
  var imageIcon: String = new String
  var imageIcon2: String = new String
  frame.setSize(Settings.FRAME_WIDTH, Settings.FRAME_WIDTH)
  frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
  mainPanel.setLayout(new GridLayout(7,6))
  val panelHolder = Array.ofDim[JPanel](7, 6)
  for(i <- 0 until 7) {
    for(j <- 0 until 6) {
      panelHolder(i)(j) = new JPanel()
      panelHolder(i)(j).setOpaque(false)
      mainPanel.add(panelHolder(i)(j))
    }
  }

  val buttonPoke: JButton = new JButton()
  this.buttonPoke.setContentAreaFilled(false)
  this.buttonPoke.setBorder(null)
  val buttonPoke2: JButton = new JButton()
  this.buttonPoke2.setContentAreaFilled(false)
  this.buttonPoke2.setBorder(null)
  val textField: JTextField = new JTextField("Pokemon")
  val button: JButton = new JButton("Crea pokemon")

  button.addActionListener((e) => {
    val p1: PokemonWithLife = PokemonFactory.createPokemon(Owner.WILD,Optional.empty(),Optional.of(15)).get()
    val p2: PokemonWithLife = PokemonFactory.createPokemon(Owner.WILD,Optional.empty(),Optional.of(15)).get()
    textField.setText(p1.pokemon.name)
    imageIcon = Settings.POKEMON_IMAGES_FRONT_FOLDER + p1.pokemon.id + ".png"
    var myImage = ImageIO.read(getClass.getResource(imageIcon))
    val myImageIcon = new ImageIcon(myImage)
    buttonPoke.setIcon(myImageIcon)
    imageIcon2 = Settings.POKEMON_IMAGES_BACK_FOLDER + p2.pokemon.id + ".png"
    var myImage2 = ImageIO.read(getClass.getResource(imageIcon2))
    val myImageIcon2 = new ImageIcon(myImage2)
    buttonPoke2.setIcon(myImageIcon2)
  })
  panelHolder(1)(4).add(buttonPoke)
  panelHolder(4)(1).add(buttonPoke2)
  panelHolder(5)(3).add(button)
  frame.add(mainPanel)
  frame.setVisible(true)
}
