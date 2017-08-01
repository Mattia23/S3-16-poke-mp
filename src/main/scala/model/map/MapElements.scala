package model.map

import model.environment.{Coordinate, CoordinateImpl}

trait MapElements {
  def map: Map[(Int, Int), Tile]

  def map_=(map : Map[(Int, Int), Tile]): Unit

  def addTile(tile: Tile, coordinate: Coordinate): Unit

  def addTileInMultipleCoordinates(tile: Tile, coordinates: Seq[Coordinate]): Unit

  def addMultipleElements(tile: Tile, topLeftCoordinate: Coordinate, bottomRightCoordinate: Coordinate): Unit

  def addCompositeElement(compositeElement: CompositeElement, topLeftCoordinate: Coordinate, bottomRightCoordinate: Coordinate): Unit

}

object MapElements {
  def apply(): MapElements = new MapElementsImpl()
}

class MapElementsImpl extends MapElements {
  override var map: Map[(Int, Int), Tile] = Map[(Int, Int), Tile]()

  override def addTile(tile: Tile, coordinate: Coordinate): Unit = map = map + ((coordinate.x, coordinate.y) -> tile)

  override def addTileInMultipleCoordinates(tile: Tile, coordinates: Seq[Coordinate]): Unit = coordinates foreach(coordinate => addTile(tile, coordinate))

  override def addMultipleElements(tile: Tile, topLeftCoordinate: Coordinate, bottomRightCoordinate: Coordinate): Unit = {
    for (x <- topLeftCoordinate.x to bottomRightCoordinate.x)
      for (y <- topLeftCoordinate.y to bottomRightCoordinate.y)
        addTile(tile, CoordinateImpl(x,y))
  }

  override def addCompositeElement(compositeElement: CompositeElement, topLeftCoordinate: Coordinate, bottomRightCoordinate: Coordinate): Unit = {
    for (x <- topLeftCoordinate.x to bottomRightCoordinate.x)
      for (y <- topLeftCoordinate.y to bottomRightCoordinate.y)
        (x,y) match{
          case (_,_) if x == topLeftCoordinate.x && y == topLeftCoordinate.y => addTile(compositeElement.topLeftTile, CoordinateImpl(x,y))
          case (_,_) if x == topLeftCoordinate.x && y == bottomRightCoordinate.y => addTile(compositeElement.bottomLeftTile, CoordinateImpl(x,y))
          case (_,_) if x == bottomRightCoordinate.x && y == topLeftCoordinate.y => addTile(compositeElement.topRightTile, CoordinateImpl(x,y))
          case (_,_) if x == bottomRightCoordinate.x && y == bottomRightCoordinate.y => addTile(compositeElement.bottomRightTile, CoordinateImpl(x,y))
          case (_,_) if x == topLeftCoordinate.x => addTile(compositeElement.leftTile, CoordinateImpl(x,y))
          case (_,_) if x == bottomRightCoordinate.x => addTile(compositeElement.rightTile, CoordinateImpl(x,y))
          case (_,_) if y == topLeftCoordinate.y => addTile(compositeElement.topTile, CoordinateImpl(x,y))
          case (_,_) if y == bottomRightCoordinate.y => addTile(compositeElement.bottomTile, CoordinateImpl(x,y))
          case _ => addTile(compositeElement.tile, CoordinateImpl(x,y))
        }
  }

}