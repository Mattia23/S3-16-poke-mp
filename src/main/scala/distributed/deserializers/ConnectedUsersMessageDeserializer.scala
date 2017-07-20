package distributed.deserializers

import java.lang.reflect.Type
import java.util.concurrent.ConcurrentHashMap

import com.google.gson.{GsonBuilder, JsonDeserializationContext, JsonDeserializer, JsonElement}
import distributed.Player
import distributed.messages.ConnectedUsersMessage

object ConnectedUsersMessageDeserializer extends JsonDeserializer[ConnectedUsersMessage]{
  override def deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): ConnectedUsersMessage = {
    val jsonConnectedUserMessage = json.getAsJsonObject
    val jsonConnectedUsers = jsonConnectedUserMessage.get("connectedUsers").getAsJsonObject
    val gson = new GsonBuilder().registerTypeAdapter(classOf[ConcurrentHashMap[Int, Player]], ConnectedUsersDeserializer).create()
    val serverUsers = gson.fromJson(jsonConnectedUsers, classOf[ConcurrentHashMap[Int, Player]])
    ConnectedUsersMessage(serverUsers)
  }
}
