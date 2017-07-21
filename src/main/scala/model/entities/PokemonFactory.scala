package model.entities

import java.util.Optional
import database.local.PokedexConnect
import database.remote.DBConnect
import scala.collection.mutable
import scala.util.Random

object PokemonFactory {
  private final val LEVEL_STEP_ONE: Int = 10
  private final val LEVEL_STEP_TWO: Int = 20

  def createPokemon(owner: Owner.Value, databaseId: Optional[Int], trainerLevel: Optional[Int]): Optional[PokemonWithLife] = owner match {
    case Owner.TRAINER => {
      var optional: Optional[java.util.Map[_,_]] = DBConnect.getPokemonFromDB(databaseId.get())
      if(optional.isPresent) {
        var pokemonMap: java.util.Map[_,_] = optional.get()
        val pokemon: PokemonWithLife =
          new PokemonWithLifeImpl(Pokemon(id = pokemonMap.get("id").toString.toInt,
                                          name = pokemonMap.get("name").toString,
                                          attacks = pokemonMap.get("attacks").asInstanceOf[(Int,Int,Int,Int)],
                                          level = pokemonMap.get("level").toString.toInt,
                                          experiencePoints = pokemonMap.get("experiencePoints").toString.toInt,
                                          levelExperience = pokemonMap.get("levelExperience").toString.toInt,
                                          imageName = pokemonMap.get("idImage").toString),
                                  pokemonMap.get("lifePoints").toString.toInt)
        Optional.of(pokemon)
      } else {
        Optional.empty()
      }
    }
    case Owner.WILD => {
      val pokemonId: Int = generateCasualPokemonId(trainerLevel.get())
      val level: Int = generateLevelPokemon(trainerLevel.get(),pokemonId)
      val exp: Int = generateExperiencePokemon(pokemonId, level)
      val pokemon: PokemonWithLife =
        new PokemonWithLifeImpl(Pokemon(id = pokemonId,
                                        name = PokedexConnect.getPokemonName(pokemonId).get(),
                                        attacks = generateAttacksPokemon(pokemonId),
                                        level = level,
                                        experiencePoints = exp,
                                        levelExperience = Random.nextInt(exp),
                                        imageName = pokemonId+".png"),
                                exp)
      Optional.of(pokemon)
    }
    case Owner.INITIAL => {
      val pokemonId: Int = databaseId.get()
      val level: Int = 5
      val exp: Int = generateExperiencePokemon(pokemonId, level)
      val pokemon: PokemonWithLife =
        new PokemonWithLifeImpl(Pokemon(id = pokemonId,
          name = PokedexConnect.getPokemonName(pokemonId).get(),
          attacks = generateAttacksPokemon(pokemonId),
          level = level,
          experiencePoints = exp,
          levelExperience = Random.nextInt(exp),
          imageName = pokemonId+".png"),
          exp)
      Optional.of(pokemon)
    }
  }

  private def generateCasualPokemonId(trainerLevel: Int): Int = {
    var indexRandomList: mutable.Map[Range,Int] = mutable.HashMap()
    if(trainerLevel < LEVEL_STEP_ONE) {
      indexRandomList = indexRandomList += ((0 until 50) -> 1) += ((50 until 80) -> 5) += ((80 until 100) -> 10)
    } else if (trainerLevel < LEVEL_STEP_TWO) {
      indexRandomList = indexRandomList += ((0 until 35) -> 1) += ((35 until 60) -> 5) += ((60 until 80) -> 10) +=
        ((80 until 90) -> 20) += ((90 until 96) -> 30) += ((96 until 100) -> 50)
    } else {
      indexRandomList = indexRandomList += ((0 until 35) -> 1) += ((35 until 60) -> 5) += ((60 until 80) -> 10) +=
        ((80 until 90) -> 20) += ((90 until 96) -> 30) += ((96 until 99) -> 50) += ((99 until 100) -> 100)
    }
    val random: Int = Random.nextInt(100)
    var pokemonId: Int = 0
    for ((k,v) <- indexRandomList) {
      if(k.contains(random)) {
        val pokemonList: java.util.List[Integer] = PokedexConnect.getPossibleWildPokemon(v).get()
        pokemonId = pokemonList.get(Random.nextInt(pokemonList.size()))
      }
    }
    pokemonId
  }

  private def generateLevelPokemon(trainerLevel: Int, pokemonId: Int): Int = {
    val min: Int = PokedexConnect.getMinLevelWildPokemon(pokemonId)
    val max: Int = PokedexConnect.getMaxLevelWildPokemon(pokemonId)
    val x: Int = (max - min)/3
    if(trainerLevel < LEVEL_STEP_ONE) {
      min + Random.nextInt(x)
    } else if(trainerLevel < LEVEL_STEP_TWO) {
      min + x + Random.nextInt(x)
    } else {
      min + 2*x + Random.nextInt(x)
    }
  }

  private def generateExperiencePokemon(id: Int, level: Int): Int = {
    val baseExp: Int = PokedexConnect.getBaseExperiencePokemon(id).get()
    baseExp + level*4 - 4
  }

  private def generateAttacksPokemon(id: Int): (Int,Int,Int,Int) = {
    val attacksList: java.util.List[Integer] = PokedexConnect.getAttacksList(id)
    (attacksList.get(Random.nextInt(attacksList.size())),attacksList.get(Random.nextInt(attacksList.size())),
      attacksList.get(Random.nextInt(attacksList.size())),attacksList.get(Random.nextInt(attacksList.size())))
  }

}
