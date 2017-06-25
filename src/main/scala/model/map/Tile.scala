package model.map

import utilities.Settings

trait Tile {
  def width: Int = Settings.TILE_WIDTH
  def height: Int = Settings.TILE_HEIGHT
  def walkable: Boolean = true
  def wild: Boolean = false
}

case class Grass() extends Tile

case class TallGrass(override val wild: Boolean = true) extends Tile

case class Tree(override val walkable: Boolean = false) extends Tile

case class Water(override val walkable: Boolean = false, override val wild: Boolean = true) extends Tile