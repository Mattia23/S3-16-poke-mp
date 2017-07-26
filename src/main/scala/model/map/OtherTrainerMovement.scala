package model.map

import java.util.concurrent.ConcurrentMap

import distributed.PlayerPositionDetails
import model.entities.{Sprite, TrainerSprites}
import model.environment.Coordinate
import model.environment.Direction.Direction

case class OtherTrainerMovement(private val playersPositionDetails: ConcurrentMap[Int, PlayerPositionDetails],
                                private val _initialPosition: Coordinate, private val _direction: Direction,
                                private val _nextPosition: Coordinate) extends Movement(_initialPosition, _direction, _nextPosition){

  override protected def currentTrainerSprite: Sprite = ???

  override protected def currentTrainerSprite_=(sprite: Sprite): Unit = ???

  override protected var trainerSprites: TrainerSprites = _

  override protected def updateCurrentX(actualX: Double): Unit = ???

  override protected def updateCurrentY(actualY: Double): Unit = ???

  override protected def updateTrainerPosition(nextPosition: Coordinate): Unit = ???

}
