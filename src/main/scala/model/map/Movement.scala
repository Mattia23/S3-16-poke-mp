package model.map

import model.entities._
import model.environment.Direction.Direction
import model.environment.{Coordinate, Direction}
import utilities.Settings

trait Movement {
  def walk(): Unit
}

object MovementImpl{
  private final val TRAINER_STEPS = 4
}

abstract class MovementImpl(private val initialPosition: Coordinate, private val direction: Direction,
                        private val nextPosition: Coordinate) extends Movement{
  import MovementImpl._

  private var firstStep: Boolean = true

  override def walk(): Unit = {
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
          if (firstStep) {
            currentTrainerSprite = trainerSprites.back1
            firstStep = false
          } else {
            currentTrainerSprite = trainerSprites.back2
            firstStep = true
          }
        case Back1(_) | Back2(_) => currentTrainerSprite = trainerSprites.backS
        case _ => currentTrainerSprite = trainerSprites.back1
      }
      case Direction.DOWN => currentTrainerSprite match {
        case FrontS(_) =>
          if (firstStep) {
            currentTrainerSprite = trainerSprites.front1
            firstStep = false
          } else {
            currentTrainerSprite = trainerSprites.front2
            firstStep = true
          }
        case Front1(_) | Front2(_) => currentTrainerSprite = trainerSprites.frontS
        case _ => currentTrainerSprite = trainerSprites.front1
      }
      case Direction.LEFT => currentTrainerSprite match {
        case LeftS(_) =>
          if (firstStep) {
            currentTrainerSprite = trainerSprites.left1
            firstStep = false
          } else {
            currentTrainerSprite = trainerSprites.left2
            firstStep = true
          }
        case Left1(_) | Left2(_) => currentTrainerSprite = trainerSprites.leftS
        case _ => currentTrainerSprite = trainerSprites.left1
      }
      case Direction.RIGHT => currentTrainerSprite match {
        case RightS(_) =>
          if (firstStep) {
            currentTrainerSprite = trainerSprites.right1
            firstStep = false
          } else {
            currentTrainerSprite = trainerSprites.right2
            firstStep = true
          }
        case Right1(_) | Right2(_) => currentTrainerSprite = trainerSprites.rightS
        case _ => currentTrainerSprite = trainerSprites.right1
      }
    }
  }

  protected def currentTrainerSprite: Sprite

  protected def currentTrainerSprite_=(sprite: Sprite): Unit

  protected val trainerSprites: TrainerSprites

  protected def updateCurrentX(actualX: Double): Unit

  protected def updateCurrentY(actualY: Double): Unit

  protected def updateTrainerPosition(nextPosition: Coordinate): Unit

}