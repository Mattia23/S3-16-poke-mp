package model.map

import model.environment.CoordinateImpl
import utilities.Settings

case class InitialTownElements() extends MapElementsImpl {
  for (x <- 0 until Settings.MAP_WIDTH)
    for (y <- 0 until Settings.MAP_HEIGHT)
      if (x == 0 || x == Settings.MAP_WIDTH - 1 || y == 0 || y == Settings.MAP_HEIGHT - 1) addTile(Tree(), CoordinateImpl(x,y))

  addTile(PokemonCenter(CoordinateImpl(15,30)), CoordinateImpl(15,30))
  addTile(Laboratory(CoordinateImpl(15,15)), CoordinateImpl(15,15))
  addLake(CoordinateImpl(15,5), CoordinateImpl(20,10))

  mainRoad()
  square()

  private def mainRoad(): Unit = {
    for (x <- 9 to 48)  x match {
      case 9 => {
        addTile(RoadMarginTopLeft(),CoordinateImpl(9,24))
        addTile(RoadMarginLeft(), CoordinateImpl(9,25))
        addTile(RoadMarginLeft(), CoordinateImpl(9,26))
        addTile(RoadMarginBottomLeft(),CoordinateImpl(9,27))
      }
      case 48 => {
        addTile(RoadMarginTopRight(),CoordinateImpl(48,24))
        addTile(RoadMarginRight(), CoordinateImpl(48,25))
        addTile(RoadMarginRight(), CoordinateImpl(48,26))
        addTile(RoadMarginBottomRight(),CoordinateImpl(48,27))
      }
      case _ => {
        addTile(RoadMarginTop(), CoordinateImpl(x, 24))
        addTile(Road(), CoordinateImpl(x, 25))
        addTile(Road(), CoordinateImpl(x, 26))
        addTile(RoadMarginBottom(), CoordinateImpl(x,27))
      }
    }
  }

  private def square(): Unit = {
    for (x <- 21 to 28)
      for (y <- 27 to 32)
        (x,y) match {
          case (21,28) | (21,29) | (21,30) | (21,31) => addTile(RoadMarginLeft(), CoordinateImpl(x,y))
          case (21,32) => addTile(RoadMarginBottomLeft(), CoordinateImpl(x,y))
          case (28,28) | (28,29) | (28,30) | (28,31) => addTile(RoadMarginRight(), CoordinateImpl(x,y))
          case (28,32) => addTile(RoadMarginBottomRight(), CoordinateImpl(x,y))
          case (_,32) => addTile(RoadMarginBottom(), CoordinateImpl(x,y))
          case _ => addTile(Road(), CoordinateImpl(x,y))
        }
  }





}