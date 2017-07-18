package controller

import java.util.concurrent.ConcurrentMap
import distributed.User

trait DistributedMapController extends GameController{
  def connectedUsers: ConcurrentMap[Int, User]

  def usersTrainerSprites: ConcurrentMap[Int, String]
}