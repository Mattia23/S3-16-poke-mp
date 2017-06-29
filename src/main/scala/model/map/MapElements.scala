package model.map

import model.environment.{Coordinate, CoordinateImpl}

trait MapElements {
  def map: Map[(Int, Int), Tile]

  def map_=(map : Map[(Int, Int), Tile]): Unit

  def addTile(tile: Tile, coordinate: Coordinate): Unit

  def addTileInMultipleCoordinates(tile: Tile, coordinates: Seq[Coordinate]): Unit

  def addLake(topLeftCoordinate: Coordinate, bottomRightCoordinate: Coordinate): Unit
}

object MapElements {
  def apply(): MapElements = new MapElementsImpl()
}

class MapElementsImpl extends MapElements {
  override var map = Map[(Int, Int), Tile]()

  override def addTile(tile: Tile, coordinate: Coordinate) = map = map + ((coordinate.x, coordinate.y) -> tile)

  override def addTileInMultipleCoordinates(tile: Tile, coordinates: Seq[Coordinate]) ={
    coordinates foreach(coordinate => addTile(tile, coordinate))
  }

  override def addLake(topLeftCoordinate: Coordinate, bottomRightCoordinate: Coordinate) = {
    for (x <- topLeftCoordinate.x to bottomRightCoordinate.x)
      for (y <- topLeftCoordinate.y to bottomRightCoordinate.y)
        (x,y) match{
          case (x,y) if x == topLeftCoordinate.x && y == topLeftCoordinate.y => addTile(WaterMarginTopLeft(), CoordinateImpl(x,y))
          case (x,y) if x == topLeftCoordinate.x && y == bottomRightCoordinate.y => addTile(WaterMarginBottomLeft(), CoordinateImpl(x,y))
          case (x,y) if x == bottomRightCoordinate.x && y == topLeftCoordinate.y => addTile(WaterMarginTopRight(), CoordinateImpl(x,y))
          case (x,y) if x == bottomRightCoordinate.x && y == bottomRightCoordinate.y => addTile(WaterMarginBottomRight(), CoordinateImpl(x,y))
          case (x,_) if x == topLeftCoordinate.x => addTile(WaterMarginLeft(), CoordinateImpl(x,y))
          case (x,_) if x == bottomRightCoordinate.x => addTile(WaterMarginRight(), CoordinateImpl(x,y))
          case (_,y) if y == topLeftCoordinate.y => addTile(WaterMarginTop(), CoordinateImpl(x,y))
          case (_,y) if y == bottomRightCoordinate.y => addTile(WaterMarginBottom(), CoordinateImpl(x,y))
          case _ => addTile(Water(), CoordinateImpl(x,y))
        }
  }
}