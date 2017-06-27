package model.entities

import java.util.Optional
import database.DBConnect
import org.json.simple.JSONObject

object Owner extends Enumeration {
  val TRAINER, WILD = Value
}

case class Pokemon(
  id: Int,
  name: String,
  pokemonType: String,
  attacks: (Int,Int,Int,Int),
  level: Int,
  experiencePoints: Int,
  idImage: Int
)

object PokemonFactory {

  def createPokemon(owner: Owner.Value, databaseId: Optional[Int]): (Pokemon,Int) = owner match {
    case Owner.TRAINER => {
      var optionalJSON: Optional[JSONObject] = DBConnect.getPokemonFromDB(databaseId.get())
      if(optionalJSON.isPresent) {
        var pokemonJSON: JSONObject = optionalJSON.get
        (Pokemon(id = pokemonJSON.get("id").toString.toInt, name = pokemonJSON.get("name").toString,
          pokemonType = pokemonJSON.get("pokemonType").toString, attacks = pokemonJSON.get("attacks").asInstanceOf[(Int,Int,Int,Int)],
          level = pokemonJSON.get("level").toString.toInt, experiencePoints = pokemonJSON.get("experiencePoints").toString.toInt,
          idImage = pokemonJSON.get("idImage").toString.toInt), pokemonJSON.get("lifePoints").toString.toInt)
      } else {
        (Pokemon(id = 4, name = "Pikachu", pokemonType = "Elettricità", attacks = (1,2,3,4),
          level = 6, experiencePoints = 60, idImage = 4),3)
      }
    }
    case Owner.WILD => {
      (Pokemon(id = 4, name = "Pikachu", pokemonType = "Elettricità", attacks = (1,2,3,4),
        level = 6, experiencePoints = 60, idImage = 4),3)
    }
  }

}

object TryPokemon extends App {

  var bulbasaur: (Pokemon,Int) = PokemonFactory.createPokemon(Owner.TRAINER,Optional.of(1))

  var pikachu: (Pokemon,Int) = PokemonFactory.createPokemon(Owner.WILD,Optional.empty())

  val prova: Prova = new Prova(bulbasaur,pikachu)

}