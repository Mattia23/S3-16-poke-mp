package controller

import model.entities.Pokemon
import model.environment.Direction.Direction
import model.environment._
import model.map.Box
import utilities.Settings
import view._
import scala.collection.JavaConverters._

abstract class BuildingController(private var view: View, private var mapController: GameControllerImpl) extends GameControllerImpl(view) {

  protected var buildingMap: BuildingMap

  this.setTrainerSpriteBack()

  override protected def doMove(direction: Direction): Unit = {
    if (!isInPause) {
      val nextPosition = nextTrainerPosition(direction)
      try {
        val tile = buildingMap.map(nextPosition.x)(nextPosition.y)
        tile match {
          case _ if tile.walkable =>
            walk(direction, nextPosition)
          case _ => trainerIsMoving = false
        }
      } catch {
        case _: ArrayIndexOutOfBoundsException =>
          trainerIsMoving = false
          if (trainer.coordinate equals buildingMap.entryCoordinate) {
            this.terminate()
            mapController.resume()
          }
        case _: NullPointerException => trainerIsMoving = false
      }
    }
  }

  override protected def doStart(): Unit = {
    audio.loop()
  }

  override protected def doTerminate(): Unit = {
    audio.stop()
  }

  override protected def doPause(): Unit = {}

  override protected def doResume(): Unit = {}

}

class PokemonCenterController(private var view: View, private var mapController: GameControllerImpl) extends BuildingController(view, mapController){
  override protected var buildingMap: BuildingMap = new PokemonCenterMap
  this.trainer.coordinate = CoordinateImpl(buildingMap.entryCoordinate.x, buildingMap.entryCoordinate.y)
  override var gamePanel: GamePanel = new PokemonCenterPanel(this, buildingMap)

  audio = Audio(Settings.POKEMONCENTER_SONG)

  override protected def doInteract(direction: Direction): Unit = {
    if (!isInPause) {
      val nextPosition = nextTrainerPosition(direction)
      try{
        val tile = buildingMap.map(nextPosition.x)(nextPosition.y)
        if(nextPosition equals buildingMap.npc.coordinate){
          this.view.showDialogue(new DoctorDialoguePanel(this, buildingMap.npc.dialogue.asJava))
        }
        if(tile.isInstanceOf[Box]){
          this.pause()
          view.showPanel(new BoxPanel(this))
        }
      }catch{
        case e: ArrayIndexOutOfBoundsException => e.printStackTrace()
      }
    }
  }
}

class LaboratoryController(private var view: View, private var mapController: GameControllerImpl) extends BuildingController(view, mapController){
  override protected var buildingMap: BuildingMap = new LaboratoryMap
  this.trainer.coordinate = CoordinateImpl(buildingMap.entryCoordinate.x, buildingMap.entryCoordinate.y)
  override var gamePanel: GamePanel = new LaboratoryPanel(this, buildingMap, true/*this.trainer.capturedPokemons.isEmpty*/)

  audio = Audio(Settings.LABORATORY_SONG)

  override protected def doInteract(direction: Direction): Unit = {
    if (!isInPause) {
      val nextPosition = nextTrainerPosition(direction)
      try{
        val tile = buildingMap.map(nextPosition.x)(nextPosition.y)
        if(nextPosition equals buildingMap.npc.coordinate){
          this.view.showDialogue(new ClassicDialoguePanel(this, buildingMap.npc.dialogue.asJava))
        }
        if(true/*trainer.capturedPokemons.isEmpty*/) {
          for (pokemon <- buildingMap.pokemonNpc) if (nextPosition equals pokemon.coordinate) {
            this.pause()
            view.showPanel(new InitialPokemonPanel(this, Pokemon(1,"ciao",(1,2,3,4),5,0,0,null)/*pokemon.pokemon*/))
          }
        }
      }catch{
        case e: ArrayIndexOutOfBoundsException => e.printStackTrace()
      }
    }
  }
}

