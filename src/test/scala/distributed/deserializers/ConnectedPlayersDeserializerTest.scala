package distributed.deserializers

import java.util

import com.google.gson.GsonBuilder
import distributed.{Player, PlayerImpl}
import org.scalatest.FunSuite

class ConnectedPlayersDeserializerTest extends FunSuite {

  test("The ConnectedPlayers map should be correctly got from Json") {
    val gson = new GsonBuilder().registerTypeAdapter(classOf[util.HashMap[Int, Player]], ConnectedPlayersDeserializer).create()
    val userIds = Seq(1, 2, 3)
    val playersMap = createPlayersMap(userIds)

    val playersMapJson = gson toJson playersMap
    val playersMapReturned = gson.fromJson(playersMapJson, classOf[util.HashMap[Int, Player]])

    assert(playersMap == playersMapReturned)
  }

  private def createPlayer(userId: Int) = {
    PlayerImpl(userId,"",1)
  }

  private def createPlayersMap(userIds: Seq[Int]) = {
    val playersMap = new util.HashMap[Int, Player]()
    userIds foreach (id => {
      val player = createPlayer(id)
      playersMap.put(id, player)
    })
    playersMap
  }
}
