package distributed.messages

import java.lang.reflect.Type

import com.google.gson.{JsonDeserializationContext, JsonDeserializer, JsonElement}
import model.environment.{Coordinate, CoordinateImpl}

trait PlayerPositionMessage {
  def userId: Int

  def position: Coordinate
}

object PlayerPositionMessageImpl {
  def apply(userId: Int, position: Coordinate): PlayerPositionMessage = new PlayerPositionMessageImpl(userId, position)
}

class PlayerPositionMessageImpl(override val userId: Int, override val position: Coordinate) extends PlayerPositionMessage

object PlayerPositionMessageDeserializer extends JsonDeserializer[PlayerPositionMessage] {
  override def deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): PlayerPositionMessage = {
    val jsonUser = json.getAsJsonObject
    val jsonPosition = jsonUser.getAsJsonObject("position")
    val position = CoordinateImpl(jsonPosition.get("x").getAsInt, jsonPosition.get("y").getAsInt)
    val playerPositionMessage = new PlayerPositionMessageImpl(jsonUser.get("userId").getAsInt, position)
    playerPositionMessage
  }
}