package controller

import com.rabbitmq.client.Connection
import model.entities.{OakAfterChoise, Trainer}
import model.environment.Direction.Direction
import model.environment._
import model.map._
import utilities.Settings
import view._

import scala.collection.JavaConverters._

abstract class BuildingController(private val view: View, private val mapController: GameController, private val _trainer: Trainer) extends GameControllerImpl(view, _trainer) {

  protected var buildingMap: BuildingMap

  this.setTrainerSpriteBack()

  override protected def doMove(direction: Direction): Unit = {
    if (!isInPause) {
      nextPosition = nextTrainerPosition(direction)
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
            if(buildingMap.isInstanceOf[LaboratoryMap] && trainer.capturedPokemons.isEmpty){
              this.pause()
              view.showDialogue(new ClassicDialoguePanel(this, buildingMap.npc.dialogue.asJava))
            }else {
              this.terminate()
              mapController.resume()
            }
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

  override protected def doPause(): Unit = {
    setFocusableOff()
    this.audio.stop()
  }

  override protected def doResume(): Unit = {
    setFocusableOn()
    this.audio.loop()
  }

  override protected def doLogout(): Unit = {
    terminate()
    mapController.terminate()
  }

  override def createDistributedBattle(otherPlayerId: Int, yourPlayerIsFirst: Boolean): Unit = {}

  override def hideCurrentDialogue(): Unit = {}

  override def sendPlayerIsFighting(isFighting: Boolean): Unit = {}

}


class PokemonCenterController(private val view: View, private val mapController: GameControllerImpl, private val _trainer: Trainer) extends BuildingController(view, mapController, _trainer){
  override protected var buildingMap: BuildingMap = new PokemonCenterMap
  this.trainer.coordinate = CoordinateImpl(buildingMap.entryCoordinate.x, buildingMap.entryCoordinate.y)

  audio = Audio(Settings.POKEMONCENTER_SONG)

  override protected def doStart(): Unit = {
    super.doStart()
    initView()
  }

  override protected def doResume(): Unit = {
    super.doResume()
    initView()
  }

  private def initView(): Unit = {
    view.showPokemonCenter(this, buildingMap)
    gamePanel = view.getGamePanel
  }

  override protected def doInteract(direction: Direction): Unit = {
    if (!isInPause) {
      if(direction != null) nextPosition = nextTrainerPosition(direction)
      try{
        val tile = buildingMap.map(nextPosition.x)(nextPosition.y)
        if(nextPosition equals buildingMap.npc.coordinate){
          //this.pause()
          showDialogue(new DoctorDialoguePanel(this, buildingMap.npc.dialogue.asJava))
        }
        if(tile.isInstanceOf[Box]){
          this.pause()
          view showBoxPanel this
        }
      }catch{
        case e: ArrayIndexOutOfBoundsException => e.printStackTrace()
      }
    }
  }
}

class LaboratoryController(private val view: View, private val mapController: GameControllerImpl, private val _trainer: Trainer) extends BuildingController(view, mapController, _trainer){
  override protected var buildingMap: BuildingMap = new LaboratoryMap
  private var capturedPokemonEmpty: Boolean = this.trainer.capturedPokemons.isEmpty
  if(!capturedPokemonEmpty) buildingMap.npc = new OakAfterChoise
  this.trainer.coordinate = CoordinateImpl(buildingMap.entryCoordinate.x, buildingMap.entryCoordinate.y)

  audio = Audio(Settings.LABORATORY_SONG)

  override protected def doStart(): Unit = {
    super.doStart()
    initView()
  }

  override protected def doResume(): Unit = {
    super.doResume()
    capturedPokemonEmpty = this.trainer.capturedPokemons.isEmpty
    initView()
  }

  private def initView(): Unit = {
    view.showLaboratory(this, buildingMap, this.trainer.capturedPokemons.isEmpty)
    gamePanel = view.getGamePanel
  }

  override protected def doInteract(direction: Direction): Unit = {
    if (!isInPause) {
      if(direction != null) nextPosition = nextTrainerPosition(direction)
      try{
        if(nextPosition equals buildingMap.npc.coordinate){
          //this.pause()
          showDialogue(new ClassicDialoguePanel(this, buildingMap.npc.dialogue.asJava))
        }
        if(capturedPokemonEmpty) {
          for (pokemon <- buildingMap.pokemonNpc) if (nextPosition equals pokemon.coordinate) {
            this.pause()
            this.view.showInitialPokemonPanel(this, pokemon.pokemonWithLife)
          }
        }
      }catch{
        case e: ArrayIndexOutOfBoundsException => e.printStackTrace()
      }
    }
  }
}

