package distributed.deserializers

import java.lang.reflect.Type

import com.google.gson.{JsonDeserializationContext, JsonDeserializer, JsonElement}
import distributed.{Player, PlayerImpl}
import model.environment.CoordinateImpl

object UserDeserializer extends JsonDeserializer[Player] {
  override def deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Player = {
    val jsonUser = json.getAsJsonObject
    val jsonPosition = jsonUser.getAsJsonObject("position")
    val position = CoordinateImpl(jsonPosition.get("x").getAsInt, jsonPosition.get("y").getAsInt)
    //val user = new UserImpl(jsonUser.get("userId").getAsInt, jsonUser.get("username").getAsString, jsonUser.get("idImage").getAsInt, position)
    //user
    Player(jsonUser.get("userId").getAsInt, jsonUser.get("username").getAsString, jsonUser.get("idImage").getAsInt, position)
  }
}
