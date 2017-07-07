import java.util.Optional
import javax.swing.JTextField

import database.remote.DBConnect
import model.entities._
import model.game.{Battle, BattleImpl}
import org.scalatest.FunSuite

class BattleTest extends FunSuite{

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
      pokemonBehaviour.insertPokemonIntoDB(trainer.id)
      pokemonBehaviour.insertPokemonIntoDB(trainer.id)
      trainer.capturedPokemons_=(DBConnect.getCapturedPokemonList(trainer.id).get())
      trainer.addFavouritePokemon(autoIncrementCaptured)
      trainer.addFavouritePokemon(autoIncrementCaptured+1)
    }

  test("Battle simulation") {
    val f = fixture
    var battle: Battle = new BattleImpl(f.trainer)
    battle.startBattleRound(battle.trainer.getFirstAvailableFavouritePokemon)
    println("pokemon catturato: " + battle.round.pokeballLaunched())
    battle.round.myPokemonAttack(1)
    println("pokemon catturato: " + battle.round.pokeballLaunched())

  }

}
