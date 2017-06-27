package model.environment

trait Coordinate {
  def x: Int
  def y: Int
}

case class CoordinateImpl(override val x: Int, override val y: Int) extends Coordinate
