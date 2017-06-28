package model.environment

trait Coordinate {
  def x: Int
  def y: Int
}

object Coordinate {
  implicit def coordinateToTuple2(coordinate: Coordinate): (Int,Int) = (coordinate.x, coordinate.y)
  implicit def tuple2ToCoordinate(t: (Int, Int)): Coordinate = CoordinateImpl(t._1, t._2)
}

case class CoordinateImpl(override val x: Int, override val y: Int) extends Coordinate
