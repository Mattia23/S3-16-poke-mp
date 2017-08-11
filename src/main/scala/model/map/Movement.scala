package model.map

import model.entities._
import model.environment.Direction.Direction
import model.environment.{Coordinate, Direction}
import utilities.Settings

/**
  * Movement represents a movement of the main trainer or of other trainer in the map
  */
trait Movement {
  /**
    * Manages the walk of the main trainer/other trainer in the map
    * @param initialPosition main trainer/other trainer initial position
    * @param direction walk direction
    * @param nextPosition main trainer/other trainer next position
    */
  def walk(initialPosition: Coordinate, direction: Direction, nextPosition: Coordinate): Unit
}

object MovementImpl{
  private final val TRAINER_STEPS = 4
}

/**
  * @inheritdoc
  */
abstract class MovementImpl extends Movement{
  import MovementImpl._

  private var firstStep: Boolean = true

  /**
    * @inheritdoc
    * @param initialPosition main trainer/other trainer initial position
    * @param direction walk direction
    * @param nextPosition main trainer/other trainer next position
    */
  override def walk(initialPosition: Coordinate, direction: Direction, nextPosition: Coordinate): Unit = {
    var actualX: Double = initialPosition.x
    var actualY: Double = initialPosition.y

    import Settings.Constants._
    for (_ <- 1 to TRAINER_STEPS) {
      direction match {
        case Direction.UP =>
          actualY = actualY - (TILE_HEIGHT.asInstanceOf[Double] / TRAINER_STEPS)
          updateCurrentY(actualY)
        case Direction.DOWN =>
          actualY = actualY + (TILE_HEIGHT.asInstanceOf[Double] / TRAINER_STEPS)
          updateCurrentY(actualY)
        case Direction.RIGHT =>
          actualX = actualX + (TILE_WIDTH.asInstanceOf[Double] / TRAINER_STEPS)
          updateCurrentX(actualX)
        case Direction.LEFT =>
          actualX = actualX - (TILE_WIDTH.asInstanceOf[Double] / TRAINER_STEPS)
          updateCurrentX(actualX)
      }
      updateTrainerSprite(direction)
      Thread.sleep(GAME_REFRESH_TIME)
    }
    updateTrainerPosition(nextPosition)
  }

  /**
    * Updates the trainer sprite
    * @param direction walk direction
    */
  private def updateTrainerSprite(direction: Direction) = {
    import Sprite._

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

  /**
    * @return the current trainer sprite
    */
  protected def currentTrainerSprite: Sprite

  /**
    * Sets the trainer sprite
    * @param sprite trainer sprite
    */
  protected def currentTrainerSprite_=(sprite: Sprite): Unit

  protected val trainerSprites: TrainerSprites

  /**
    * Updates the current x coordinate of the trainer
    * @param actualX new x coordinate
    */
  protected def updateCurrentX(actualX: Double): Unit

  /**
    * Updates the current y coordinate of the trainer
    * @param actualY new y coordinate
    */
  protected def updateCurrentY(actualY: Double): Unit

  /**
    * Updates trainer position
    * @param nextPosition next trainer position
    */
  protected def updateTrainerPosition(nextPosition: Coordinate): Unit

}