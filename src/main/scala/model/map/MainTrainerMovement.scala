package model.map

import model.entities.{Sprite, Trainer, TrainerSprites}
import model.environment.Direction.Direction
import model.environment.{Coordinate, CoordinateImpl}
import view.GamePanel

case class MainTrainerMovement(private val trainer: Trainer,
                               private val gamePanel: GamePanel,
                               private val initialPosition: Coordinate,
                               private val direction: Direction,
                               private val nextPosition: Coordinate)
  extends MovementImpl(initialPosition, direction, nextPosition){

  override protected def currentTrainerSprite: Sprite = trainer.currentSprite

  override protected def currentTrainerSprite_=(sprite: Sprite): Unit = trainer.currentSprite = sprite

  override protected val trainerSprites: TrainerSprites = trainer.sprites

  override protected def updateCurrentX(actualX: Double): Unit = gamePanel.updateCurrentX(actualX)

  override protected def updateCurrentY(actualY: Double): Unit = gamePanel.updateCurrentY(actualY)

  override protected def updateTrainerPosition(nextPosition: Coordinate): Unit = trainer.coordinate = CoordinateImpl(nextPosition.x, nextPosition.y)

}
