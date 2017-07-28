package model.map

import distributed.PlayerPositionDetails
import model.entities.{Sprite, TrainerSprites}
import model.environment.Coordinate

case class OtherTrainerMovement(private val userId: Int,
                                private val playersPositionDetails: Map[Int, PlayerPositionDetails],
                                override protected val trainerSprites: TrainerSprites) extends MovementImpl{

  private val playerPositionDetails: PlayerPositionDetails = playersPositionDetails(userId)

  override protected def currentTrainerSprite: Sprite = playerPositionDetails.currentSprite

  override protected def currentTrainerSprite_=(sprite: Sprite): Unit = playerPositionDetails.currentSprite = sprite

  override protected def updateCurrentX(actualX: Double): Unit = playerPositionDetails.coordinateX = actualX

  override protected def updateCurrentY(actualY: Double): Unit = playerPositionDetails.coordinateY = actualY

  override protected def updateTrainerPosition(nextPosition: Coordinate): Unit = playersPositionDetails + (userId -> playerPositionDetails)


}
