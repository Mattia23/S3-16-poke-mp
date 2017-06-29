package model.entities

import java.util.Optional

import database.remote.DBConnect
import utilities.Settings

trait Trainer {
  def experiencePoints: Int

  def experiencePoints_= (points: Int) : Unit

  def level : Int

  def level_=(level: Int) : Unit

  def image: String

  //def coordinate: Coordinate

  //def coordinate_=(coordinate: Coordinate): Unit

  def pokedex: Pokedex

  def favouritePokemons: List[Int]

  def favouritePokemons_= (list: List[Int]) : Unit

  def capturedPokemons: List[Tuple2[Int,Int]]

  def capturedPokemons_= (list: List[Tuple2[Int,Int]]) : Unit

  def updateTrainer(points:Int): Unit

  def changeFavouritePokemon(idNewPokemon: Int, idOldPokemon: Int): Unit

  def addFavouritePokemon(idNewPokemon: Int): Unit

  def addMetPokemon(pokemon: Int): Unit

  def getFirstAvailableFavouritePokemon: Int
}

class TrainerImpl(val name: String, val image: String, private var _experiencePoints: Int) extends Trainer{
  private var _level: Int = calculateLevel(experiencePoints)
  //var coordinate: Coordinate
  private var _pokedex: Pokedex = new PokedexImpl(name)
  private var _favouritePokemons: List[Int] = DBConnect.getFavouritePokemonList(name).get()
  private var _capturedPokemons: List[Tuple2[Int,Int]] = DBConnect.getCapturedPokemonList(name).get()

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

  //override def coordinate = this.coordinate

  override def updateTrainer(points: Int): Unit = {
    this.experiencePoints_=(this.experiencePoints + points)
    this.level_=(calculateLevel(this.experiencePoints))
    DBConnect.updateTrainer(this.name, this.experiencePoints, this.favouritePokemons)
  }

  override def changeFavouritePokemon(idNewPokemon: Int, idOldPokemon: Int): Unit = {
    var pos = 0
    if(this.capturedPokemons.toMap.get(idNewPokemon).isDefined){
      for(pokemon <- this.favouritePokemons){
        pos += 1
        if(pokemon == idOldPokemon){
          DBConnect.addFavouritePokemon(name,idNewPokemon,pos)
        }
      }
      this.favouritePokemons_=(DBConnect.getFavouritePokemonList(name).get())
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
          DBConnect.addFavouritePokemon(name,idNewPokemon,pos)
          found = 1
        }
      }
      this.favouritePokemons_=(DBConnect.getFavouritePokemonList(name).get())
    } else if(this.favouritePokemons.contains(idNewPokemon)){
      println("you already have this pokemon in your list")
    } else if(this.capturedPokemons.toMap.get(idNewPokemon).isEmpty){
      println("Error: you tried to add a pokemon you haven't captured in your favourite list!!!")
    }
  }

  override def addMetPokemon(pokemon: Int): Unit = {
    if(!this.pokedex.checkIfAlreadyMet(pokemon)){
      DBConnect.addMetPokemon(this.name,pokemon)
      this.pokedex.pokedex_=(DBConnect.getMetPokemonList(name).get())
    } else {
      println("You have already met this pokemon")
    }
  }

  override def getFirstAvailableFavouritePokemon: Int = {
    for(pokemon <- this._favouritePokemons){
      if(DBConnect.pokemonIsLive(pokemon)){
        return pokemon
      }
    }
    -1
  }
}

