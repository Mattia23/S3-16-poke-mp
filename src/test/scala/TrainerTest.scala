import java.util.Optional
import javax.swing.JTextField

import database.remote.DBConnect
import model.entities.TryTrainer.trainer
import model.entities.{Owner, Pokemon, PokemonFactory, Trainer}
import org.scalatest.FunSuite

class TrainerTest extends FunSuite{

  def fixture =
    new {
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
      val pokemon: (Pokemon,Int) = PokemonFactory.createPokemon(Owner.TRAINER,Optional.of(1))
      //val autoIncrementMet = DBConnect.getAutoIncrement("pokemon_met")
      val autoIncrementCaptured = DBConnect.getAutoIncrement("pokemon")
    }

  test("New pokemon met, update pokedex") {
    val f = fixture
    f.trainer.addMetPokemon(23)
    assert(f.trainer.pokedex.pokedex == List(23))
    f.trainer.addMetPokemon(24)
    assert(f.trainer.pokedex.pokedex == List(23,24))
    f.trainer.addMetPokemon(23)
    assert(f.trainer.pokedex.pokedex == List(23,24))
    DBConnect.deleteUserAndRelatedData("prova")
  }

  test("New pokemon captured") {
    val f = fixture
    f.trainer.addCapturedPokemon(f.pokemon._1)
    assert(f.trainer.pokedex.pokedex == List(4))
    assert(f.trainer.capturedPokemons == List((f.autoIncrementCaptured,4)))
    f.trainer.addCapturedPokemon(f.pokemon._1)
    assert(f.trainer.pokedex.pokedex == List(4))
    assert(f.trainer.capturedPokemons == List((f.autoIncrementCaptured,4),(f.autoIncrementCaptured+1,4)))
    DBConnect.deleteUserAndRelatedData("prova")
  }

  test("New pokemon added to favourite list") {
    val f = fixture
    f.trainer.addCapturedPokemon(f.pokemon._1)
    f.trainer.addCapturedPokemon(f.pokemon._1)
    f.trainer.addFavouritePokemon(f.autoIncrementCaptured)
    assert(f.trainer.favouritePokemons == List(f.autoIncrementCaptured,0,0,0,0,0))
    f.trainer.addFavouritePokemon(f.autoIncrementCaptured+1)
    assert(f.trainer.favouritePokemons == List(f.autoIncrementCaptured,f.autoIncrementCaptured+1,0,0,0,0))
    f.trainer.addFavouritePokemon(f.autoIncrementCaptured)
    assert(f.trainer.favouritePokemons == List(f.autoIncrementCaptured,f.autoIncrementCaptured+1,0,0,0,0))
    DBConnect.deleteUserAndRelatedData("prova")
  }

  test("Change pokemon in favourite list") {
    val f = fixture
    f.trainer.addCapturedPokemon(f.pokemon._1)
    f.trainer.addCapturedPokemon(f.pokemon._1)
    f.trainer.addFavouritePokemon(f.autoIncrementCaptured)
    f.trainer.changeFavouritePokemon(f.autoIncrementCaptured+1,f.autoIncrementCaptured)
    assert(f.trainer.favouritePokemons == List(f.autoIncrementCaptured+1,0,0,0,0,0))
    DBConnect.deleteUserAndRelatedData("prova")
  }

  test("Update trainer") {
    val f = fixture
    assert(f.trainer.experiencePoints == 0)
    assert(f.trainer.level == 0)
    f.trainer.updateTrainer(200)
    assert(f.trainer.experiencePoints == 200)
    assert(f.trainer.level == 2)
    DBConnect.deleteUserAndRelatedData("prova")
  }

}
