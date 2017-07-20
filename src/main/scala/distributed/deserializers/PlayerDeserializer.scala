package distributed.deserializers

import java.lang.reflect.Type

import com.google.gson.{JsonDeserializationContext, JsonDeserializer, JsonElement}
import distributed.{User, UserImpl}
import model.environment.CoordinateImpl

object PlayerDeserializer extends JsonDeserializer[User] {
  override def deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): User = {
    val jsonPlayer = json.getAsJsonObject
    val jsonPosition = jsonPlayer.getAsJsonObject("position")
    val position = CoordinateImpl(jsonPosition.get("x").getAsInt, jsonPosition.get("y").getAsInt)
    //val user = new UserImpl(jsonPlayer.get("userId").getAsInt, jsonPlayer.get("username").getAsString, jsonPlayer.get("idImage").getAsInt, position)
    //user
    User(jsonPlayer.get("userId").getAsInt, jsonPlayer.get("username").getAsString, jsonPlayer.get("idImage").getAsInt, position)
  }
}
