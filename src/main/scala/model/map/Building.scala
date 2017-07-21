package model.map

import model.environment.{Coordinate, CoordinateImpl}
import utilities.Settings

trait Building extends Tile {
  override def walkable: Boolean = false

  def doorCoordinates: Coordinate

  def topLeftCoordinate: Coordinate

}

case class PokemonCenter(override val topLeftCoordinate: Coordinate) extends Building {
  override val width = Settings.POKEMON_CENTER_WIDTH
  override val height = Settings.POKEMON_CENTER_HEIGHT
  override val image = Settings.POKEMON_CENTER_IMAGE_STRING
  override val doorCoordinates = CoordinateImpl(Settings.POKEMON_CENTER_DOOR_X, Settings.POKEMON_CENTER_DOOR_Y)
}

case class Laboratory(override val topLeftCoordinate: Coordinate) extends Building {
  override val width = Settings.LABORATORY_WIDTH
  override val height = Settings.LABORATORY_HEIGHT
  override val image = Settings.LABORATORY_IMAGE_STRING
  override val doorCoordinates = CoordinateImpl(Settings.LABORATORY_DOOR_X, Settings.LABORATORY_DOOR_Y)
}