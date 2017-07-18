package controller

import java.util.concurrent.ConcurrentHashMap
import distributed.ConnectedUsersImpl
import model.entities.{Trainer, TrainerSprites}
import utilities.Settings
import view.View

class DistributedMapController(private val view: View, private val _trainer: Trainer/*, private val connectedUsers: Future[Unit]*/) extends MapController(view, _trainer){

  private var distributedAgent: DistributedMapControllerAgent = _

  /*
  import scala.concurrent.ExecutionContext.Implicits.global
  connectedUsers.onComplete {
    case Success(value) =>
      println("put distr map contro")
      ConnectedUsersImpl.map.values() forEach (user => UsersTrainerSpritesMapImpl.map.put(user.userId, TrainerSprites.selectTrainerSprite(user.idImage).backS.image))
    case Failure(e) => e.printStackTrace
  }
*/
  override protected def doStart(): Unit = {
    super.doStart()
    distributedAgent = new DistributedMapControllerAgent(this)
    distributedAgent.start
  }

}


trait UsersTrainerSprites{
  def map: ConcurrentHashMap[Int, String]
}

object UsersTrainerSpritesMapImpl extends UsersTrainerSprites{

  private val _map: ConcurrentHashMap[Int, String] = new ConcurrentHashMap[Int, String]()

  override val map: ConcurrentHashMap[Int, String] = _map
}

private class DistributedMapControllerAgent(private val gameController: GameController) extends Thread {
  var stopped: Boolean = false

  override def run(): Unit = {
    while(gameController.isInGame && !stopped){
      if(!gameController.isInPause){
        ConnectedUsersImpl.map.values() forEach (user =>
          UsersTrainerSpritesMapImpl.map.put(user.userId, TrainerSprites.selectTrainerSprite(user.idImage).frontS.image))
      }

      try
        Thread.sleep(Settings.GAME_REFRESH_TIME)
      catch {
        case e: InterruptedException => System.out.println(e)
      }
    }
  }

  def terminate(): Unit = {
    stopped = true
  }

}
