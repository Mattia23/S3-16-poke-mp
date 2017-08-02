package distributed.deserializers

import java.lang.reflect.Type

import com.google.gson.{JsonDeserializationContext, JsonDeserializer, JsonElement}
import distributed.messages.PlayerPositionMessage
import model.environment.CoordinateImpl

/**
  * Deserializer from Json to PlayerPositionMessage
  */
object PlayerPositionMessageDeserializer extends JsonDeserializer[PlayerPositionMessage] {
  override def deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): PlayerPositionMessage = {
    val jsonPlayer = json.getAsJsonObject
    val jsonPosition = jsonPlayer.getAsJsonObject("position")
    val position = CoordinateImpl(jsonPosition.get("x").getAsInt, jsonPosition.get("y").getAsInt)
    PlayerPositionMessage(jsonPlayer.get("userId").getAsInt, position)
  }
}
