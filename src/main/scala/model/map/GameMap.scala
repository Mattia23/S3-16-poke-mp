package model.map

import model.environment.{Coordinate, CoordinateImpl}

trait GameMap extends BasicMap{
  def addTile(coordinate: Coordinate, tile: Tile): Unit
}

object GameMap {
  def apply(height: Int, width: Int): GameMap = new GameMapImpl(height: Int, width)
}

class GameMapImpl(override val height: Int, override val width: Int) extends GameMap{

  override val map = Array.ofDim[Tile](height,width)
  initMap()

  private def initMap(): Unit = {
    for( x <- 0 until width)
      for( y <- 0 until height)
        map(x)(y) = Tile.Grass()
  }

  override def addTile(coordinate: Coordinate, tile: Tile): Unit = coordinate match {
    case CoordinateImpl(x,y) if ( x >= 0 && y >= 0 ) && ( x + tile.width <= width && y + tile.height <= height) =>
      for (x <- coordinate.x until coordinate.x + tile.width)
        for (y <- coordinate.y until coordinate.y + tile.height)
          map(x)(y) = tile
    case _ => throw WrongPositionException()
  }

}

