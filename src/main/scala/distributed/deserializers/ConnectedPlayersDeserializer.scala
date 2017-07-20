package distributed.deserializers

import java.lang.reflect.Type
import java.util.concurrent.{ConcurrentHashMap, ConcurrentMap}

import com.google.gson.{GsonBuilder, JsonDeserializationContext, JsonDeserializer, JsonElement}
import distributed.{User, UserImpl}

object ConnectedPlayersDeserializer extends JsonDeserializer[ConcurrentMap[Int, User]] {
  override def deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): ConcurrentMap[Int, User] = {
    val jsonConnectedPlayers = json.getAsJsonObject
    val jsonKeys = jsonConnectedPlayers.keySet()
    val gson = new GsonBuilder().registerTypeAdapter(classOf[UserImpl], PlayerDeserializer).create()
    val map = new ConcurrentHashMap[Int, User]()

    jsonKeys forEach(key => map.put(key.toInt, gson.fromJson(jsonConnectedPlayers.get(key), classOf[UserImpl])))

    map
  }
}