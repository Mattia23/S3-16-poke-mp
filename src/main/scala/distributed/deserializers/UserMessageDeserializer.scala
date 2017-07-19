package distributed.deserializers

import java.lang.reflect.Type

import com.google.gson._
import distributed.UserImpl
import distributed.messages.UserMessage

object UserMessageDeserializer extends JsonDeserializer[UserMessage]{
  override def deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): UserMessage = {
    val jsonUserMessage = json.getAsJsonObject
    val gson = new GsonBuilder().registerTypeAdapter(classOf[UserImpl], UserDeserializer).create()
    UserMessage(gson.fromJson(jsonUserMessage.getAsJsonObject("user"), classOf[UserImpl]))
  }
}
