package model.map

import model.environment.Coordinate

trait GameMap {
  def map: Array[Array[Tile]]

  def height: Int

  def height_=(height: Int): Unit

  def width: Unit

  def width_=(width: Int): Unit

  def addTile(coordinate: Coordinate, tile: Tile): Unit

  def removeTile(coordinate: Coordinate): Unit

  def addBuilding(coordinate: Coordinate, building: Building): Unit

  def removeBuilding(coordinate: Coordinate, building: Building): Unit
}

case class GameMapImpl(override var height: Int, override var width: Int) extends GameMap{

  override val map = Array.ofDim[Tile](height,width)
  this.initMap(height,width)

  private def initMap(height: Int, width: Int): Unit = Array.tabulate(height,width)( (x,y) => Grass() )

  override def addTile(coordinate: Coordinate, tile: Tile): Unit = map(coordinate.x)(coordinate.y) = tile

  override def removeTile(coordinate: Coordinate): Unit = map(coordinate.x)(coordinate.y) = Grass()

  override def addBuilding(coordinate: Coordinate, building: Building): Unit =
    Array.range(coordinate.x, building.width).map(x => Array.range(coordinate.y, building.height).map(y => building))

  override def removeBuilding(coordinate: Coordinate, building: Building): Unit =
    Array.range(coordinate.x, building.width).map(x => Array.range(coordinate.y, building.height).map(y => Grass()))

}