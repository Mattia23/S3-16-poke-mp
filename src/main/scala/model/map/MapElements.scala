package model.map

import model.environment.{Coordinate, CoordinateImpl}

/**
  * MapElements represents all the elements that compose the map
  */
trait MapElements {

  /**
    * @return the map of tiles that compose the game map and their coordinates
    */
  def map: Map[(Int, Int), Tile]

  /**
    * @param map the map of tiles that compose the game map and their coordinates
    */
  def map_=(map : Map[(Int, Int), Tile]): Unit

  /**
    * adds a tile and its coordinates to the map
    * @param tile the tile to add
    * @param coordinate the coordinates of the tile
    */
  def addTile(tile: Tile, coordinate: Coordinate): Unit

  /**
    * adds a same tile in more than one coordinates to the map
    * @param tile the tile to add
    * @param coordinates the list of the coordinates of the tile as a Seq
    */
  def addTileInMultipleCoordinates(tile: Tile, coordinates: Seq[Coordinate]): Unit

  /**
    * adds a same tile to the map in a given area
    * @param tile the tile to add
    * @param topLeftCoordinate the top left coordinates of the area
    * @param bottomRightCoordinate the bottom right coordinates of the area
    */
  def addMultipleElements(tile: Tile, topLeftCoordinate: Coordinate, bottomRightCoordinate: Coordinate): Unit

  /**
    * adds a composite element to the map
    * @param compositeElement the composite element to add as CompositeElement
    * @param topLeftCoordinate the top left coordinates of the composite element
    * @param bottomRightCoordinate the bottom right coordinates of the composite element
    */
  def addCompositeElement(compositeElement: CompositeElement, topLeftCoordinate: Coordinate, bottomRightCoordinate: Coordinate): Unit

}

object MapElements {
  def apply(): MapElements = new MapElementsImpl()
}

/**
  * @inheritdoc
  */
class MapElementsImpl extends MapElements {
  override var map: Map[(Int, Int), Tile] = Map[(Int, Int), Tile]()

  /**
    * @inheritdoc
    * @param tile the tile to add
    * @param coordinate the coordinates of the tile
    */
  override def addTile(tile: Tile, coordinate: Coordinate): Unit = map = map + ((coordinate.x, coordinate.y) -> tile)

  /**
    * @inheritdoc
    * @param tile the tile to add
    * @param coordinates the list of the coordinates of the tile as a Seq
    */
  override def addTileInMultipleCoordinates(tile: Tile, coordinates: Seq[Coordinate]): Unit = coordinates foreach(coordinate => addTile(tile, coordinate))

  /**
    * @inheritdoc
    * @param tile the tile to add
    * @param topLeftCoordinate the top left coordinates of the area
    * @param bottomRightCoordinate the bottom right coordinates of the area
    */
  override def addMultipleElements(tile: Tile, topLeftCoordinate: Coordinate, bottomRightCoordinate: Coordinate): Unit = {
    for (x <- topLeftCoordinate.x to bottomRightCoordinate.x)
      for (y <- topLeftCoordinate.y to bottomRightCoordinate.y)
        addTile(tile, CoordinateImpl(x,y))
  }

  /**
    * @inheritdoc
    * @param compositeElement the composite element to add as CompositeElement
    * @param topLeftCoordinate the top left coordinates of the composite element
    * @param bottomRightCoordinate the bottom right coordinates of the composite element
    */
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