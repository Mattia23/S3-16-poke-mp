package distributed.deserializers

import java.lang.reflect.Type
import java.util.concurrent.{ConcurrentHashMap, ConcurrentMap}

import com.google.gson.{GsonBuilder, JsonDeserializationContext, JsonDeserializer, JsonElement}
import distributed.{User, UserImpl}

object ConnectedUsersDeserializer extends JsonDeserializer[ConcurrentMap[Int, User]] {
  override def deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): ConcurrentMap[Int, User] = {
    val jsonConnectedUser = json.getAsJsonObject
    val jsonKeys = jsonConnectedUser.keySet()
    val gson = new GsonBuilder().registerTypeAdapter(classOf[UserImpl], UserDeserializer).create()
    val map = new ConcurrentHashMap[Int, User]()

    jsonKeys forEach(key => map.put(key.toInt, gson.fromJson(jsonConnectedUser.get(key), classOf[UserImpl])))

    map
  }
}