package model.map

import model.environment.{Coordinate, CoordinateImpl}
import utilities.Settings

trait Building extends Tile {
  override def walkable: Boolean = false

  def doorCoordinates: Coordinate
}

case class PokemonCenter() extends Building {
  override val width: Int = Settings.POKEMON_CENTER_WIDTH
  override val height: Int = Settings.POKEMON_CENTER_HEIGHT
  override val doorCoordinates: Coordinate = CoordinateImpl(Settings.POKEMON_CENTER_DOOR_X, Settings.POKEMON_CENTER_DOOR_Y)
}

case class Laboratory() extends Building {
  override val width: Int = Settings.LABORATORY_WIDTH
  override val height: Int = Settings.LABORATORY_HEIGHT
  override val doorCoordinates: Coordinate = CoordinateImpl(Settings.LABORATORY_DOOR_X, Settings.LABORATORY_DOOR_Y)
}