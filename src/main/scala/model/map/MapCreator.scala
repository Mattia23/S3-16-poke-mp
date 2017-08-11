package model.map

/**
  * Creates a GameMap
  */
object MapCreator {

  /**
    * Returns a GameMap given width, height and map elements
    * @param height the height of the map
    * @param width the width of the map
    * @param elements the map elements that fill the map
    * @return a game map of dimension width x height, containing the given map elements
    */
  def create(height: Int, width: Int, elements: MapElements): GameMap = {
    val map: GameMap = GameMap(height,width)
    elements.map foreach (element => map.addTile(element._1, element._2))
    map
  }
}
