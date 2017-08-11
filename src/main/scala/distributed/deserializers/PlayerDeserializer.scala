package distributed.deserializers

import java.lang.reflect.Type

import com.google.gson.{JsonDeserializationContext, JsonDeserializer, JsonElement}
import distributed.{Player, PlayerImpl}
import model.environment.CoordinateImpl

/**
  * Deserializer form Json to Player
  */
object PlayerDeserializer extends JsonDeserializer[Player] {
  override def deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Player = {
    val jsonPlayer = json.getAsJsonObject
    val jsonPosition = jsonPlayer.getAsJsonObject("position")
    val position = CoordinateImpl(jsonPosition.get("x").getAsInt, jsonPosition.get("y").getAsInt)
    PlayerImpl(jsonPlayer.get("userId").getAsInt, jsonPlayer.get("username").getAsString, jsonPlayer.get("idImage").getAsInt, position,jsonPlayer.get("isVisible").getAsBoolean, jsonPlayer.get("isBusy").getAsBoolean)
  }
}
