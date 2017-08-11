package model.map

import model.environment.{Coordinate, CoordinateImpl}
import utilities.Settings

/**
  * Building represents a tile of a building
  */
trait Building extends Tile {

  /**
    * @inheritdoc
    * @return true if the tile is walkable, false otherwise
    */
  override def walkable: Boolean = false

  /**
    * @return the coordinates of the building door
    */
  def doorCoordinates: Coordinate

  /**
    * @return the top left coordinates of the building
    */
  def topLeftCoordinate: Coordinate

}

/**
  * PokemonCenter represents a Pokemon Center building
  * @param topLeftCoordinate the top left coordinates of the Pokemon Center
  */
case class PokemonCenter(override val topLeftCoordinate: Coordinate) extends Building {
  override val width = Settings.Constants.POKEMON_CENTER_WIDTH
  override val height = Settings.Constants.POKEMON_CENTER_HEIGHT
  override val image = Settings.Images.POKEMON_CENTER_IMAGE_STRING
  override val doorCoordinates = CoordinateImpl(Settings.Constants.POKEMON_CENTER_DOOR_X, Settings.Constants.POKEMON_CENTER_DOOR_Y)
}

/**
  * Labpratory represents a Laboratory building
  * @param topLeftCoordinate the top left coordinates of the Laboratory
  */
case class Laboratory(override val topLeftCoordinate: Coordinate) extends Building {
  override val width = Settings.Constants.LABORATORY_WIDTH
  override val height = Settings.Constants.LABORATORY_HEIGHT
  override val image = Settings.Images.LABORATORY_IMAGE_STRING
  override val doorCoordinates = CoordinateImpl(Settings.Constants.LABORATORY_DOOR_X, Settings.Constants.LABORATORY_DOOR_Y)
}