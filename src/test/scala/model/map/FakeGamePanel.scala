package model.map

import utilities.Settings
import view.map.GamePanel

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