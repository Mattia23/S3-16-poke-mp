import java.util.Optional
import javax.swing.{JFrame, JPanel}

import model.entities.{Owner, PokemonFactory, PokemonWithLife}
import database.local.PokedexConnect
import org.scalatest.FunSuite

class PokemonDBTest extends FunSuite {

  test("Check pokemon name") {
    assert(PokedexConnect.getPokemonName(1).get() == "bulbasaur")
    info("getPokemonName works correctly")
  }

  test("Check pokemon attacks") {
    assert(PokedexConnect.getPokemonAttack(1).get()._1 == "pound")
    assert(PokedexConnect.getPokemonAttack(1).get()._2 == 40)
    assert(PokedexConnect.getPokemonAttack(2).get()._1 == "karate-chop")
    assert(PokedexConnect.getPokemonAttack(2).get()._2 == 50)
    info("getPokemonAttack works correctly")
  }

  test("Check pokemon evolutions") {
    assert(PokedexConnect.pokemonHasToEvolve(1,15)._1 == false)
    assert(PokedexConnect.pokemonHasToEvolve(1,16)._1 == true)
    assert(PokedexConnect.pokemonHasToEvolve(1,16)._2.get() == 2)
    info("pokemonHasToEvolve works correctly")
  }

  test("Check the lists of possible wild pokemon") {
    assert(PokedexConnect.getPossibleWildPokemon(1).get().size == 10)
    assert(PokedexConnect.getPossibleWildPokemon(5).get().size == 12)
    assert(PokedexConnect.getPossibleWildPokemon(10).get().size == 42)
    assert(PokedexConnect.getPossibleWildPokemon(20).get().size == 32)
    assert(PokedexConnect.getPossibleWildPokemon(30).get().size == 30)
    assert(PokedexConnect.getPossibleWildPokemon(50).get().size == 10)
    assert(PokedexConnect.getPossibleWildPokemon(100).get().size == 6)
    info("getPossibleWildPokemon works correctly")
  }

  test("Check the level range of possible wild pokemon") {
    assert(PokedexConnect.getMinLevelWildPokemon(1) == 1)
    assert(PokedexConnect.getMaxLevelWildPokemon(1) == 16)
    assert(PokedexConnect.getMinLevelWildPokemon(2) == 16)
    assert(PokedexConnect.getMaxLevelWildPokemon(2) == 32)
    assert(PokedexConnect.getMinLevelWildPokemon(3) == 32)
    assert(PokedexConnect.getMaxLevelWildPokemon(3) == 70)
    info("getMinLevelWildPokemon and getMaxLevelWildPokemon works correctly")
  }

  test("Create new pokemon wild") {
    val p1: PokemonWithLife = PokemonFactory.createPokemon(Owner.WILD,Optional.empty(),Optional.of(5)).get()
    val p2: PokemonWithLife = PokemonFactory.createPokemon(Owner.WILD,Optional.empty(),Optional.of(15)).get()
    val p3: PokemonWithLife = PokemonFactory.createPokemon(Owner.WILD,Optional.empty(),Optional.of(25)).get()
    println("1) ID:"+p1.pokemon.id+" NAME:"+p1.pokemon.name+" LEVEL:"+p1.pokemon.level+" EXP:"+p1.pokemon.experiencePoints+" LEV_EXP:"+p1.pokemon.levelExperience+" LIFE:"+p1.pokemonLife)
    println("2) ID:"+p2.pokemon.id+" NAME:"+p2.pokemon.name+" LEVEL:"+p2.pokemon.level+" EXP:"+p2.pokemon.experiencePoints+" LEV_EXP:"+p2.pokemon.levelExperience+" LIFE:"+p2.pokemonLife)
    println("3) ID:"+p3.pokemon.id+" NAME:"+p3.pokemon.name+" LEVEL:"+p3.pokemon.level+" EXP:"+p3.pokemon.experiencePoints+" LEV_EXP:"+p3.pokemon.levelExperience+" LIFE:"+p3.pokemonLife)
  }

}
