package distributed

import model.entities.Sprite

trait PlayerPositionDetails {

  def id: Int

  def coordinateX: Double

  def coordinateX_=(x: Double): Unit

  def coordinateY: Double

  def coordinateY_=(y: Double): Unit

  def currentSprite: Sprite

  def currentSprite_=(sprite: Sprite): Unit

}

object PlayerPositionDetails {
  def apply(id: Int, coordinateX: Double, coordinateY: Double, currentSprite: Sprite): PlayerPositionDetails =
    new PlayerPositionDetailsImpl(id, coordinateX, coordinateY, currentSprite)
}

class PlayerPositionDetailsImpl(override val id: Int, override var coordinateX: Double,
                                override var coordinateY: Double, override var currentSprite: Sprite) extends PlayerPositionDetails