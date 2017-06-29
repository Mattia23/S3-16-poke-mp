package model.map

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

case class WaterMarginTopLeft() extends Water {
  override val image = Settings.WATER_MARGIN_TOP_LEFT_IMAGE_STRING
}

case class WaterMarginTopRight() extends Water {
  override val image = Settings.WATER_MARGIN_TOP_RIGHT_IMAGE_STRING
}

case class WaterMarginTop() extends Water {
  override val image = Settings.WATER_MARGIN_TOP_IMAGE_STRING
}

case class WaterMarginBottom() extends Water {
  override val image = Settings.WATER_MARGIN_BOTTOM_IMAGE_STRING
}

case class WaterMarginBottomLeft() extends Water {
  override val image = Settings.WATER_MARGIN_BOTTOM_LEFT_IMAGE_STRING
}

case class WaterMarginBottomRight() extends Water {
  override val image = Settings.WATER_MARGIN_BOTTOM_RIGHT_IMAGE_STRING
}

case class WaterMarginRight() extends Water {
  override val image = Settings.WATER_MARGIN_RIGHT_IMAGE_STRING
}

case class WaterMarginLeft() extends Water {
  override val image = Settings.WATER_MARGIN_LEFT_IMAGE_STRING
}

case class Road() extends Tile {
  override val image = Settings.ROAD_IMAGE_STRING
  override val walkable = true
  override val wild = false
}

case class RoadMarginTopLeft() extends Road {
  override val image = Settings.ROAD_MARGIN_TOP_LEFT_IMAGE_STRING
}

case class RoadMarginTopRight() extends Road {
  override val image = Settings.ROAD_MARGIN_TOP_RIGHT_IMAGE_STRING
}

case class RoadMarginTop() extends Road {
  override val image = Settings.ROAD_MARGIN_TOP_IMAGE_STRING
}

case class RoadMarginBottom() extends Road {
  override val image = Settings.ROAD_MARGIN_BOTTOM_IMAGE_STRING
}

case class RoadMarginBottomLeft() extends Road {
  override val image = Settings.ROAD_MARGIN_BOTTOM_LEFT_IMAGE_STRING
}

case class RoadMarginBottomRight() extends Road {
  override val image = Settings.ROAD_MARGIN_BOTTOM_RIGHT_IMAGE_STRING
}

case class RoadMarginRight() extends Road {
  override val image = Settings.ROAD_MARGIN_RIGHT_IMAGE_STRING
}

case class RoadMarginLeft() extends Road {
  override val image = Settings.ROAD_MARGIN_LEFT_IMAGE_STRING
}