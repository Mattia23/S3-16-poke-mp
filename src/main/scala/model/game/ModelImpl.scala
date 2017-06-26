package model.game

import model.environment.Direction
import model.environment.Direction.Direction

trait Model {
  def startGame: Unit

  def isInGame: Boolean

  def isInPause: Boolean

  def pause: Unit

  def resumeGame: Unit

  def terminate: Unit

  def moveTrainer(direction: Direction.Direction)
}

class ModelImpl extends Model {
  private var inGame: Boolean = false
  private var inPause: Boolean = false

  override def startGame: Unit = this.inGame = true
  override def isInGame: Boolean = this.inGame
  override def isInPause: Boolean = this.inPause
  override def pause: Unit = this.inPause = true
  override def resumeGame: Unit = this.inPause = false
  override def terminate: Unit = this.inGame = false

  override def moveTrainer(direction: Direction): Unit = {
    //passare il movimento ad un metodo dentro la classe "Trainer"
  }
}
