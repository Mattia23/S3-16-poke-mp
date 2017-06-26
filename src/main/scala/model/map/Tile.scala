package model.map

import utilities.Settings

trait Tile {
  def width: Int = Settings.TILE_WIDTH
  def height: Int = Settings.TILE_HEIGHT
  def walkable: Boolean = true
  def wild: Boolean = false
}

case class Grass() extends Tile

case class TallGrass() extends Tile {
  override val wild: Boolean = true
}

case class Tree() extends Tile {
  override val walkable: Boolean = false
}

case class Water() extends Tile {
  override val walkable: Boolean = false
  override val wild: Boolean = true
}