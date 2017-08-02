package distributed.deserializers

import java.lang.reflect.Type
import java.util

import com.google.gson.{GsonBuilder, JsonDeserializationContext, JsonDeserializer, JsonElement}
import distributed.Player
import distributed.messages.ConnectedPlayersMessage

/**
  * Deserializer from Json to ConnectedPlayersMessage
  */
object ConnectedPlayersMessageDeserializer extends JsonDeserializer[ConnectedPlayersMessage]{
  override def deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): ConnectedPlayersMessage = {
    val jsonConnectedPlayersMessage = json.getAsJsonObject
    val jsonConnectedPlayers = jsonConnectedPlayersMessage.get("connectedPlayers").getAsJsonObject
    val gson = new GsonBuilder().registerTypeAdapter(classOf[util.HashMap[Int, Player]], ConnectedPlayersDeserializer).create()
    val serverPlayers = gson.fromJson(jsonConnectedPlayers, classOf[util.HashMap[Int, Player]])
    ConnectedPlayersMessage(serverPlayers)
  }
}
