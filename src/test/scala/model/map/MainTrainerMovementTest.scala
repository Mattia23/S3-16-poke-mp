package model.map

import java.util.Optional

import database.remote.DBConnect
import model.entities.Trainer
import model.environment.{CoordinateImpl, Direction}
import org.scalatest.FunSuite
import utilities.Settings
import view.map.GamePanel

class MainTrainerMovementTest extends FunSuite{

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
      movement.walk(initialPosition, Direction.RIGHT, nextPosition)
    }

   test("The current trainer sprite is the one set after a movement") {
    val f = fixture
    assert(f.trainer.currentSprite ==  f.trainer.sprites.rightS)
  }

  test("The current trainer position is the one set after a movement") {
    val f = fixture
    assert(f.trainer.coordinate == f.nextPosition)
  }

  test("The current trainer positions in game panel are the one set after a movement") {
    val f = fixture
    assert(f.gamePanel.currentX == 1 * Settings.Constants.TILE_PIXEL)
    assert(f.gamePanel.currentY == 0 * Settings.Constants.TILE_PIXEL)
  }

  test("The current trainer return in the initial position after two movement (one on the right and one on the left)") {
    val f = fixture
    f.movement.walk(f.nextPosition, Direction.LEFT, f.initialPosition)
    assert(f.trainer.coordinate ==  f.initialPosition)
    assert(f.trainer.currentSprite == f.trainer.sprites.leftS)
  }

  private[map] class FakeGamePanel extends GamePanel {

    var currentX: Int = 0 * Settings.Constants.TILE_PIXEL
    var currentY: Int = 0 * Settings.Constants.TILE_PIXEL

    /**
      * Updates the current trainer's coordinate x
      *
      * @param x new coordinate x in double
      */
    override def updateCurrentX(x: Double): Unit = currentX = (x * Settings.Constants.TILE_PIXEL).toInt

    /**
      * Updates the current trainer's coordinate y
      *
      * @param y new coordinate y in double
      */
    override def updateCurrentY(y: Double): Unit = currentY = (y * Settings.Constants.TILE_PIXEL).toInt
  }
}