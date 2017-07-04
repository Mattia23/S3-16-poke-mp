package app

import java.awt._
import java.util.Optional
import javax.swing._
import javax.imageio.ImageIO
import model.entities.{Owner, PokemonFactory, PokemonWithLife}
import utilities.Settings
import view.BattlePanel

object CreazionePokemon extends App {
  val imagePokemonSize: Int = Settings.FRAME_SIDE / 4
  val pokemonImgPose: Array[Dimension] = Array(new Dimension((Settings.FRAME_SIDE * 0.65).toInt,(Settings.FRAME_SIDE * 0.10).toInt),
                                          new Dimension((Settings.FRAME_SIDE * 0.11).toInt,(Settings.FRAME_SIDE * 0.48).toInt))
  val pokemonNamePose: Array[Dimension] = Array(new Dimension((Settings.FRAME_SIDE * 0.08).toInt,(Settings.FRAME_SIDE * 0.165).toInt),
                                                new Dimension((Settings.FRAME_SIDE * 0.57).toInt,(Settings.FRAME_SIDE * 0.565).toInt))
  var pokemonNames: Array[JTextField] = Array(new JTextField(),new JTextField())

  val frame: JFrame = new JFrame()
  val mainPanel: BattlePanel = new BattlePanel()
  var imageIcon: String = new String
  var imageIcon2: String = new String

  val firstPokemonImage: JButton = new JButton()
  firstPokemonImage.setContentAreaFilled(false)
  firstPokemonImage.setBorder(null)
  firstPokemonImage.setBounds(pokemonImgPose(0).width,pokemonImgPose(0).height,imagePokemonSize,imagePokemonSize)

  val secondPokemonImage: JButton = new JButton()
  secondPokemonImage.setContentAreaFilled(false)
  secondPokemonImage.setBorder(null)
  secondPokemonImage.setBounds(pokemonImgPose(1).width,pokemonImgPose(1).height,imagePokemonSize,imagePokemonSize)

  frame.setSize(Settings.FRAME_SIDE, Settings.FRAME_SIDE)
  frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
  mainPanel.setLayout(null)
  val button: JButton = new JButton("Crea pokemon")
  button.setBounds(400,500,200,30)

  button.addActionListener((e) => {
    val p1: PokemonWithLife = PokemonFactory.createPokemon(Owner.WILD,Optional.empty(),Optional.of(15)).get()
    val p2: PokemonWithLife = PokemonFactory.createPokemon(Owner.WILD,Optional.empty(),Optional.of(15)).get()
    pokemonNames(0).setText(p1.pokemon.name.toUpperCase)
    pokemonNames(0).setBounds(pokemonNamePose(0).width,pokemonNamePose(0).height,160,30)
    pokemonNames(0).setFont(new Font("Verdana", Font.BOLD, 22))
    pokemonNames(0).setOpaque(false)
    pokemonNames(0).setBorder(null)
    mainPanel.add(pokemonNames(0))
    pokemonNames(1).setText(p2.pokemon.name.toUpperCase)
    pokemonNames(1).setBounds(pokemonNamePose(1).width,pokemonNamePose(1).height,160,30)
    pokemonNames(1).setFont(new Font("Verdana", Font.BOLD, 22))
    pokemonNames(1).setOpaque(false)
    pokemonNames(1).setBorder(null)
    mainPanel.add(pokemonNames(1))

    var myImage: Image = ImageIO.read(getClass.getResource(Settings.POKEMON_IMAGES_FRONT_FOLDER + p1.pokemon.id + ".png"))
    var myImageIcon: ImageIcon = new ImageIcon(myImage.getScaledInstance(imagePokemonSize,imagePokemonSize,java.awt.Image.SCALE_SMOOTH))
    firstPokemonImage.setIcon(myImageIcon)

    var myImage2: Image = ImageIO.read(getClass.getResource(Settings.POKEMON_IMAGES_BACK_FOLDER + p2.pokemon.id + ".png"))
    var myImageIcon2: ImageIcon = new ImageIcon(myImage2.getScaledInstance(imagePokemonSize,imagePokemonSize,java.awt.Image.SCALE_SMOOTH))
    secondPokemonImage.setIcon(myImageIcon2)

  })
  mainPanel.add(firstPokemonImage)
  mainPanel.add(secondPokemonImage)
  mainPanel.add(button)

  frame.add(mainPanel)
  frame.setVisible(true)
  frame.setResizable(false)
}
