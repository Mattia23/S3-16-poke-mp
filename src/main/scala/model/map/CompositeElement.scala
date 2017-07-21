package model.map

trait CompositeElement {
  def tile: Tile
  def topTile: Tile
  def topLeftTile: Tile
  def topRightTile: Tile
  def leftTile: Tile
  def rightTile: Tile
  def bottomTile: Tile
  def bottomLeftTile: Tile
  def bottomRightTile: Tile
}

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