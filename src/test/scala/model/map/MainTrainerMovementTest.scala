package model.map

import model.entities.Trainer
import model.environment.{CoordinateImpl, Direction}
import org.scalamock.scalatest.MockFactory
import org.scalatest.FunSuite
import view.GamePanel
import view.map.MapPanel

class MainTrainerMovementTest extends FunSuite with MockFactory{

  val trainer: Trainer = mock[Trainer]
  val gamePanel: GamePanel = mock[MapPanel]

  val movement = MainTrainerMovement(trainer, gamePanel)
  val initialPosition = CoordinateImpl(0,0)
  val nextPosition = CoordinateImpl(1,0)

  (trainer.currentSprite _).expects().returning(trainer.sprites.rightS)

  movement.walk(initialPosition, Direction.RIGHT, nextPosition)

 /* test ("The trainer current sprite is the one set after a right movement") {
    val f = fixture
    f.movement.walk(f.initialPosition, Direction.RIGHT, f.nextPosition)
    assert(f.trainer.currentSprite == f.trainer.sprites.rightS)
  }*/
}
