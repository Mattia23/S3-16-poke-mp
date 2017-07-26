package model.map

import java.util.concurrent.ConcurrentMap

import distributed.PlayerPositionDetails
import model.entities.{Sprite, TrainerSprites}
import model.environment.Coordinate
import model.environment.Direction.Direction

case class OtherTrainerMovement(private val userId: Int, private val playersPositionDetails: ConcurrentMap[Int, PlayerPositionDetails],
                                private val _initialPosition: Coordinate, private val _direction: Direction,
                                private val _nextPosition: Coordinate,
                                override protected val trainerSprites: TrainerSprites) extends MovementImpl(_initialPosition, _direction, _nextPosition){

  override protected def currentTrainerSprite: Sprite = playersPositionDetails.get(userId).currentSprite

  override protected def currentTrainerSprite_=(sprite: Sprite): Unit = playersPositionDetails.get(userId).currentSprite = sprite

  override protected def updateCurrentX(actualX: Double): Unit = playersPositionDetails.get(userId).coordinateX = actualX

  override protected def updateCurrentY(actualY: Double): Unit = playersPositionDetails.get(userId).coordinateY = actualY

  override protected def updateTrainerPosition(nextPosition: Coordinate): Unit = {}

}
