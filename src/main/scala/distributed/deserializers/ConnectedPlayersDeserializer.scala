package distributed.deserializers

import java.lang.reflect.Type
import java.util

import com.google.gson.{GsonBuilder, JsonDeserializationContext, JsonDeserializer, JsonElement}
import distributed.{Player, PlayerImpl}

/**
  * Deserializer from Json to util.Map[Int,Player]
  */
object ConnectedPlayersDeserializer extends JsonDeserializer[util.Map[Int, Player]] {
  override def deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): util.Map[Int, Player] = {
    val jsonConnectedPlayers = json.getAsJsonObject
    val jsonKeys = jsonConnectedPlayers.keySet()
    val gson = new GsonBuilder().registerTypeAdapter(classOf[PlayerImpl], PlayerDeserializer).create()
    val map = new util.HashMap[Int, Player]()

    jsonKeys forEach (key => map.put(key.toInt, gson.fromJson(jsonConnectedPlayers get key, classOf[PlayerImpl])))

    map
  }
}