package model.entities

import database.remote.DBConnect
import model.environment.{Coordinate, CoordinateImpl}
import utilities.Settings

trait Trainer {
  def name: String

  def id: Int

  def experiencePoints: Int

  def experiencePoints_= (points: Int) : Unit

  def level : Int

  def level_=(level: Int) : Unit

  def sprites: TrainerSprites

  def coordinate: Coordinate

  def coordinate_=(coordinate: Coordinate): Unit

  def pokedex: Pokedex

  def pokedex_=(pokedex: Pokedex)

  def favouritePokemons: List[Int]

  def favouritePokemons_=(list: List[Int]) : Unit

  def capturedPokemons: List[Tuple2[Int,Int]]

  def capturedPokemons_=(list: List[Tuple2[Int,Int]]) : Unit

  def capturedPokemonId: List[Int]

  def capturedPokemonId_=(list: List[Int]): Unit

  def updateTrainer(points:Int): Unit

  def setAllFavouritePokemon(list: java.util.List[Object]): Unit

  def addFavouritePokemon(idNewPokemon: Int): Unit

  def addMetPokemon(pokemon: Int): Unit

  def getFirstAvailableFavouritePokemon: Int
}

class TrainerImpl(override val name: String, private val idImage: Int, override var experiencePoints: Int) extends Trainer{
  override val id: Int = DBConnect.getTrainerIdFromUsername(name).get()
  override var level: Int = calculateLevel()
  private var _coordinate: Coordinate = Settings.INITIAL_PLAYER_POSITION
  override var pokedex: Pokedex = new PokedexImpl(id)
  override var favouritePokemons: List[Int] = DBConnect.getFavouritePokemonList(id).get()
  override var capturedPokemons: List[Tuple2[Int,Int]] = DBConnect.getCapturedPokemonList(id).get()
  override var capturedPokemonId: List[Int] = DBConnect.getCapturedPokemonIdList(id).get()

  override val sprites: TrainerSprites = TrainerSprites.selectTrainerSprite(idImage)

  private def calculateLevel(): Int = {
    var level: Double = Settings.INITIAL_TRAINER_LEVEL
    var step: Double = Settings.LEVEL_STEP
    while(this.experiencePoints > step ){
      step = step + Settings.LEVEL_STEP*math.pow(2,level)
      level += 1
    }
    level.toInt
  }

  override def coordinate: Coordinate = this._coordinate

  override def coordinate_=(coordinate: Coordinate): Unit = this._coordinate = coordinate

  override def updateTrainer(points: Int): Unit = {
    this.experiencePoints_=(this.experiencePoints + points)
    this.level_=(calculateLevel())
    capturedPokemons_=(DBConnect.getCapturedPokemonList(id).get())
    capturedPokemonId_=(DBConnect.getCapturedPokemonIdList(id).get())
    DBConnect.updateTrainer(this.id, this.experiencePoints, this.favouritePokemons)
  }

  override def setAllFavouritePokemon(list: java.util.List[Object]): Unit = {
    DBConnect.setAllFavouritePokemon(id, list)
    this.favouritePokemons = DBConnect.getFavouritePokemonList(id).get()
  }

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

  override def addMetPokemon(pokemon: Int): Unit = {
    if(!this.pokedex.checkIfAlreadyMet(pokemon)){
      DBConnect.addMetPokemon(this.id,pokemon)
      this.pokedex.pokedex_=(DBConnect.getMetPokemonList(id).get())
    }
  }

  override def getFirstAvailableFavouritePokemon: Int = {
    for(pokemon <- this.favouritePokemons){
      if(DBConnect.pokemonHasLife(pokemon)){
        return pokemon
      }
    }
    -1
  }
}

