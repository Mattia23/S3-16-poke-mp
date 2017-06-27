package model.map

trait BasicMap {
  def map: Array[Array[Tile]]

  def height: Int

  def width: Int
}
