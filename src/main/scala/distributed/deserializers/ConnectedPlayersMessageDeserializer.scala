package distributed.deserializers

import java.lang.reflect.Type
import java.util.concurrent.ConcurrentHashMap

import com.google.gson.{GsonBuilder, JsonDeserializationContext, JsonDeserializer, JsonElement}
import distributed.User
import distributed.messages.ConnectedPlayersMessage

object ConnectedPlayersMessageDeserializer extends JsonDeserializer[ConnectedPlayersMessage]{
  override def deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): ConnectedPlayersMessage = {
    val jsonConnectedPlayersMessage = json.getAsJsonObject
    val jsonConnectedPlayers = jsonConnectedPlayersMessage.get("connectedUsers").getAsJsonObject
    val gson = new GsonBuilder().registerTypeAdapter(classOf[ConcurrentHashMap[Int, User]], ConnectedPlayersDeserializer).create()
    val serverPlayers = gson.fromJson(jsonConnectedPlayers, classOf[ConcurrentHashMap[Int, User]])
    ConnectedPlayersMessage(serverPlayers)
  }
}
