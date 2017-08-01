package model.map

import java.awt.Image

import model.entities._
import model.environment.{Coordinate, CoordinateImpl, MatrixCoordinate}
import utilities.Settings
import view.LoadImage
import Tile._

trait BuildingMap extends BasicMap{  
  def image: Image
  def matricesNotWalkable: List[MatrixCoordinate]
  def npc: StaticCharacter
  def npc_=(staticCharacter: StaticCharacter): Unit = npc = staticCharacter
  def entryCoordinate: Coordinate

  def pokemonNpc: List[PokemonCharacter]

  protected def setNotWalkableArea(): Unit = {
    for(matrixNotWalkable <- matricesNotWalkable){
      for( i <- matrixNotWalkable.startCoordinate.x to matrixNotWalkable.endCoordinate.x){
        for( j <- matrixNotWalkable.startCoordinate.y to matrixNotWalkable.endCoordinate.y){
          map(i)(j) = Barrier()
        }
      }
    }
    map(npc.coordinate.x)(npc.coordinate.y) = Barrier()
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

  override val image: Image = LoadImage.load(Settings.Images.MAP_IMAGES_FOLDER + "pokemon-center.png")

  override val matricesNotWalkable: List[MatrixCoordinate] =
    List(new MatrixCoordinate(CoordinateImpl(0,0),CoordinateImpl(14,1)),
      new MatrixCoordinate(CoordinateImpl(4,2),CoordinateImpl(10,3)),
      new MatrixCoordinate(CoordinateImpl(0,5),CoordinateImpl(1,6)),
      new MatrixCoordinate(CoordinateImpl(0,8),CoordinateImpl(0,8)),
      new MatrixCoordinate(CoordinateImpl(14,8),CoordinateImpl(14,8)),
      new MatrixCoordinate(CoordinateImpl(11,6),CoordinateImpl(12,7)))

  override val entryCoordinate: Coordinate = CoordinateImpl(7,8)

  setBasicTilesInMap()
  setNotWalkableArea()

  val boxCoordinate: Coordinate = CoordinateImpl(11, 1)
  map(boxCoordinate.x)(boxCoordinate.y) = Box()

  override def pokemonNpc: List[PokemonCharacter] = null
}

class LaboratoryMap extends BuildingMap{
  override val height: Int = 13
  override val width: Int = 13
  override val map: Array[Array[Tile]]= Array.ofDim[Tile](width, height)

  override var npc: StaticCharacter = new Oak

  override val image: Image = LoadImage.load(Settings.Images.MAP_IMAGES_FOLDER + "laboratory.png")

  override val matricesNotWalkable: List[MatrixCoordinate] =
    List(new MatrixCoordinate(CoordinateImpl(0,0),CoordinateImpl(12,1)),
      new MatrixCoordinate(CoordinateImpl(0,3),CoordinateImpl(2,4)),
      new MatrixCoordinate(CoordinateImpl(1,5),CoordinateImpl(2,5)),
      new MatrixCoordinate(CoordinateImpl(8,4),CoordinateImpl(10,4)),
      new MatrixCoordinate(CoordinateImpl(0,7),CoordinateImpl(4,8)),
      new MatrixCoordinate(CoordinateImpl(8,7),CoordinateImpl(12,8)),
      new MatrixCoordinate(CoordinateImpl(0,11),CoordinateImpl(0,12)),
      new MatrixCoordinate(CoordinateImpl(12,11),CoordinateImpl(12,12)))

  override val entryCoordinate: Coordinate = CoordinateImpl(6,12)

  setBasicTilesInMap()
  setNotWalkableArea()

  private val bulbasaur: PokemonCharacter = PokemonCharacter(PokemonCharacter.InitialPokemon.Bulbasaur)
  private val charmander: PokemonCharacter = PokemonCharacter(PokemonCharacter.InitialPokemon.Charmander)
  private val squirtle: PokemonCharacter = PokemonCharacter(PokemonCharacter.InitialPokemon.Squirtle)
  override val pokemonNpc: List[PokemonCharacter] = List(bulbasaur, charmander, squirtle)
}
