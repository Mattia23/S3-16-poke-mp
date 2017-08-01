package distributed.deserializers

import java.lang.reflect.Type

import com.google.gson._
import distributed.PlayerImpl
import distributed.messages.PlayerMessage

object PlayerMessageDeserializer extends JsonDeserializer[PlayerMessage]{
  override def deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): PlayerMessage = {
    val jsonPlayerMessage = json.getAsJsonObject
    val gson = new GsonBuilder().registerTypeAdapter(classOf[PlayerImpl], PlayerDeserializer).create()
    PlayerMessage(gson.fromJson(jsonPlayerMessage.getAsJsonObject("player"), classOf[PlayerImpl]))
  }
}
