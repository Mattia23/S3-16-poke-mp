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

  def favouritePokemons: List[Int]

  def favouritePokemons_=(list: List[Int]) : Unit

  def capturedPokemons: List[Tuple2[Int,Int]]

  def capturedPokemons_=(list: List[Tuple2[Int,Int]]) : Unit

  def capturedPokemonId: List[Int]

  def capturedPokemonId_=(list: List[Int]): Unit

  def updateTrainer(points:Int): Unit

  def changeFavouritePokemon(idNewPokemon: Int, idOldPokemon: Int): Unit

  def addFavouritePokemon(idNewPokemon: Int): Unit

  def addMetPokemon(pokemon: Int): Unit

  def getFirstAvailableFavouritePokemon: Int
}

class TrainerImpl(val name: String, private val idImage: Int, private var _experiencePoints: Int) extends Trainer{
  val id: Int = DBConnect.getTrainerIdFromUsername(name).get()
  private var _level: Int = calculateLevel(experiencePoints)
  private var _coordinate: Coordinate = CoordinateImpl(25,25)
  private var _pokedex: Pokedex = new PokedexImpl(id)
  private var _favouritePokemons: List[Int] = DBConnect.getFavouritePokemonList(id).get()
  private var _capturedPokemons: List[Tuple2[Int,Int]] = DBConnect.getCapturedPokemonList(id).get()
  override var capturedPokemonId: List[Int] = DBConnect.getCapturedPokemonIdList(id).get()

  override val sprites =  idImage match {
    case 1 => Trainer1()
    case 2 => Trainer2()
    case 3 => Trainer3()
    case _ => Trainer4()
  }

  override def experiencePoints: Int = this._experiencePoints

  override def experiencePoints_=(points: Int): Unit = this._experiencePoints = points

  override def level: Int = this._level

  override def level_=(level: Int): Unit = this._level = level

  override def pokedex: Pokedex = this._pokedex

  override def favouritePokemons: List[Int] = this._favouritePokemons

  override def favouritePokemons_=(list: List[Int]): Unit = this._favouritePokemons = list

  override def capturedPokemons: List[Tuple2[Int,Int]] = this._capturedPokemons

  override def capturedPokemons_=(list: List[Tuple2[Int,Int]]): Unit = this._capturedPokemons = list

  private def calculateLevel(experiencePoints: Int): Int = {
    var level: Double = Settings.INITIAL_TRAINER_LEVEL
    var step: Double = Settings.LEVEL_STEP
    while(experiencePoints > step ){
      step = step + Settings.LEVEL_STEP*math.pow(2,level)
      level += 1
    }
    level.toInt
  }

  override def coordinate: Coordinate = this._coordinate

  override def coordinate_=(coordinate: Coordinate): Unit = this._coordinate = coordinate

  override def updateTrainer(points: Int): Unit = {
    this.experiencePoints_=(this.experiencePoints + points)
    this.level_=(calculateLevel(this.experiencePoints))
    DBConnect.updateTrainer(this.id, this.experiencePoints, this.favouritePokemons)
  }

  override def changeFavouritePokemon(idNewPokemon: Int, idOldPokemon: Int): Unit = {
    var pos = 0
    if(idNewPokemon==0 || this.capturedPokemons.toMap.get(idNewPokemon).isDefined){
      for(pokemon <- this.favouritePokemons){
        pos += 1
        if(pokemon == idOldPokemon){
          DBConnect.addFavouritePokemon(id,idNewPokemon,pos)
        }
      }
      this.favouritePokemons_=(DBConnect.getFavouritePokemonList(id).get())
    } else {
      println("Error: you tried to add a pokemon you haven't captured in your favourite list!!!")
    }
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
      this.favouritePokemons_=(DBConnect.getFavouritePokemonList(id).get())
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
    for(pokemon <- this._favouritePokemons){
      if(DBConnect.pokemonHasLife(pokemon)){
        return pokemon
      }
    }
    -1
  }
}

