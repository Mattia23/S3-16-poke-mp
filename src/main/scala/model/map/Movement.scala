package model.map

import model.entities._
import model.environment.{Coordinate, CoordinateImpl, Direction}
import model.environment.Direction.Direction
import utilities.Settings

trait Movement {
  def walk(initialPosition: Coordinate, direction: Direction, nextPosition: Coordinate): Unit
}

object MovementImpl{
  private final val TRAINER_STEPS = 4
}

abstract class MovementImpl() extends Movement{
  import MovementImpl._

  private var fistStep: Boolean = true

  override def walk(initialPosition: Coordinate, direction: Direction, nextPosition: Coordinate): Unit = {
    var actualX: Double = initialPosition.x
    var actualY: Double = initialPosition.y
    for (_ <- 1 to TRAINER_STEPS) {
      direction match {
        case Direction.UP =>
          actualY = actualY - (Settings.TILE_HEIGHT.asInstanceOf[Double] / TRAINER_STEPS)
          updateCurrentY(actualY)
        case Direction.DOWN =>
          actualY = actualY + (Settings.TILE_HEIGHT.asInstanceOf[Double] / TRAINER_STEPS)
          updateCurrentY(actualY)
        case Direction.RIGHT =>
          actualX = actualX + (Settings.TILE_WIDTH.asInstanceOf[Double] / TRAINER_STEPS)
          updateCurrentX(actualX)
        case Direction.LEFT =>
          actualX = actualX - (Settings.TILE_WIDTH.asInstanceOf[Double] / TRAINER_STEPS)
          updateCurrentX(actualX)
      }
      updateTrainerSprite(direction)
      Thread.sleep(Settings.GAME_REFRESH_TIME)
    }
    updateTrainerPosition(nextPosition)
  }

  private def updateTrainerSprite(direction: Direction): Unit = {
    direction match {
      case Direction.UP => currentTrainerSprite match {
        case BackS(_) =>
          if (fistStep) {
            currentTrainerSprite = trainerSprites.back1
            fistStep = false
          } else {
            currentTrainerSprite = trainerSprites.back2
            fistStep = true
          }
        case Back1(_) | Back2(_) => currentTrainerSprite = trainerSprites.backS
        case _ => currentTrainerSprite = trainerSprites.back1
      }
      case Direction.DOWN => currentTrainerSprite match {
        case FrontS(_) =>
          if (fistStep) {
            currentTrainerSprite = trainerSprites.front1
            fistStep = false
          } else {
            currentTrainerSprite = trainerSprites.front2
            fistStep = true
          }
        case Front1(_) | Front2(_) => currentTrainerSprite = trainerSprites.frontS
        case _ => currentTrainerSprite = trainerSprites.front1
      }
      case Direction.LEFT => currentTrainerSprite match {
        case LeftS(_) =>
          if (fistStep) {
            currentTrainerSprite = trainerSprites.left1
            fistStep = false
          } else {
            currentTrainerSprite = trainerSprites.left2
            fistStep = true
          }
        case Left1(_) | Left2(_) => currentTrainerSprite = trainerSprites.leftS
        case _ => currentTrainerSprite = trainerSprites.left1
      }
      case Direction.RIGHT => currentTrainerSprite match {
        case RightS(_) =>
          if (fistStep) {
            currentTrainerSprite = trainerSprites.right1
            fistStep = false
          } else {
            currentTrainerSprite = trainerSprites.right2
            fistStep = true
          }
        case Right1(_) | Right2(_) => currentTrainerSprite = trainerSprites.rightS
        case _ => currentTrainerSprite = trainerSprites.right1
      }
    }
  }

  protected def currentTrainerSprite: Sprite

  protected def currentTrainerSprite_=(sprite: Sprite): Unit

  protected var trainerSprites: TrainerSprites

  protected def updateCurrentX(actualX: Double): Unit

  protected def updateCurrentY(actualY: Double): Unit

  protected def updateTrainerPosition(nextPosition: Coordinate): Unit

}