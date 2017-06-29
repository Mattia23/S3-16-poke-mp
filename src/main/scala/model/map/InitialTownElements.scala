package model.map

import model.environment.CoordinateImpl
import utilities.Settings

case class InitialTownElements() extends MapElementsImpl {
  for (x <- 0 until Settings.MAP_WIDTH)
    for (y <- 0 until Settings.MAP_HEIGHT)
      if (x == 0 || x == Settings.MAP_WIDTH - 1 || y == 0 || y == Settings.MAP_HEIGHT - 1) addTile(Tree(), CoordinateImpl(x,y))

  val coordinatePokemonCenter = CoordinateImpl(15,30)
  val coordinateLaboratory = CoordinateImpl(15,15)

  addTile(PokemonCenter(coordinatePokemonCenter), coordinatePokemonCenter)
  addTile(Laboratory(coordinateLaboratory), coordinateLaboratory)

  for (x <- 30 until 40)
    for (y <- 30 until 40)
      addTile(Water(), CoordinateImpl(x,y))

  for (x <- 20 until 25)
    for (y <- 20 until 25)
      addTile(TallGrass(), CoordinateImpl(x,y))
}