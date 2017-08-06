package model.map

import java.util.Optional

import database.remote.DBConnect
import model.entities.Trainer
import model.environment.{CoordinateImpl, Direction}
import org.scalamock.scalatest.MockFactory
import org.scalatest.FunSuite
import utilities.Settings

class MainTrainerMovementTest extends FunSuite with MockFactory {


  def fixture =
    new {
      var trainer: Trainer = null
      val optionalTrainer: Optional[Trainer] = DBConnect getTrainerFromDB "admin"
      if(optionalTrainer.isPresent) {
        trainer = optionalTrainer.get()
      }
      val gamePanel = new FakeGamePanel()
      val movement = MainTrainerMovement(trainer, gamePanel)
      val initialPosition = CoordinateImpl(0, 0)
      val nextPosition = CoordinateImpl(1, 0)
    }

  test("The trainer current sprite is the one set after a movement") {
    val f = fixture
    f.movement.walk(f.initialPosition, Direction.RIGHT, f.nextPosition)
    assert(f.trainer.currentSprite == f.trainer.sprites.rightS)
  }

  test("The trainer current position is the one set after a movement") {
    val f = fixture
    f.movement.walk(f.initialPosition, Direction.RIGHT, f.nextPosition)
    assert(f.trainer.coordinate == CoordinateImpl(f.nextPosition.x, f.nextPosition.y))
  }

  test("The game panel current trainer positions are the one set after a movement") {
    val f = fixture
    f.movement.walk(f.initialPosition, Direction.RIGHT, f.nextPosition)
    assert(f.gamePanel.currentX == 1 * Settings.Constants.TILE_PIXEL)
    assert(f.gamePanel.currentY == 0 * Settings.Constants.TILE_PIXEL)
  }
}