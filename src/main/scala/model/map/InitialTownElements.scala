package model.map

import model.environment.CoordinateImpl
import model.map.CompositeElement.{Lake, Square}
import utilities.Settings

case class InitialTownElements() extends MapElementsImpl {

  addBuildings()
  addLakes()
  addTallGrass()
  addTrees()

  addCompositeElement(Square(), CoordinateImpl(21,21), CoordinateImpl(28,28))
  addCompositeElement(Square(), CoordinateImpl(6,35), CoordinateImpl(16,45))
  addRoadFromSquareToPokemonCenter()
  addRoadFromSquareToLaboratory()
  addRoadFromSquareToBottomLeftLake()
  addRoadFromSquareToTopTallGrass()

  private def addBuildings(): Unit ={
    addTile(PokemonCenter(CoordinateImpl(10,19)), CoordinateImpl(10,19))
    addTile(Laboratory(CoordinateImpl(40,20)), CoordinateImpl(40,20))
  }

  import Settings.Constants._
  private def addTrees(): Unit ={
    for (x <- 0 until MAP_WIDTH)
      for (y <- 0 until MAP_HEIGHT)
        if (x == 0 || x == MAP_WIDTH - 1 || y == 0 || y == MAP_HEIGHT - 1) addTile(Tree(), CoordinateImpl(x,y))

    addMultipleElements(Tree(), CoordinateImpl(1,18), CoordinateImpl(14,18))
    addMultipleElements(Tree(), CoordinateImpl(6,34), CoordinateImpl(8,34))
    addMultipleElements(Tree(), CoordinateImpl(6,46), CoordinateImpl(8,46))
    addMultipleElements(Tree(), CoordinateImpl(14,34), CoordinateImpl(16,34))
    addMultipleElements(Tree(), CoordinateImpl(14,46), CoordinateImpl(16,46))
    addMultipleElements(Tree(), CoordinateImpl(5,34), CoordinateImpl(5,37))
    addMultipleElements(Tree(), CoordinateImpl(17,34), CoordinateImpl(17,37))
    addMultipleElements(Tree(), CoordinateImpl(5,43), CoordinateImpl(5,46))
    addMultipleElements(Tree(), CoordinateImpl(17,43), CoordinateImpl(17,46))
    addMultipleElements(Tree(), CoordinateImpl(20,18), CoordinateImpl(20,23))
    addMultipleElements(Tree(), CoordinateImpl(9,28), CoordinateImpl(9,31))
    addMultipleElements(Tree(), CoordinateImpl(20,28), CoordinateImpl(20,31))
    addMultipleElements(Tree(), CoordinateImpl(10,31), CoordinateImpl(11,31))
    addMultipleElements(Tree(), CoordinateImpl(18,31), CoordinateImpl(19,31))
    addMultipleElements(Tree(), CoordinateImpl(15,18), CoordinateImpl(16,23))
    addMultipleElements(Tree(), CoordinateImpl(43,1), CoordinateImpl(49,4))
    addMultipleElements(Tree(), CoordinateImpl(35,32), CoordinateImpl(39,36))

    val trees = Seq[CoordinateImpl](CoordinateImpl(39,23), CoordinateImpl(47,23), CoordinateImpl(29, 23), CoordinateImpl(34,23))
    addTileInMultipleCoordinates(Tree(), trees)
  }

  private def addLakes(): Unit ={
    addCompositeElement(Lake(),CoordinateImpl(4,5), CoordinateImpl(20,10))
    addCompositeElement(Lake(),CoordinateImpl(40,40), CoordinateImpl(48,48))
    addCompositeElement(Lake(),CoordinateImpl(10,28), CoordinateImpl(19,30))
  }

  private def addTallGrass(): Unit ={
    addMultipleElements(TallGrass(), CoordinateImpl(1,1), CoordinateImpl(42,4))
    addMultipleElements(TallGrass(), CoordinateImpl(1,5), CoordinateImpl(3,10))
    addMultipleElements(TallGrass(), CoordinateImpl(1,19), CoordinateImpl(9,23))
    addMultipleElements(TallGrass(), CoordinateImpl(1,24), CoordinateImpl(8,27))
    addMultipleElements(TallGrass(), CoordinateImpl(1,28), CoordinateImpl(8,31))
    addMultipleElements(TallGrass(), CoordinateImpl(37,5), CoordinateImpl(49,11))
    addMultipleElements(TallGrass(), CoordinateImpl(17,18), CoordinateImpl(19,23))
    addMultipleElements(TallGrass(), CoordinateImpl(38,1), CoordinateImpl(38,8))
    addMultipleElements(TallGrass(), CoordinateImpl(36,1), CoordinateImpl(37,5))
    addMultipleElements(TallGrass(), CoordinateImpl(42,11), CoordinateImpl(48,13))
    addMultipleElements(TallGrass(), CoordinateImpl(23,47), CoordinateImpl(39,48))
    addMultipleElements(TallGrass(), CoordinateImpl(33,31), CoordinateImpl(41,37))
  }

  private def addRoadFromSquareToPokemonCenter(): Unit = {
    for (x <- 9 to 21)  x match {
      case 9 =>
        addTile(RoadMarginTopLeft(),CoordinateImpl(x,24))
        addTile(RoadMarginLeft(), CoordinateImpl(x,25))
        addTile(RoadMarginLeft(), CoordinateImpl(x,26))
        addTile(RoadMarginBottomLeft(),CoordinateImpl(x,27))
      case 21 =>
        addTile(Road(), CoordinateImpl(x,24))
        addTile(Road(), CoordinateImpl(x,25))
        addTile(Road(), CoordinateImpl(x,26))
        addTile(Road(), CoordinateImpl(x,27))
      case _ =>
        addTile(RoadMarginTop(), CoordinateImpl(x,24))
        addTile(Road(), CoordinateImpl(x,25))
        addTile(Road(), CoordinateImpl(x,26))
        addTile(RoadMarginBottom(), CoordinateImpl(x,27))
    }
  }

  private def addRoadFromSquareToLaboratory(): Unit = {
    for (x <- 28 to 47)  x match {
      case 28 =>
        addTile(Road(), CoordinateImpl(x,24))
        addTile(Road(), CoordinateImpl(x,25))
        addTile(Road(), CoordinateImpl(x,26))
        addTile(Road(), CoordinateImpl(x,27))
      case 47 =>
        addTile(RoadMarginTopRight(),CoordinateImpl(x,24))
        addTile(RoadMarginRight(), CoordinateImpl(x,25))
        addTile(RoadMarginRight(), CoordinateImpl(x,26))
        addTile(RoadMarginBottomRight(),CoordinateImpl(x,27))
      case _ =>
        addTile(RoadMarginTop(), CoordinateImpl(x,24))
        addTile(Road(), CoordinateImpl(x,25))
        addTile(Road(), CoordinateImpl(x,26))
        addTile(RoadMarginBottom(), CoordinateImpl(x,27))
    }
  }

  private def addRoadFromSquareToBottomLeftLake(): Unit = {
    for (y <- 28 to 46)  y match {
      case 28 =>
        addTile(Road(), CoordinateImpl(23,y))
        addTile(Road(), CoordinateImpl(24,y))
        addTile(Road(), CoordinateImpl(25,y))
        addTile(Road(), CoordinateImpl(26,y))
      case 46 =>
        addTile(RoadMarginBottomLeft(),CoordinateImpl(23,y))
        addTile(RoadMarginBottom(), CoordinateImpl(24,y))
        addTile(RoadMarginBottom(), CoordinateImpl(25,y))
        addTile(RoadMarginBottomRight(),CoordinateImpl(26,y))
      case _ =>
        addTile(RoadMarginLeft(), CoordinateImpl(23,y))
        addTile(Road(), CoordinateImpl(24,y))
        addTile(Road(), CoordinateImpl(25,y))
        addTile(RoadMarginRight(), CoordinateImpl(26,y))
    }

    for (x <- 26 to 39) x match {
      case 26 =>
        addTile(Road(), CoordinateImpl(x,43))
        addTile(Road(), CoordinateImpl(x,44))
        addTile(Road(), CoordinateImpl(x,45))
        addTile(RoadMarginBottom(), CoordinateImpl(x,46))
      case 39 =>
        addTile(RoadMarginTopRight(),CoordinateImpl(x,43))
        addTile(RoadMarginRight(), CoordinateImpl(x,44))
        addTile(RoadMarginRight(), CoordinateImpl(x,45))
        addTile(RoadMarginBottomRight(),CoordinateImpl(x,46))
      case _ =>
        addTile(RoadMarginTop(), CoordinateImpl(x,43))
        addTile(Road(), CoordinateImpl(x,44))
        addTile(Road(), CoordinateImpl(x,45))
        addTile(RoadMarginBottom(), CoordinateImpl(x,46))
    }
  }

  private def addRoadFromSquareToTopTallGrass(): Unit = {
    for (y <- 6 to 21)  y match {
      case 6 =>
        addTile(RoadMarginTopLeft(),CoordinateImpl(23,y))
        addTile(RoadMarginTop(), CoordinateImpl(24,y))
        addTile(RoadMarginTop(), CoordinateImpl(25,y))
        addTile(RoadMarginTopRight(),CoordinateImpl(26,y))
      case 21 =>
        addTile(Road(), CoordinateImpl(23,y))
        addTile(Road(), CoordinateImpl(24,y))
        addTile(Road(), CoordinateImpl(25,y))
        addTile(Road(), CoordinateImpl(26,y))
      case _ =>
        addTile(RoadMarginLeft(), CoordinateImpl(23,y))
        addTile(Road(), CoordinateImpl(24,y))
        addTile(Road(), CoordinateImpl(25,y))
        addTile(RoadMarginRight(), CoordinateImpl(26,y))
    }

    for (x <- 26 to 36) x match {
      case 26 =>
        addTile(RoadMarginTop(), CoordinateImpl(x,6))
        addTile(Road(), CoordinateImpl(x,7))
        addTile(Road(), CoordinateImpl(x,8))
        addTile(Road(), CoordinateImpl(x,9))
      case 36 =>
        addTile(RoadMarginTopRight(),CoordinateImpl(x,6))
        addTile(RoadMarginRight(), CoordinateImpl(x,7))
        addTile(RoadMarginRight(), CoordinateImpl(x,8))
        addTile(RoadMarginBottomRight(),CoordinateImpl(x,9))
      case _ =>
        addTile(RoadMarginTop(), CoordinateImpl(x,6))
        addTile(Road(), CoordinateImpl(x,7))
        addTile(Road(), CoordinateImpl(x,8))
        addTile(RoadMarginBottom(), CoordinateImpl(x,9))
    }
  }

}