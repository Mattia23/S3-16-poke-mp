package model.map

import utilities.Settings

trait Building extends Tile {
  override def walkable: Boolean = false

  def totalHeightInTales: Int

  def totalWidthInTales: Int

  def doorCoordinates: Coordinate
}

case class PokemonCenter(override val totalHeightInTales: Int = 3, override val totalWidthInTales: Int = 3,
                         override val doorCoordinates: Coordinate = Coordinate(Settings.POKEMON_CENTER_DOOR_X, Settings.POKEMON_CENTER_DOOR_Y)) extends Building

case class Laboratory(override val totalHeightInTales: Int = 5, override val totalWidthInTales: Int = 5,
                      override val doorCoordinates: Coordinate = Coordinate(Settings.LABORATORY_DOOR_X, Settings.LABORATORY_DOOR_Y)) extends Building