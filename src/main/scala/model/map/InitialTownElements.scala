package model.map

import model.environment.CoordinateImpl
import utilities.Settings

case class InitialTownElements() extends MapElementsImpl {
  for (x <- 0 until Settings.MAP_WIDTH)
    for (y <- 0 until Settings.MAP_HEIGHT)
      if (x == 0 || x == Settings.MAP_WIDTH - 1 || y == 0 || y == Settings.MAP_HEIGHT - 1) addTile(Tree(), CoordinateImpl(x,y))

  addTile(PokemonCenter(CoordinateImpl(10,19)), CoordinateImpl(10,19))
  addTile(Laboratory(CoordinateImpl(40,20)), CoordinateImpl(40,20))

  addLake(CoordinateImpl(4,5), CoordinateImpl(20,10))
  addLake(CoordinateImpl(40,40), CoordinateImpl(48,48))
  addLake(CoordinateImpl(6,28), CoordinateImpl(15,30))

  for (x <- 6 to 15)
    addTile(TallGrass(), CoordinateImpl(x,31))
  for (y <- 28 to 31) {
    addTile(TallGrass(), CoordinateImpl(5, y))
    addTile(TallGrass(), CoordinateImpl(16, y))
  }

  for (x <- 1 to 20)
    for (y <- 1 to 4)
      addTile(TallGrass(), CoordinateImpl(x,y))

  for (x <- 1 to 3)
    for (y <- 5 to 10)
      addTile(TallGrass(), CoordinateImpl(x,y))

  for (x <- 1 to 9)
    for (y <- 19 to 23)
      addTile(TallGrass(), CoordinateImpl(x,y))

  for (x <- 39 to 48)
    for (y <- 1 to 10)
      addTile(TallGrass(), CoordinateImpl(x,y))

  for (y <- 1 to 8)
    addTile(TallGrass(), CoordinateImpl(38,y))

  for (x <- 36 to 37)
    for (y <- 1 to 5)
      addTile(TallGrass(), CoordinateImpl(x,y))


  for (x <- 1 to 14)
      addTile(Tree(), CoordinateImpl(x,18))

  for (x <- 43 to 49)
    for (y <- 1 to 4)
      addTile(Tree(), CoordinateImpl(x,y))
}