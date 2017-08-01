package model.map

import utilities.Settings.Constants._
import utilities.Settings.Images._

trait Tile {
  def width: Int = TILE_WIDTH
  def height: Int = TILE_HEIGHT
  def walkable: Boolean = true
  def wild: Boolean = false
  def image: String
}

object Tile {
  case class Grass() extends Tile {
    override val image = GRASS_IMAGE_STRING
  }

  case class TallGrass() extends Tile {
    override val image = TALL_GRASS_IMAGE_STRING
    override val wild = true
  }

  case class Tree() extends Tile {
    override val image = TREE_IMAGE_STRING
    override val walkable = false
  }

  case class Water() extends Tile {
    override val image = WATER_IMAGE_STRING
    override val walkable = false
    override val wild = true
  }

  case class WaterMarginTopLeft() extends Tile {
    override val image = WATER_MARGIN_TOP_LEFT_IMAGE_STRING
    override val walkable = false
    override val wild = true
  }

  case class WaterMarginTopRight() extends Tile {
    override val image = WATER_MARGIN_TOP_RIGHT_IMAGE_STRING
    override val walkable = false
    override val wild = true
  }

  case class WaterMarginTop() extends Tile {
    override val image = WATER_MARGIN_TOP_IMAGE_STRING
    override val walkable = false
    override val wild = true
  }

  case class WaterMarginBottom() extends Tile {
    override val image = WATER_MARGIN_BOTTOM_IMAGE_STRING
    override val walkable = false
    override val wild = true
  }

  case class WaterMarginBottomLeft() extends Tile {
    override val image = WATER_MARGIN_BOTTOM_LEFT_IMAGE_STRING
    override val walkable = false
    override val wild = true
  }

  case class WaterMarginBottomRight() extends Tile {
    override val image = WATER_MARGIN_BOTTOM_RIGHT_IMAGE_STRING
    override val walkable = false
    override val wild = true
  }

  case class WaterMarginRight() extends Tile {
    override val image = WATER_MARGIN_RIGHT_IMAGE_STRING
    override val walkable = false
    override val wild = true
  }

  case class WaterMarginLeft() extends Tile {
    override val image = WATER_MARGIN_LEFT_IMAGE_STRING
    override val walkable = false
    override val wild = true
  }

  case class Road() extends Tile {
    override val image = ROAD_IMAGE_STRING
  }

  case class RoadMarginTopLeft() extends Tile {
    override val image = ROAD_MARGIN_TOP_LEFT_IMAGE_STRING
  }

  case class RoadMarginTopRight() extends Tile {
    override val image = ROAD_MARGIN_TOP_RIGHT_IMAGE_STRING
  }

  case class RoadMarginTop() extends Tile {
    override val image = ROAD_MARGIN_TOP_IMAGE_STRING
  }

  case class RoadMarginBottom() extends Tile {
    override val image = ROAD_MARGIN_BOTTOM_IMAGE_STRING
  }

  case class RoadMarginBottomLeft() extends Tile {
    override val image = ROAD_MARGIN_BOTTOM_LEFT_IMAGE_STRING
  }

  case class RoadMarginBottomRight() extends Tile {
    override val image = ROAD_MARGIN_BOTTOM_RIGHT_IMAGE_STRING
  }

  case class RoadMarginRight() extends Tile {
    override val image = ROAD_MARGIN_RIGHT_IMAGE_STRING
  }

  case class RoadMarginLeft() extends Tile {
    override val image = ROAD_MARGIN_LEFT_IMAGE_STRING
  }

  case class Box() extends Tile{
    override val image = null
    override val walkable = false
  }

  case class BasicTile() extends Tile{
    override val image = null
  }

  case class Barrier() extends Tile {
    override val image = null
    override val walkable = false
  }
}