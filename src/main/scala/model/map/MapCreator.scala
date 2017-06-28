package model.map

object MapCreator {
  def create(height: Int, width: Int, elements: MapElements): GameMap = {
    val map:GameMap = GameMapImpl(height,width)
    elements.map foreach (element => map.addTile(element.coordinate, element.tile))
    map
  }
}
