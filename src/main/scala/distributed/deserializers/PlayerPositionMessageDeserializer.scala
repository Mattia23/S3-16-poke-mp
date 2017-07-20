package distributed.deserializers

import java.lang.reflect.Type

import com.google.gson.{JsonDeserializationContext, JsonDeserializer, JsonElement}
import distributed.messages.{PlayerPositionMessage, PlayerPositionMessageImpl}
import model.environment.{CoordinateImpl, Direction}

object PlayerPositionMessageDeserializer extends JsonDeserializer[PlayerPositionMessage] {
  override def deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): PlayerPositionMessage = {
    val jsonPlayer = json.getAsJsonObject
    val jsonPosition = jsonPlayer.getAsJsonObject("position")
    val position = CoordinateImpl(jsonPosition.get("x").getAsInt, jsonPosition.get("y").getAsInt)
    //val playerPositionMessage = new PlayerPositionMessageImpl(jsonPlayer.get("userId").getAsInt, position)
    //playerPositionMessage
    PlayerPositionMessageImpl(jsonPlayer.get("userId").getAsInt, position)
  }
}
