package model.environment

import java.awt.Image

import model.characters._
import model.map.{Barrier, BasicMap, BasicTile, Tile}
import utilities.Settings
import view.LoadImage

trait BuildingMap extends BasicMap{
  def image: Image
  def matriciesNotWalkable: List[MatrixCoordinate]
  def npc: StaticCharacter
  def npcCoordinate: Coordinate
  def entryCoordinate: Coordinate
  def userCoordinate: Coordinate

  def userCoordinate_=(coordinate: Coordinate): Unit

  protected def setNotWalkableArea(): Unit = {
    for(matrixNotWalkable <- matriciesNotWalkable){
      for( i <- matrixNotWalkable.startCoordinate.x to matrixNotWalkable.endCoordiante.x){
        for( j <- matrixNotWalkable.startCoordinate.y to matrixNotWalkable.endCoordiante.y){
          map(i)(j) = Barrier()
        }
      }
    }
    map(npcCoordinate.x)(npcCoordinate.y) = Barrier()
  }

  protected def setBasicTilesInMap(): Unit = {
    for(i <- 0 until map.length){
      for(j <- 0 until map(0).length){
        map(i)(j) = BasicTile()
      }
    }
  }
}

class PokemonCenterMap extends BuildingMap{
  override def height: Int = 9
  override def width: Int = 15
  override val map: Array[Array[Tile]]= Array.ofDim[Tile](width, height)

  override val npc: StaticCharacter = new Doctor

  override val image: Image = LoadImage.load(Settings.MAP_IMAGES_FOLDER + "pokemon-center.png")

  override val matriciesNotWalkable: List[MatrixCoordinate] =
    List(new MatrixCoordinate(CoordinateImpl(0,0),CoordinateImpl(14,1)),
      new MatrixCoordinate(CoordinateImpl(4,2),CoordinateImpl(10,3)),
      new MatrixCoordinate(CoordinateImpl(0,5),CoordinateImpl(1,6)),
      new MatrixCoordinate(CoordinateImpl(0,8),CoordinateImpl(0,8)),
      new MatrixCoordinate(CoordinateImpl(14,8),CoordinateImpl(14,8)),
      new MatrixCoordinate(CoordinateImpl(11,6),CoordinateImpl(12,7)))

  override val npcCoordinate: Coordinate = CoordinateImpl(7,3)
  override val entryCoordinate: Coordinate = CoordinateImpl(7,8)
  override var userCoordinate: Coordinate = entryCoordinate

  setBasicTilesInMap()
  setNotWalkableArea()
}

class LaboratoryMap extends BuildingMap{
  override val height: Int = 13
  override val width: Int = 13
  override val map: Array[Array[Tile]]= Array.ofDim[Tile](width, height)

  override val npc: StaticCharacter = new Oak

  override val image: Image = LoadImage.load(Settings.MAP_IMAGES_FOLDER + "laboratory.png")

  val pokemonNcp: List[PokemonCharacter] = List(new Bulbasaur, new Charmander, new Squirtle)

  override val matriciesNotWalkable: List[MatrixCoordinate] =
    List(new MatrixCoordinate(CoordinateImpl(0,0),CoordinateImpl(12,1)),
      new MatrixCoordinate(CoordinateImpl(0,3),CoordinateImpl(2,4)),
      new MatrixCoordinate(CoordinateImpl(1,5),CoordinateImpl(2,5)),
      new MatrixCoordinate(CoordinateImpl(8,4),CoordinateImpl(10,5)),
      new MatrixCoordinate(CoordinateImpl(0,7),CoordinateImpl(4,8)),
      new MatrixCoordinate(CoordinateImpl(8,7),CoordinateImpl(12,8)),
      new MatrixCoordinate(CoordinateImpl(0,11),CoordinateImpl(0,12)),
      new MatrixCoordinate(CoordinateImpl(12,11),CoordinateImpl(12,12)))

  override val npcCoordinate: Coordinate = CoordinateImpl(6,4)
  override val entryCoordinate: Coordinate = CoordinateImpl(6,12)
  override var userCoordinate: Coordinate = CoordinateImpl(6,12)

  setBasicTilesInMap()
  setNotWalkableArea()
}
