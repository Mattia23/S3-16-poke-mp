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

class PlayerPositionDetailsImpl(private val _id: Int, private var _coordinateX: Double,
                                private var _coordinateY: Double, private var _currentSprite: Sprite) extends PlayerPositionDetails {
  override def id: Int = synchronized {
    _id
  }

  override def coordinateX: Double = synchronized {
    _coordinateX
  }

  override def coordinateX_=(x: Double): Unit = synchronized {
    _coordinateX = x
  }

  override def coordinateY: Double = synchronized {
    _coordinateY
  }

  override def coordinateY_=(y: Double): Unit = synchronized {
    _coordinateY = y
  }

  override def currentSprite: Sprite = synchronized {
    _currentSprite
  }

  override def currentSprite_=(sprite: Sprite): Unit = synchronized {
    _currentSprite = sprite
  }
}