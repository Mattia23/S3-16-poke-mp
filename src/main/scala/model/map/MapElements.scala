package model.map

import model.environment.Coordinate

trait MapElements {
  def map: Map[(Int, Int), Tile]

  def map_=(map : Map[(Int, Int), Tile]): Unit

  def addTile(tile: Tile, coordinate: Coordinate): Unit

  def addTileInMultipleCoordinates(tile: Tile, coordinates: Seq[Coordinate]): Unit
}

object MapElements {
  def apply(): MapElements = new MapElementsImpl()
}

class MapElementsImpl extends MapElements {
  override var map: Map[(Int, Int), Tile] = Map[(Int, Int), Tile]()

  override def addTile(tile: Tile, coordinate: Coordinate): Unit = map = map + ((coordinate.x, coordinate.y) -> tile)

  override def addTileInMultipleCoordinates(tile: Tile, coordinates: Seq[Coordinate]): Unit ={
    coordinates foreach(coordinate => addTile(tile, coordinate))
  }
}