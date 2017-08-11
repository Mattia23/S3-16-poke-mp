package distributed

import model.entities.Sprite

/**
  * PlayerPositionDetails represents the position details of a player belonging to the current connected players to the game
  */
trait PlayerPositionDetails {

  /**
    * @return the player id
    */
  def id: Int

  /**
    * @return the x coordinate of the player position
    */
  def coordinateX: Double

  /**
    * Sets the x coordinate of the player in the map
    * @param x x coordinate
    */
  def coordinateX_=(x: Double): Unit

  /**
    * @return the y coordinate of the player in the map
    */
  def coordinateY: Double

  /**
    * Sets the y coordinate of the player in the map
    * @param y y coordinate
    */
  def coordinateY_=(y: Double): Unit

  /**
    * @return the current sprite of the player
    */
  def currentSprite: Sprite

  /**
    * Sets the current sprite of the player
    * @param sprite current sprite
    */
  def currentSprite_=(sprite: Sprite): Unit

}

object PlayerPositionDetails {
  def apply(id: Int, coordinateX: Double, coordinateY: Double, currentSprite: Sprite): PlayerPositionDetails =
    new PlayerPositionDetailsImpl(id, coordinateX, coordinateY, currentSprite)
}

/**
  * @inheritdoc
  * @param id player id
  * @param coordinateX x coordinate of the player in the map
  * @param coordinateY y coordinate of the player in the map
  * @param currentSprite current player sprite
  */
class PlayerPositionDetailsImpl(override val id: Int,
                                override var coordinateX: Double,
                                override var coordinateY: Double,
                                override var currentSprite: Sprite) extends PlayerPositionDetails