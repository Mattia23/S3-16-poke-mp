
import model.map._
import org.scalatest.FunSuite
import model.map.Tile._

class BuildingMapTest extends FunSuite{

  def fixture =
    new {
      val pokemonCenterMap: PokemonCenterMap = new PokemonCenterMap
      val laboratoryMap: LaboratoryMap = new LaboratoryMap
    }

  test("Test that every building map element is a Tile") {
    val f = fixture
    f.pokemonCenterMap.map foreach (element => element foreach (element2 => assert(element2.isInstanceOf[Tile])))
    f.laboratoryMap.map foreach (element => element foreach (element2 => assert(element2.isInstanceOf[Tile])))
  }

  test("Test box position") {
    val f = fixture
    assert(f.pokemonCenterMap.map(f.pokemonCenterMap.boxCoordinate.x)(f.pokemonCenterMap.boxCoordinate.y).isInstanceOf[Box])
  }

  test("Test Barrier position"){
    val f = fixture
    testBarrier(f.laboratoryMap)
  }

  def testBarrier(buildingMap: BuildingMap): Unit = {
    for(matrixNotWalkable <- buildingMap.matriciesNotWalkable){
      for( i <- matrixNotWalkable.startCoordinate.x to matrixNotWalkable.endCoordiante.x){
        for( j <- matrixNotWalkable.startCoordinate.y to matrixNotWalkable.endCoordiante.y){
          assert(buildingMap.map(i)(j).isInstanceOf[Barrier])
        }
      }
    }
  }

}
