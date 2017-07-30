package model.entities

import database.remote.DBConnect
import model.environment.{Coordinate, CoordinateImpl}
import utilities.Settings

/**
  * A trainer represents a player in the game and has his attributes stored in the remote
  * database. A trainer can move on the map and fight against wild Pokemon or other trainers.
  */
trait Trainer {
  /**
    * Return the username of the playing trainer as a String
    * @return the username of this Trainer
    */
  def username: String

  /**
    * Return the database ID of the playing trainer as an Int
    * @return the database ID of this trainer
    */
  def id: Int

  /**
    * Return the experience points of the playing trainer as an Int
    * @return the experience points of this trainer
    */
  def experiencePoints: Int

  /**
    * Set the experience points of the trainer
    * @param points the new trainer's experience points
    */
  def experiencePoints_= (points: Int) : Unit


  /**
    * Return the level of the playing trainer as an Int
    * @return the trainer's level
    */
  def level : Int

  /**
    * Set the level of the playing trainer
    * @param level the new trainer's level
    */
  def level_=(level: Int) : Unit

  /**
    * Return all the possible sprites of the playing trainer(different based on player's movement direction)
    * @return all the trainer's sprites
    */
  def sprites: TrainerSprites

  /**
    * Return the current trainer sprite in the map
    * @return current trainer sprite
    */
  def currentSprite: Sprite

  /**
    * Set the current trainer sprite for the map
    * @param sprite the new trainer sprite
    */
  def currentSprite_=(sprite: Sprite): Unit

  /**
    * Return the current trainer coordinate on the map
    * @return current trainer coordinate
    */
  def coordinate: Coordinate

  /**
    * Set the trainer coordinate on the map
    * @param coordinate the new trainer coordinate
    */
  def coordinate_=(coordinate: Coordinate): Unit

  /**
    * Return the trainer Pokedex that contains all the Pokemon met IDs
    * @return trainer Pokedex
    */
  def pokedex: Pokedex

  /**
    * Set the trainer Pokedex
    * @param pokedex the new Pokedex
    */
  def pokedex_=(pokedex: Pokedex)

  /**
    * Return the list of the trainer's six favourite Pokemon IDs used in battles; if the trainer has less than six
    * favourite Pokemons, the ID is set to 0
    * @return the list of the trainer's six favourite Pokemon IDs
    */
  def favouritePokemons: List[Int]

  /**
    * Set a new favourite Pokemon list
    * @param list the new favourite Pokemon list
    */
  def favouritePokemons_=(list: List[Int]) : Unit

  /**
    * Return a list that contains a Tuple2 with the pokemon ID in the remote database (useful to have specific Pokemon
    * information like life, attacks, current level) and the pokemon ID in the local database (useful to have pokemon
    * name, evolution level...) of the Pokemons captured by the trainer
    * @return captured pokemon list
    */
  def capturedPokemons: List[Tuple2[Int,Int]]

  /**
    * Set a new captured Pokemon list
    * @param list new captured Pokemon list
    */
  def capturedPokemons_=(list: List[Tuple2[Int,Int]]) : Unit

  /**
    * Return a list that contains the pokemon ID in the local database. It used in the Pokedex.
    * @return captured pokemon list
    */
  def capturedPokemonId: List[Int]

  /**
    * Set a new captured Pokemon list
    * @param list new captured Pokemon list
    */
  def capturedPokemonId_=(list: List[Int]): Unit

  /**
    * Update trainer's experience points, level and captured Pokemon. It can be used after a battle
    * @param earnedPoints the points earned by the trainer that will be added to the points the trainer already had
    */
  def updateTrainer(earnedPoints:Int): Unit

  /**
    * Set the trainer's favourite pokemon list and update the remote database
    * @param list new trainer's favourite pokemon list
    */
  def setAllFavouritePokemon(list: java.util.List[Object]): Unit

  /**
    * Add a new Pokemon in the six favourite list
    * @param idNewPokemon pokemon ID in the remote database
    */
  def addFavouritePokemon(idNewPokemon: Int): Unit

  /**
    * Add a new met Pokemon in the trainer's Pokedex and in the remote database
    * @param pokemonId the pokemon ID in the local database
    */
  def addMetPokemon(pokemonId: Int): Unit

