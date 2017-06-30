package model.map

import utilities.Settings

trait Tile {
  def width: Int = Settings.TILE_WIDTH
  def height: Int = Settings.TILE_HEIGHT
  def walkable: Boolean = true
  def wild: Boolean = false
}

case class BasicTile() extends Tile{}

case class Barrier() extends Tile {
  override val walkable: Boolean = false
}
