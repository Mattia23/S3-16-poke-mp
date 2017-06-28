import model.environment.{Coordinate, CoordinateImpl}
import model.map.{MapElements, TallGrass}
import org.scalatest.FunSuite

class MapElementsTest extends FunSuite{

  def fixture =
    new {
      val mapElements = MapElements()
      val coordinate1 = CoordinateImpl(1,1)
      val coordinate2 = CoordinateImpl(1,5)
      val coordinate3 = CoordinateImpl(3,0)
      val coordinates: Seq[Coordinate] = Seq(coordinate1, coordinate2, coordinate3)
      val tallGrass = TallGrass()
    }

  test("A new element is added to the map elements") {
    val f = fixture
    f.mapElements.addTile(f.tallGrass, f.coordinate1)
    assert(f.mapElements.map(f.coordinate1) == f.tallGrass)
  }

  test("An element is added in multiple coordinates to the map elements") {
    val f = fixture
    f.mapElements.addTileInMultipleCoordinates(f.tallGrass, f.coordinates)
    assert(f.mapElements.map(f.coordinate1) == f.tallGrass)
    assert(f.mapElements.map(f.coordinate2) == f.tallGrass)
    assert(f.mapElements.map(f.coordinate3) == f.tallGrass)
  }
}
