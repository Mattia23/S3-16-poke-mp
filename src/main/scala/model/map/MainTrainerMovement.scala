package model.map

import model.entities.{Sprite, Trainer, TrainerSprites}
import model.environment.{Coordinate, CoordinateImpl}
import view.GamePanel

class MainTrainerMovement(trainer: Trainer, gamePanel: GamePanel) extends MovementImpl{

  override protected def currentTrainerSprite: Sprite = trainer.currentSprite

  override protected def currentTrainerSprite_=(sprite: Sprite): Unit = trainer.currentSprite = sprite

  override protected var trainerSprites: TrainerSprites = trainer.sprites

  override protected def updateCurrentX(actualX: Double): Unit = gamePanel.updateCurrentX(actualX)

  override protected def updateCurrentY(actualY: Double): Unit = gamePanel.updateCurrentY(actualY)

  override protected def updateTrainerPosition(nextPosition: Coordinate): Unit = trainer.coordinate = CoordinateImpl(nextPosition.x, nextPosition.y)

}
