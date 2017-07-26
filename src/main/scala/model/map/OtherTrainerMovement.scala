package model.map

import java.util.concurrent.ConcurrentMap

import distributed.PlayerPositionDetails
import model.entities.{Sprite, TrainerSprites}
import model.environment.Coordinate

case class OtherTrainerMovement(playersPositionDetails: ConcurrentMap[Int, PlayerPositionDetails]) extends MovementImpl{

  override protected def currentTrainerSprite: Sprite = ???

  override protected def currentTrainerSprite_=(sprite: Sprite): Unit = ???

  override protected var trainerSprites: TrainerSprites = _

  override protected def updateCurrentX(actualX: Double): Unit = ???

  override protected def updateCurrentY(actualY: Double): Unit = ???

  override protected def updateTrainerPosition(nextPosition: Coordinate): Unit = ???

}
