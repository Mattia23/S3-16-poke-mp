package model.environment

trait Coordinate {
  def x: Int
  def y: Int
}

object Coordinate {
  def apply(x: Int, y: Int): Coordinate = new CoordinateImpl(x, y)
}

class CoordinateImpl(override val x: Int, override val y: Int) extends Coordinate
