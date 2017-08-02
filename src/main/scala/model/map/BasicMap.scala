package model.map

/**
  * BasicMap represent a map of the game
  */
trait BasicMap {
  /**
    * @return the map as a matrix of Tile
    */
  def map: Array[Array[Tile]]

  /**
    * @return the height of the map
    */
  def height: Int

  /**
    * @return the width of the map
    */
  def width: Int
}
