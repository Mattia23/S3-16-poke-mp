package model.map

import java.awt.Image

import utilities.Settings

trait Tile {
  def width: Int = Settings.TILE_WIDTH
  def height: Int = Settings.TILE_HEIGHT
  def walkable: Boolean = true
  def wild: Boolean = false
  def image: String
}

case class Grass() extends Tile {
  override val image = Settings.GRASS_IMAGE_STRING
}

case class TallGrass() extends Tile {
  override val image = Settings.TALL_GRASS_IMAGE_STRING
  override val wild = true
}

case class Tree() extends Tile {
  override val image = Settings.TREE_IMAGE_STRING
  override val walkable = false
}

case class Water() extends Tile {
  override val image = Settings.WATER_IMAGE_STRING
  override val walkable = false
  override val wild = true
}