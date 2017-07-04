package app

import java.awt._
import java.awt.event.{KeyEvent, KeyListener}
import java.util.Optional
import javax.swing._
import javax.imageio.ImageIO

import database.local.PokedexConnect
import model.entities.{Owner, PokemonFactory, PokemonWithLife}
import utilities.Settings
import view.BattlePanel

class CreazionePokemon(myPoke: PokemonWithLife, otherPoke: PokemonWithLife, frame: JFrame) extends BattlePanel {
  val imagePokemonSize: Int = Settings.FRAME_SIDE / 4
  val fontSize: Int = (Settings.FRAME_SIDE * 0.034).toInt
  val fontLifeSize: Int = (Settings.FRAME_SIDE * 0.02).toInt
  val progressBarW: Int = (Settings.FRAME_SIDE * 0.23437).toInt
  val progressBarH: Int = (Settings.FRAME_SIDE * 0.021648).toInt
  val pokemonImgPose: Array[Dimension] = Array(new Dimension((Settings.FRAME_SIDE * 0.65).toInt,(Settings.FRAME_SIDE * 0.10).toInt),
                                          new Dimension((Settings.FRAME_SIDE * 0.11).toInt,(Settings.FRAME_SIDE * 0.48).toInt))
  val pokemonNamePose: Array[Dimension] = Array(new Dimension((Settings.FRAME_SIDE * 0.08).toInt,(Settings.FRAME_SIDE * 0.165).toInt),
                                            new Dimension((Settings.FRAME_SIDE * 0.57).toInt,(Settings.FRAME_SIDE * 0.565).toInt))
  val pokemonLevelPose: Array[Dimension] = Array(new Dimension((Settings.FRAME_SIDE * 0.06).toInt,(Settings.FRAME_SIDE * 0.205).toInt),
                                            new Dimension((Settings.FRAME_SIDE * 0.55).toInt,(Settings.FRAME_SIDE * 0.61).toInt))
  val pokemonProgressBarPose: Array[Dimension] = Array(new Dimension((Settings.FRAME_SIDE * 0.214).toInt,(Settings.FRAME_SIDE * 0.2155).toInt),
                                            new Dimension((Settings.FRAME_SIDE * 0.7).toInt,(Settings.FRAME_SIDE * 0.613).toInt))
  val pokemonLifePose: Dimension = new Dimension((Settings.FRAME_SIDE * 0.845).toInt,(Settings.FRAME_SIDE * 0.625).toInt)
  val pokemonLevExpPose: Dimension = new Dimension((Settings.FRAME_SIDE * 0.64).toInt,(Settings.FRAME_SIDE * 0.666).toInt)
  var pokemonEntities: Array[PokemonWithLife] = Array(otherPoke,myPoke)
  var pokemonImages: Array[JButton] = Array(new JButton(),new JButton())
  var pokemonSide: Array[String] = Array(Settings.POKEMON_IMAGES_FRONT_FOLDER,Settings.POKEMON_IMAGES_BACK_FOLDER)
  var pokemonNames: Array[JTextField] = Array(new JTextField(),new JTextField())
  var pokemonLevels: Array[JTextField] = Array(new JTextField(),new JTextField())
  var pokemonProgressBar: Array[JProgressBar] = Array(new JProgressBar(0, 100),new JProgressBar(0, 100))
  var myPokemonLife: JTextField = new JTextField()
  val pokemonLevExpBar: JProgressBar = new JProgressBar(0, 100)
  var pokeball: Int = 3

  this.setLayout(null)

  for(i <- pokemonImages.indices) {
    createJTextField(pokemonNames(i),pokemonEntities(i).pokemon.name.toUpperCase)
    pokemonNames(i).setBounds(pokemonNamePose(i).width,pokemonNamePose(i).height,200,30)
    pokemonNames(i).setFont(new Font("Verdana", Font.BOLD, fontSize))
    createJTextField(pokemonLevels(i),"L."+pokemonEntities(i).pokemon.level)
    pokemonLevels(i).setBounds(pokemonLevelPose(i).width,pokemonLevelPose(i).height,70,30)
    pokemonLevels(i).setFont(new Font("Verdana", Font.PLAIN, fontSize))

    var myImage: Image = ImageIO.read(getClass.getResource(pokemonSide(i) + pokemonEntities(i).pokemon.id + ".png"))
    var myImageIcon: ImageIcon = new ImageIcon(myImage.getScaledInstance(imagePokemonSize,imagePokemonSize,java.awt.Image.SCALE_SMOOTH))
    pokemonImages(i).setIcon(myImageIcon)
    pokemonImages(i).setBounds(pokemonImgPose(i).width,pokemonImgPose(i).height,imagePokemonSize,imagePokemonSize)
    pokemonImages(i).setBorder(null)
    pokemonImages(i).setContentAreaFilled(false)
    this.add(pokemonImages(i))

    pokemonProgressBar(i).setBounds(pokemonProgressBarPose(i).width,pokemonProgressBarPose(i).height,progressBarW,progressBarH)
    pokemonProgressBar(i).setValue(100)
    this.add(pokemonProgressBar(i))
  }
  createJTextField(myPokemonLife,pokemonEntities(1).pokemonLife+"/"+pokemonEntities(1).pokemon.experiencePoints)
  myPokemonLife.setBounds(pokemonLifePose.width,pokemonLifePose.height,100,30)
  myPokemonLife.setFont(new Font("Verdana", Font.PLAIN, fontLifeSize))
  this.add(myPokemonLife)
  pokemonLevExpBar.setValue(23)
  pokemonLevExpBar.setBounds(pokemonLevExpPose.width,pokemonLevExpPose.height,170,8)
  this.add(pokemonLevExpBar)

