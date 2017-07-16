package distributed

import java.lang.reflect.Type
import java.util.concurrent.ConcurrentHashMap

import com.google.gson.{GsonBuilder, JsonDeserializationContext, JsonDeserializer, JsonElement}

trait ConnectedUsers {
  def map: ConcurrentHashMap[Int, User]
}

object ConnectedUsersImpl extends ConnectedUsers{

  private val _map = new ConcurrentHashMap[Int, User]()

  override val map: ConcurrentHashMap[Int, User] = _map
}

class ConnectedUsersDeserializer extends JsonDeserializer[ConnectedUsers] {
  override def deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): ConnectedUsers = {
    val jsonConnectedUser = json.getAsJsonObject
    val jsonKeys = jsonConnectedUser.keySet()
    val gson = new GsonBuilder().registerTypeAdapter(classOf[UserImpl], new UserDeserializer).create()

    jsonKeys forEach(key => {
      ConnectedUsersImpl.map.put(key.toInt, gson.fromJson(jsonConnectedUser.get(key), classOf[UserImpl]))
    })

    ConnectedUsersImpl
  }
}