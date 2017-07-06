import java.util.Optional
import javax.swing.JTextField

import database.remote.DBConnect
import model.entities._
import org.scalatest.FunSuite

class TrainerTest extends FunSuite{

  def fixture =
    new {
      DBConnect.deleteUserAndRelatedData("prova")
      val map = scala.collection.mutable.Map[String,JTextField]()
      val name = new JTextField("prova")
      val surname = new JTextField("prova")
      val email = new JTextField("prova@prova.it")
      val username = new JTextField("prova")
      val password = new JTextField("prova")
      map += "Name" -> name
      map += "Surname" -> surname
      map += "Email" -> email
      map += "Username" -> username
      map += "Password" -> password
      DBConnect.insertCredentials(collection.JavaConverters.mapAsJavaMap(map),3)
      val trainer: Trainer = DBConnect.getTrainerFromDB("prova").get()
      val pokemon: PokemonWithLife = PokemonFactory.createPokemon(Owner.WILD,Optional.empty(),Optional.of(10)).get()
      val pokemonBehaviour: PokemonBehaviour = new PokemonBehaviourImpl(pokemon)
      //val autoIncrementMet = DBConnect.getAutoIncrement("pokemon_met")
      val autoIncrementCaptured: Int = DBConnect.getAutoIncrement("pokemon")
    }

  test("New pokemon met, update pokedex") {
    val f = fixture
    f.trainer.addMetPokemon(23)
    assert(f.trainer.pokedex.pokedex == List(23))
    f.trainer.addMetPokemon(24)
    assert(f.trainer.pokedex.pokedex == List(23,24))
    f.trainer.addMetPokemon(23)
    assert(f.trainer.pokedex.pokedex == List(23,24))
  }

  test("New pokemon added to favourite list") {
    val f = fixture
    f.pokemonBehaviour.insertPokemonIntoDB(f.trainer.id)
    f.pokemonBehaviour.insertPokemonIntoDB(f.trainer.id)
    f.trainer.capturedPokemons_=(DBConnect.getCapturedPokemonList(f.trainer.id).get())
    f.trainer.addFavouritePokemon(f.autoIncrementCaptured)
    assert(f.trainer.favouritePokemons == List(f.autoIncrementCaptured,0,0,0,0,0))
    f.trainer.addFavouritePokemon(f.autoIncrementCaptured+1)
    assert(f.trainer.favouritePokemons == List(f.autoIncrementCaptured,f.autoIncrementCaptured+1,0,0,0,0))
    f.trainer.addFavouritePokemon(f.autoIncrementCaptured)
    assert(f.trainer.favouritePokemons == List(f.autoIncrementCaptured,f.autoIncrementCaptured+1,0,0,0,0))
  }

  test("Change pokemon in favourite list") {
    val f = fixture
    f.pokemonBehaviour.insertPokemonIntoDB(f.trainer.id)
    f.pokemonBehaviour.insertPokemonIntoDB(f.trainer.id)
    f.trainer.capturedPokemons_=(DBConnect.getCapturedPokemonList(f.trainer.id).get())
    f.trainer.addFavouritePokemon(f.autoIncrementCaptured)
    f.trainer.changeFavouritePokemon(f.autoIncrementCaptured+1,f.autoIncrementCaptured)
    assert(f.trainer.favouritePokemons == List(f.autoIncrementCaptured+1,0,0,0,0,0))
  }

  test("Update trainer") {
    val f = fixture
    assert(f.trainer.experiencePoints == 0)
    assert(f.trainer.level == 1)
    f.trainer.updateTrainer(200)
    assert(f.trainer.experiencePoints == 200)
    assert(f.trainer.level == 3)
  }

  test("Get first pokemon with life in favourite pokemon list") {
    val f = fixture
    f.pokemonBehaviour.insertPokemonIntoDB(f.trainer.id)
    f.pokemonBehaviour.insertPokemonIntoDB(f.trainer.id)
    f.trainer.capturedPokemons_=(DBConnect.getCapturedPokemonList(f.trainer.id).get())
    f.trainer.addFavouritePokemon(f.autoIncrementCaptured)
    f.trainer.addFavouritePokemon(f.autoIncrementCaptured+1)
    assert(f.trainer.getFirstAvailableFavouritePokemon == f.autoIncrementCaptured)
    f.pokemonBehaviour.undergoAttack(100000)
    f.pokemonBehaviour.updatePokemonTrainer(f.autoIncrementCaptured)
    assert(f.trainer.getFirstAvailableFavouritePokemon == f.autoIncrementCaptured+1)
  }

}