  private def createJTextField(jTextField: JTextField,text: String): Unit = {
    jTextField.setText(text)
    jTextField.setOpaque(false)
    jTextField.setBorder(null)
    this.add(jTextField)
  }

  val southPanel: JPanel = new JPanel(new BorderLayout())
  southPanel.setBounds(Settings.FRAME_SIDE/40,(Settings.FRAME_SIDE*0.72).toInt,(Settings.FRAME_SIDE*0.9).toInt,Settings.FRAME_SIDE/5)
  southPanel.setOpaque(false)

  val southWestPanel: JPanel = new JPanel()
  val gridLayout: GridLayout = new GridLayout(4,1)
  gridLayout.setVgap(10)
  southWestPanel.setLayout(gridLayout)
  southWestPanel.setOpaque(false)

  val b1: JButton = new JButton("Combatti")
  val b2: JButton = new JButton("Cambia pokemon")
  val b3: JButton = new JButton("Pokeball (x3)")
  val b4: JButton = new JButton("Fuga")
  southWestPanel.add(b1)
  southWestPanel.add(b2)
  southWestPanel.add(b3)
  southWestPanel.add(b4)

  val southEastPanel: JPanel = new JPanel()
  val gridLayoutEast: GridLayout = new GridLayout(2,2)
  gridLayoutEast.setVgap(10)
  gridLayoutEast.setHgap(10)
  southEastPanel.setLayout(gridLayoutEast)
  southEastPanel.setBounds(Settings.FRAME_SIDE/35,(Settings.FRAME_SIDE*0.72).toInt,Settings.FRAME_SIDE,Settings.FRAME_SIDE/5)
  southEastPanel.setOpaque(false)

  val attack1: JButton = new JButton(PokedexConnect.getPokemonAttack(pokemonEntities(1).pokemon.attacks._1).get()._1.toUpperCase)
  val attack2: JButton = new JButton(PokedexConnect.getPokemonAttack(pokemonEntities(1).pokemon.attacks._2).get()._1.toUpperCase)
  val attack3: JButton = new JButton(PokedexConnect.getPokemonAttack(pokemonEntities(1).pokemon.attacks._3).get()._1.toUpperCase)
  val attack4: JButton = new JButton(PokedexConnect.getPokemonAttack(pokemonEntities(1).pokemon.attacks._4).get()._1.toUpperCase)
  southEastPanel.add(attack1)
  southEastPanel.add(attack2)
  southEastPanel.add(attack3)
  southEastPanel.add(attack4)

  southPanel.add(southWestPanel,BorderLayout.WEST)
  southPanel.add(southEastPanel,BorderLayout.CENTER)
  this.add(southPanel)

  frame.getRootPane().setDefaultButton(b1)
  b1.addActionListener(e => println(b1.getText))
  b2.addActionListener(e => println(b2.getText))
  b3.addActionListener(e => {
    println(b3.getText)
    pokeball = pokeball - 1
    b3.setText("Pokeball (x"+pokeball+")")
    if(pokeball == 0){
      b3.setEnabled(false)
    }
  })
  b4.addActionListener(e => println(b4.getText))
  val provaButtons: Array[JButton] = Array(b1,b2,b3,b4)


  this.setFocusable(true)
  this.requestFocusInWindow
  this.addKeyListener(new KeyListener {
    var i: Int = 0
    override def keyPressed(e: KeyEvent): Unit = {
      e.getKeyCode match {
        case KeyEvent.VK_DOWN => {
          if(i<provaButtons.size-1){
            i = i+1
            frame.getRootPane().setDefaultButton(provaButtons(i))
          }
        }
        case KeyEvent.VK_UP =>{
          if(i>0) {
            i = i-1
            frame.getRootPane().setDefaultButton(provaButtons(i))
          }
        }
        case _ =>
      }
    }
    override def keyTyped(e: KeyEvent): Unit = {}
    override def keyReleased(e: KeyEvent): Unit = {}
  })

  def setPokemonLife(life: Int): Unit = {
    myPokemonLife.setText(pokemonEntities(1).pokemonLife+"/"+pokemonEntities(1).pokemon.experiencePoints)
  }

  def setPokemonLifeProgressBar(life: Int, owner: Owner.Value): Unit = {
    if (owner == Owner.TRAINER) pokemonProgressBar(1).setValue(life) else pokemonProgressBar(0).setValue(life)
  }

}


object CreazionePokemonMain extends App {
  val frame: JFrame = new JFrame()
  frame.setSize(Settings.FRAME_SIDE, Settings.FRAME_SIDE)
  frame.setResizable(false)
  frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
  var myPokemon: PokemonWithLife = PokemonFactory.createPokemon(Owner.WILD,Optional.empty(),Optional.of(25)).get()
  val panel: CreazionePokemon = new CreazionePokemon(myPokemon,
    PokemonFactory.createPokemon(Owner.WILD,Optional.empty(),Optional.of(25)).get(),frame)
  frame.add(panel)
  myPokemon.loseLifePoints(30)
  panel.setPokemonLife(myPokemon.pokemonLife)
  val lifeRatio: Double = myPokemon.pokemonLife.toDouble/myPokemon.pokemon.experiencePoints.toDouble
  panel.setPokemonLifeProgressBar((lifeRatio*100).toInt,Owner.TRAINER)
  frame.setVisible(true)
  panel.requestFocusInWindow()

}