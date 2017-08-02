package model.map

import java.awt.Image

import model.entities._
import model.environment.{Coordinate, CoordinateImpl}
import utilities.Settings
import view.LoadImage
import Tile._

trait BuildingMap extends BasicMap{  
  def image: Image
  def matricesNotWalkable: List[Tuple2[Coordinate, Coordinate]]
  def npc: StaticCharacter
  def npc_=(staticCharacter: StaticCharacter): Unit
  def entryCoordinate: Coordinate

  def pokemonNpc: List[PokemonCharacter]

  protected def setNotWalkableArea(): Unit = {
    for(matrixNotWalkable <- matricesNotWalkable){
      for( i <- matrixNotWalkable._1.x to matrixNotWalkable._2.x){
        for( j <- matrixNotWalkable._1.y to matrixNotWalkable._2.y){
          map(i)(j) = Barrier()
        }
      }
    }
    map(npc.coordinate.x)(npc.coordinate.y) = Barrier()
  }

  protected def setBasicTilesInMap(): Unit = {
    for(i <- map.indices){
      for(j <- map(0).indices){
        map(i)(j) = BasicTile()
      }
    }
  }
}

class PokemonCenterMap extends BuildingMap{
  override def height: Int = 9
  override def width: Int = 15
  override val map: Array[Array[Tile]]= Array.ofDim[Tile](width, height)

  override var npc: StaticCharacter = new Doctor

  override val image: Image = LoadImage.load(Settings.Images.MAP_IMAGES_FOLDER + "pokemon-center.png")

  override val matricesNotWalkable: List[Tuple2[Coordinate, Coordinate]] =
    List(Tuple2(CoordinateImpl(0,0),CoordinateImpl(14,1)),
         Tuple2(CoordinateImpl(4,2),CoordinateImpl(10,3)),
         Tuple2(CoordinateImpl(0,5),CoordinateImpl(1,6)),
         Tuple2(CoordinateImpl(0,8),CoordinateImpl(0,8)),
         Tuple2(CoordinateImpl(14,8),CoordinateImpl(14,8)),
         Tuple2(CoordinateImpl(11,6),CoordinateImpl(12,7)))

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
  override val map: Array[Array[Tile]] = Array.ofDim[Tile](width, height)

  override var npc: StaticCharacter = new Oak

  override val image: Image = LoadImage.load(Settings.Images.MAP_IMAGES_FOLDER + "laboratory.png")

  override val matricesNotWalkable: List[Tuple2[Coordinate, Coordinate]] =
    List(Tuple2(CoordinateImpl(0,0),CoordinateImpl(12,1)),
         Tuple2(CoordinateImpl(0,3),CoordinateImpl(2,4)),
         Tuple2(CoordinateImpl(1,5),CoordinateImpl(2,5)),
         Tuple2(CoordinateImpl(8,4),CoordinateImpl(10,4)),
         Tuple2(CoordinateImpl(0,7),CoordinateImpl(4,8)),
         Tuple2(CoordinateImpl(8,7),CoordinateImpl(12,8)),
         Tuple2(CoordinateImpl(0,11),CoordinateImpl(0,12)),
         Tuple2(CoordinateImpl(12,11),CoordinateImpl(12,12)))

  override val entryCoordinate: Coordinate = CoordinateImpl(6,12)

  setBasicTilesInMap()
  setNotWalkableArea()

  private val bulbasaur: PokemonCharacter = PokemonCharacter(PokemonCharacter.InitialPokemon.Bulbasaur)
  private val charmander: PokemonCharacter = PokemonCharacter(PokemonCharacter.InitialPokemon.Charmander)
  private val squirtle: PokemonCharacter = PokemonCharacter(PokemonCharacter.InitialPokemon.Squirtle)
  override val pokemonNpc: List[PokemonCharacter] = List(bulbasaur, charmander, squirtle)
}
