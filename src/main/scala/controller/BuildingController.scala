package controller

import javax.swing.SwingUtilities

import model.environment.Direction.Direction
import model.environment._
import model.map.Box
import utilities.Settings
import view._

abstract class BuildingController(private var view: View, private var mapController: MapController) extends GameController(view){

  private var agent: GameControllerAgent = _
  protected var buildingMap: BuildingMap
  protected var audio: Audio = _

  this.setTrainerSpriteBack()

  override protected def doMove(direction: Direction): Unit = {
    if (!isInPause) {
      val nextPosition = nextTrainerPosition(direction)
      try{
        val tile = buildingMap.map(nextPosition.x)(nextPosition.y)
        tile match {
          case _ if tile.walkable =>
            walk(direction, nextPosition)
          case tile:Box =>
            trainerIsMoving = false
            this.pauseGame()
            view.showPanel(new BoxPanel(this))
          case _ => if(nextPosition equals buildingMap.npc.coordinate){
            trainerIsMoving = false
            println("Dialogo")
          }else if(1==1/*trainer.capturedPokemons.isEmpty*/) {
            trainerIsMoving = false
            for (pokemon <- buildingMap.pokemonNpc) if (nextPosition equals pokemon.coordinate) {
              view showPanel new PokemonPanel
            }
          }else {
            trainerIsMoving = false
          }
        }
      }catch{
        case e: ArrayIndexOutOfBoundsException =>
          trainerIsMoving = false
          if(trainerPosition equals buildingMap.entryCoordinate){
            this.terminateGame()
            mapController.resumeGame()
          }
        case e2: NullPointerException => trainerIsMoving = false
      }
    }
  }

  override protected def doStart(): Unit = {
    agent = new GameControllerAgent
    audio.loop()
    try {
      agent.start()
    } catch {
      case e: IllegalStateException => view.showError(e.toString, "Not initialized")
    }
  }

  override protected def doTerminate(): Unit = {
    audio.stop()
    agent.terminate()
  }

  override protected def doPause(): Unit = {
    agent.terminate()
  }

  override protected def doResume(): Unit = {
    agent = new GameControllerAgent
    agent.start()
  }




  private class GameControllerAgent extends Thread {
    var stopped: Boolean = false

    override def run(): Unit = {
      while(isInGame && !stopped){
        if(!isInPause){
          try
            SwingUtilities.invokeAndWait(() => gamePanel.repaint())
          catch {
            case e: Exception => System.out.println(e)
          }
        }

        try
          Thread.sleep(Settings.GAME_REFRESH_TIME)
        catch {
          case e: InterruptedException => System.out.println(e)
        }
      }
    }

    def terminate(): Unit = {
      stopped = true
    }

  }
}

class PokemonCenterController(private var view: View, private var controller: MapController) extends BuildingController(view, controller){
  override protected var buildingMap: BuildingMap = new PokemonCenterMap
  this.trainerPosition = CoordinateImpl(buildingMap.entryCoordinate.x, buildingMap.entryCoordinate.y)
  override var gamePanel: GamePanel = new BuildingPanel(this, buildingMap)

  this.audio = Audio(Settings.POKEMONCENTER_SONG)

}

class LaboratoryController(private var view: View, private var controller: MapController) extends BuildingController(view, controller){
  override protected var buildingMap: BuildingMap = new LaboratoryMap
  this.trainerPosition = CoordinateImpl(buildingMap.entryCoordinate.x, buildingMap.entryCoordinate.y)
  override var gamePanel: GamePanel = new BuildingPanel(this, buildingMap)

  this.audio = Audio(Settings.LABORATORY_SONG)

}

