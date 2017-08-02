package model.environment

/**
  * Coordinate represent a position of an element (tile, building, trainer) in the map expressed in x and y coordinate
  */
trait Coordinate {
  /**
    * @return the x coordinate
    */
  def x: Int

  /**
    * @return the y coordinate
    */
  def y: Int
}

object Coordinate {
  implicit def coordinateToTuple2(coordinate: Coordinate): (Int,Int) = (coordinate.x, coordinate.y)
  implicit def tuple2ToCoordinate(t: (Int, Int)): Coordinate = CoordinateImpl(t._1, t._2)
}

/**
  * @inheritdoc
  * @param x x coordinate
  * @param y y coordinate
  */
case class CoordinateImpl(override val x: Int,
                          override val y: Int) extends Coordinate
