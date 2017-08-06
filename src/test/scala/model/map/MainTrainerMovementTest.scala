package model.map

import controller.MapController
import model.entities.TrainerImpl
import model.environment.{CoordinateImpl, Direction}
import org.scalatest.FunSuite
import view.map.MapPanel

class MainTrainerMovementTest extends FunSuite{

  def fixture =
    new {
      val trainer = new TrainerImpl("trainer", 1, 0)
      val mapController = MapController(null, trainer, null, null)
      val gamePanel = new MapPanel(mapController, null, null)
      val movement = MainTrainerMovement(trainer, gamePanel)
      val initialPosition = CoordinateImpl(0,0)
      val nextPosition = CoordinateImpl(1,0)
    }

  test ("The trainer current sprite is the one set after a right movement") {
    val f = fixture
    f.movement.walk(f.initialPosition, Direction.RIGHT, f.nextPosition)
    assert(f.trainer.currentSprite == f.trainer.sprites.rightS)
  }
}
