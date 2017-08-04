package model.map

import java.util.concurrent.ConcurrentMap

import distributed.PlayerPositionDetails
import model.entities.{Sprite, TrainerSprites}
import model.environment.Coordinate

/**
  * OtherTrainerMovement represents the movement of another trainer in the map
  * @param userId trainer id
  * @param playersPositionDetails position details of the trainer
  * @param trainerSprites sprites belonging to the trainer
  */
case class OtherTrainerMovement(private val userId: Int,
                                private val playersPositionDetails: ConcurrentMap[Int, PlayerPositionDetails],
                                override protected val trainerSprites: TrainerSprites) extends MovementImpl{

  private val playerPositionDetails: PlayerPositionDetails = playersPositionDetails.get(userId)

  /**
    * @inheritdoc
    * @return the current trainer sprite
    */
  override protected def currentTrainerSprite: Sprite = playerPositionDetails.currentSprite

  /**
    * @inheritdoc
    * @param sprite trainer sprite
    */
  override protected def currentTrainerSprite_=(sprite: Sprite): Unit = playerPositionDetails.currentSprite = sprite

  /**
    * @inheritdoc
    * @param actualX new x coordinate
    */
  override protected def updateCurrentX(actualX: Double): Unit = playerPositionDetails.coordinateX = actualX

  /**
    * @inheritdoc
    * @param actualY new y coordinate
    */
  override protected def updateCurrentY(actualY: Double): Unit = playerPositionDetails.coordinateY = actualY

  /**
    * @inheritdoc
    * @param nextPosition next trainer position
    */
  override protected def updateTrainerPosition(nextPosition: Coordinate): Unit = playersPositionDetails.put(userId, playerPositionDetails)


}
