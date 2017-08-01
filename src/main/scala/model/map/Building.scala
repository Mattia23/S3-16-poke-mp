package model.map

import model.environment.{Coordinate, CoordinateImpl}
import utilities.Settings

trait Building extends Tile {
  override def walkable: Boolean = false

  def doorCoordinates: Coordinate

  def topLeftCoordinate: Coordinate

}

case class PokemonCenter(override val topLeftCoordinate: Coordinate) extends Building {
  override val width = Settings.Constants.POKEMON_CENTER_WIDTH
  override val height = Settings.Constants.POKEMON_CENTER_HEIGHT
  override val image = Settings.Images.POKEMON_CENTER_IMAGE_STRING
  override val doorCoordinates = CoordinateImpl(Settings.Constants.POKEMON_CENTER_DOOR_X, Settings.Constants.POKEMON_CENTER_DOOR_Y)
}

case class Laboratory(override val topLeftCoordinate: Coordinate) extends Building {
  override val width = Settings.Constants.LABORATORY_WIDTH
  override val height = Settings.Constants.LABORATORY_HEIGHT
  override val image = Settings.Images.LABORATORY_IMAGE_STRING
  override val doorCoordinates = CoordinateImpl(Settings.Constants.LABORATORY_DOOR_X, Settings.Constants.LABORATORY_DOOR_Y)
}