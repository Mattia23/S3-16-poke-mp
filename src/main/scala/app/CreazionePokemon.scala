package app

import java.awt._
import java.util.Optional
import javax.swing._
import javax.imageio.ImageIO

import com.sun.xml.internal.ws.api.config.management.policy.ManagementAssertion.Setting
import model.entities.{Owner, PokemonFactory, PokemonWithLife}
import utilities.Settings
import view.BattlePanel

object CreazionePokemon extends App {
  val imagePokemonSize: Int = Settings.FRAME_SIDE / 4
  val firstPokemonX: Int = (Settings.FRAME_SIDE * 0.65).toInt
  val firstPokemonY: Int = (Settings.FRAME_SIDE * 0.10).toInt
  val secondPokemonX: Int = (Settings.FRAME_SIDE * 0.11).toInt
  val secondPokemonY: Int = (Settings.FRAME_SIDE * 0.48).toInt
  val frame: JFrame = new JFrame()
  val mainPanel: BattlePanel = new BattlePanel()
  mainPanel.setLayout(null)
  var imageIcon: String = new String
  var imageIcon2: String = new String
  frame.setSize(Settings.FRAME_SIDE, Settings.FRAME_SIDE)
  frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)

  val buttonPoke: JButton = new JButton()
  this.buttonPoke.setContentAreaFilled(false)
  this.buttonPoke.setBorder(null)
  val buttonPoke2: JButton = new JButton()
  this.buttonPoke2.setContentAreaFilled(false)
  this.buttonPoke2.setBorder(null)
  val textField: JTextField = new JTextField("Pokemon")
  val button: JButton = new JButton("Crea pokemon")
  button.setBounds(300,300,200,30)

  button.addActionListener((e) => {
    val p1: PokemonWithLife = PokemonFactory.createPokemon(Owner.WILD,Optional.empty(),Optional.of(25)).get()
    val p2: PokemonWithLife = PokemonFactory.createPokemon(Owner.WILD,Optional.empty(),Optional.of(25)).get()
    textField.setText(p1.pokemon.name)
    imageIcon = Settings.POKEMON_IMAGES_FRONT_FOLDER + p1.pokemon.id + ".png"
    var myImage: Image = ImageIO.read(getClass.getResource(imageIcon))
    val myImageIcon = new ImageIcon(myImage.getScaledInstance(imagePokemonSize,imagePokemonSize,java.awt.Image.SCALE_SMOOTH))
    buttonPoke.setIcon(myImageIcon)
    imageIcon2 = Settings.POKEMON_IMAGES_BACK_FOLDER + p2.pokemon.id + ".png"
    var myImage2 = ImageIO.read(getClass.getResource(imageIcon2))
    val myImageIcon2 = new ImageIcon(myImage2.getScaledInstance(imagePokemonSize,imagePokemonSize,java.awt.Image.SCALE_SMOOTH))
    buttonPoke2.setIcon(myImageIcon2)
  })
  buttonPoke.setBounds(firstPokemonX,firstPokemonY,imagePokemonSize,imagePokemonSize)
  buttonPoke2.setBounds(secondPokemonX,secondPokemonY,imagePokemonSize,imagePokemonSize)
  mainPanel.add(buttonPoke)
  mainPanel.add(buttonPoke2)
  mainPanel.add(button)

  frame.add(mainPanel)
  frame.setVisible(true)
  frame.setResizable(false)
}
