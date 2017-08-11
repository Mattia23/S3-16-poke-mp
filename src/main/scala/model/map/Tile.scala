package model.map

import utilities.Settings.Constants._
import utilities.Settings.Images._

/**
  * Tile represents a single tile of the map
  */
trait Tile {
  /**
    * @return the tile width
    */
  def width: Int = TILE_WIDTH

  /**
    * @return the tile height
    */
  def height: Int = TILE_HEIGHT

  /**
    * @return true if the tile is walkable, false otherwise
    */
  def walkable: Boolean = true

  /**
    * @return true if in the tile can be found a wild pokemon, false otherwise
    */
  def wild: Boolean = false

  /**
    * @return the String path of the tile image
    */
  def image: String
}

object Tile {

  /**
    * Grass represents a grass tile
    */
  case class Grass() extends Tile {
    override val image = GRASS_IMAGE_STRING
  }

  /**
    * TallGrass represents a tall grass tile
    */
  case class TallGrass() extends Tile {
    override val image = TALL_GRASS_IMAGE_STRING
    override val wild = true
  }

  /**
    * Tree represents a tree tile
    */
  case class Tree() extends Tile {
    override val image = TREE_IMAGE_STRING
    override val walkable = false
  }

  /**
    * Water represents a water tile
    */
  case class Water() extends Tile {
    override val image = WATER_IMAGE_STRING
    override val walkable = false
    override val wild = true
  }

  /**
    * WaterMarginTopLeft represents the top left margin tile of a lake
    */
  case class WaterMarginTopLeft() extends Tile {
    override val image = WATER_MARGIN_TOP_LEFT_IMAGE_STRING
    override val walkable = false
    override val wild = true
  }

  /**
    * WaterMarginTopRight represents the top right margin tile of a lake
    */
  case class WaterMarginTopRight() extends Tile {
    override val image = WATER_MARGIN_TOP_RIGHT_IMAGE_STRING
    override val walkable = false
    override val wild = true
  }

  /**
    * WaterMarginTop represents the top margin tile of a lake
    */
  case class WaterMarginTop() extends Tile {
    override val image = WATER_MARGIN_TOP_IMAGE_STRING
    override val walkable = false
    override val wild = true
  }

  /**
    * WaterMarginBottom represents the bottom margin tile of a lake
    */
  case class WaterMarginBottom() extends Tile {
    override val image = WATER_MARGIN_BOTTOM_IMAGE_STRING
    override val walkable = false
    override val wild = true
  }

  /**
    * WaterMarginBottomLeft represents the bottom left margin tile of a lake
    */
  case class WaterMarginBottomLeft() extends Tile {
    override val image = WATER_MARGIN_BOTTOM_LEFT_IMAGE_STRING
    override val walkable = false
    override val wild = true
  }

  /**
    * WaterMarginBottomRight represents the bottom right margin tile of a lake
    */
  case class WaterMarginBottomRight() extends Tile {
    override val image = WATER_MARGIN_BOTTOM_RIGHT_IMAGE_STRING
    override val walkable = false
    override val wild = true
  }

  /**
    * WaterMarginRight represents the right margin tile of a lake
    */
  case class WaterMarginRight() extends Tile {
    override val image = WATER_MARGIN_RIGHT_IMAGE_STRING
    override val walkable = false
    override val wild = true
  }

  /**
    * WaterMarginLeft represents the left margin tile of a lake
    */
  case class WaterMarginLeft() extends Tile {
    override val image = WATER_MARGIN_LEFT_IMAGE_STRING
    override val walkable = false
    override val wild = true
  }

  /**
    * Road represents the road tile
    */
  case class Road() extends Tile {
    override val image = ROAD_IMAGE_STRING
  }

  /**
    * RoadMarginTopLeft represents the top left margin tile of a road
    */
  case class RoadMarginTopLeft() extends Tile {
    override val image = ROAD_MARGIN_TOP_LEFT_IMAGE_STRING
  }

  /**
    * RoadMarginTopRight represents the top right margin tile of a road
    */
  case class RoadMarginTopRight() extends Tile {
    override val image = ROAD_MARGIN_TOP_RIGHT_IMAGE_STRING
  }

  /**
    * RoadMarginTop represents the top margin tile of a road
    */
  case class RoadMarginTop() extends Tile {
    override val image = ROAD_MARGIN_TOP_IMAGE_STRING
  }

  /**
    * RoadMarginBottom represents the bottom margin tile of a road
    */
  case class RoadMarginBottom() extends Tile {
    override val image = ROAD_MARGIN_BOTTOM_IMAGE_STRING
  }

  /**
    * RoadMarginBottomLeft represents the bottom left margin tile of a road
    */
  case class RoadMarginBottomLeft() extends Tile {
    override val image = ROAD_MARGIN_BOTTOM_LEFT_IMAGE_STRING
  }

  /**
    * RoadMarginBottomRight represents the bottom right margin tile of a road
    */
  case class RoadMarginBottomRight() extends Tile {
    override val image = ROAD_MARGIN_BOTTOM_RIGHT_IMAGE_STRING
  }

  /**
    * RoadMarginRight represents the right margin tile of a road
    */
  case class RoadMarginRight() extends Tile {
    override val image = ROAD_MARGIN_RIGHT_IMAGE_STRING
  }

  /**
    * RoadMarginLeft represents the left margin tile of a road
    */
  case class RoadMarginLeft() extends Tile {
    override val image = ROAD_MARGIN_LEFT_IMAGE_STRING
  }

  /**
    * Box represents the box tile
    */
  case class Box() extends Tile{
    override val image = null
    override val walkable = false
  }

  /**
    * BasicTile represents a tile without image
    */
  case class BasicTile() extends Tile{
    override val image = null
  }

  /**
    * Barrier represents a tile a tile without image and not walkable
    */
  case class Barrier() extends Tile {
    override val image = null
    override val walkable = false
  }
}