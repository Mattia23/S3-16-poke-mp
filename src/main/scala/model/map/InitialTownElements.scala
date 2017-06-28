package model.map

import model.environment.CoordinateImpl
import utilities.Settings

case class InitialTownElements() extends MapElementsImpl {

  for (x <- 0 until Settings.MAP_WIDTH)
    for (y <- 0 until Settings.MAP_HEIGHT)
      if (x == 0 || x == Settings.MAP_WIDTH - 1 || y == 0 || y == Settings.MAP_HEIGHT - 1) addTile(Tree(), CoordinateImpl(x,y))

  addTile(PokemonCenter(), CoordinateImpl(10,40))
  addTile(Laboratory(), CoordinateImpl(30,40))

  for (x <- 30 until 40)
    for (y <- 45 until 49)
      addTile(Water(), CoordinateImpl(x,y))

  for (x <- 20 until 30)
    for (y <- 20 until 30)
      addTile(TallGrass(), CoordinateImpl(x,y))

}