  /**
    * Return the first Pokemon that can fight (with life more than 0) from the favourite Pokemon list. If none of the
    * Pokemon is available it returns -1
    * @return the remote database ID of the first pokemon available to fight
    */
  def getFirstAvailableFavouritePokemon: Int
}

/**
  * @inheritdoc
  * @param username trainer's username from remote DB
  * @param idImage trainer's ID image from remote DB
  * @param experiencePoints trainer's experience points from remote DB
  */
class TrainerImpl(override val username: String, private val idImage: Int, override var experiencePoints: Int) extends Trainer{

  override var coordinate: Coordinate = Settings.INITIAL_PLAYER_POSITION
  override val id: Int = DBConnect.getTrainerIdFromUsername(username).get()
  override var level: Int = calculateLevel()
  override val sprites: TrainerSprites = TrainerSprites.selectTrainerSprite(idImage)
  override var currentSprite: Sprite = sprites.frontS
  override var pokedex: Pokedex = new PokedexImpl(id)
  override var favouritePokemons: List[Int] = DBConnect.getFavouritePokemonList(id).get()
  override var capturedPokemons: List[Tuple2[Int,Int]] = DBConnect.getCapturedPokemonList(id).get()
  override var capturedPokemonId: List[Int] = DBConnect.getCapturedPokemonIdList(id).get()

  /**
    * Calculate and return trainer's level using the trainer's experience points
    * @return trainer's level
    */
  private def calculateLevel(): Int = {
    var level: Double = Settings.INITIAL_TRAINER_LEVEL
    var step: Double = Settings.LEVEL_STEP
    while(this.experiencePoints > step ){
      step = step + Settings.LEVEL_STEP*math.pow(2,level)
      level += 1
    }
    level.toInt
  }

  /**
    * @inheritdoc
    */
  override def updateTrainer(earnedPoints: Int): Unit = {
    this.experiencePoints_=(this.experiencePoints + earnedPoints)
    this.level_=(calculateLevel())
    this.capturedPokemons_=(DBConnect.getCapturedPokemonList(id).get())
    this.capturedPokemonId_=(DBConnect.getCapturedPokemonIdList(id).get())
    DBConnect.updateTrainer(this.id, this.experiencePoints, this.favouritePokemons)
  }

  /**
    * @inheritdoc
    */
  override def setAllFavouritePokemon(list: java.util.List[Object]): Unit = {
    DBConnect.setAllFavouritePokemon(id, list)
    this.favouritePokemons = DBConnect.getFavouritePokemonList(id).get()
  }

  /**
    * @inheritdoc
    */
  override def addFavouritePokemon(idNewPokemon: Int): Unit = {
    var pos = 0
    var found = 0
    if(!this.favouritePokemons.contains(idNewPokemon) && this.capturedPokemons.toMap.get(idNewPokemon).isDefined){
      for(pokemon <- this.favouritePokemons){
        pos += 1
        if(pokemon == 0 && found != 1){
          DBConnect.addFavouritePokemon(id,idNewPokemon,pos)
          found = 1
        }
      }
      this.favouritePokemons=DBConnect.getFavouritePokemonList(id).get()
    } else if(this.favouritePokemons.contains(idNewPokemon)){
      println("you already have this pokemon in your list")
    } else if(this.capturedPokemons.toMap.get(idNewPokemon).isEmpty){
      println("Error: you tried to add a pokemon you haven't captured in your favourite list!!!")
    }
  }

  /**
    * @inheritdoc
    */
  override def addMetPokemon(pokemon: Int): Unit = {
    if(!this.pokedex.checkIfAlreadyMet(pokemon)){
      DBConnect.addMetPokemon(this.id,pokemon)
      this.pokedex.pokedex_=(DBConnect.getMetPokemonList(id).get())
    }
  }

  /**
    * @inheritdoc
    */
  override def getFirstAvailableFavouritePokemon: Int = {
    for(pokemon <- this.favouritePokemons){
      if(DBConnect.pokemonHasLife(pokemon)){
        return pokemon
      }
    }
    -1
  }
}

