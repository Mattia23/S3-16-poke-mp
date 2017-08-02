package model.map

/**
  * CompositeElement represents a map element that is composed by more than one tile
  */
trait CompositeElement {
  /**
    * @return the tile that does not represent a border tile
    */
  def tile: Tile

  /**
    * @return the tile that represent the top margin
    */
  def topTile: Tile

  /**
    * @return the tile that represent the top left margin
    */
  def topLeftTile: Tile

  /**
    * @return the tile that represent the top right margin
    */
  def topRightTile: Tile

  /**
    * @return the tile that represent the left margin
    */
  def leftTile: Tile

  /**
    * @return the tile that represent the right margin
    */
  def rightTile: Tile

  /**
    * @return the tile that represent the bottom margin
    */
  def bottomTile: Tile

  /**
    * @return the tile that represent the bottom left margin
    */
  def bottomLeftTile: Tile

  /**
    * @return the tile that represent the bottom right margin
    */
  def bottomRightTile: Tile
}

object CompositeElement {
  import Tile._

  /**
    * Lake represent a lake in the map
    */
  case class Lake() extends  CompositeElement{
    override val tile = Water()

    override val topTile = WaterMarginTop()

    override val topLeftTile = WaterMarginTopLeft()

    override val topRightTile = WaterMarginTopRight()

    override val leftTile = WaterMarginLeft()

    override val rightTile = WaterMarginRight()

    override val bottomTile = WaterMarginBottom()

    override val bottomLeftTile = WaterMarginBottomLeft()

    override val bottomRightTile = WaterMarginBottomRight()
  }

  /**
    * Square represent a square in the map
    */
  case class Square() extends  CompositeElement{
    override val tile = Road()

    override val topTile = RoadMarginTop()

    override val topLeftTile = RoadMarginTopLeft()

    override val topRightTile = RoadMarginTopRight()

    override val leftTile = RoadMarginLeft()

    override val rightTile = RoadMarginRight()

    override val bottomTile = RoadMarginBottom()

    override val bottomLeftTile = RoadMarginBottomLeft()

    override val bottomRightTile = RoadMarginBottomRight()
  }
}