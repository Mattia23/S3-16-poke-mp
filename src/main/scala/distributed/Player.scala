package distributed

import model.environment.Coordinate
import utilities.Settings

/**
  * Player represents a connected player in the game. It contains some fields to represent the player on the map of the user.
  */
trait Player {

  /**
    * @return the player id
    */
  def userId: Int

  /**
    * @return the player username
    */
  def username: String

  /**
    * @return the trainer image of the player
    */
  def idImage: Int

  /**
    * @return the player position
    */
  def position: Coordinate

  /**
    * Sets the player position
    * @param coordinate player position
    */
  def position_=(coordinate: Coordinate): Unit

  /**
    * @return whether the player is visible
    */
  def isVisible: Boolean

  /**
    * Sets the player visibility
    * @param visible true if the trainer is visible, false in the opposite case
    */
  def isVisible_=(visible: Boolean): Unit

  /**
    * @return whether the player is busy
    */
  def isBusy: Boolean

  /**
    * Sets if the player is busy
    * @param isBusy true if the trainer is busy, false in the opposite case
    */
  def isBusy_=(isBusy: Boolean): Unit
}

/**
  * @inheritdoc
  * @param userId player id
  * @param username player username
  * @param idImage trainer image of the player
  * @param position player position
  * @param isVisible player visibility
  * @param isBusy says if the player is busy
  */
case class PlayerImpl(override val userId: Int,
                      override val username: String,
                      override val idImage: Int,
                      override var position: Coordinate = Settings.Constants.INITIAL_PLAYER_POSITION,
                      override var isVisible: Boolean = true,
                      override var isBusy: Boolean = false) extends Player


