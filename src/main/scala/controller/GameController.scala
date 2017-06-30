package controller

import javax.swing.SwingUtilities

import model.environment.Direction
import model.environment.Direction.Direction
import model.game.Model
import view.View

trait GameViewObserver {
  def startGame: Unit

  def pauseButton: Unit

  def resumeGame: Unit

  def moveTrainer(direction: Direction.Direction): Unit
  //def speakTrainer: Unit
  //def ...
}

class GameController(private var model: Model, private var view: View) extends GameViewObserver{
  private var agent: GameControllerAgent = _

  override def startGame: Unit = {
    this.agent = new GameControllerAgent

    try {
      this.model.startGame
      this.view.showGame
      this.view.drawModel(this.model)
      this.agent.start
    } catch {
      case e: IllegalStateException =>
        this.view.showError(e.toString, "Not initialized")
    }
  }

  override def pauseButton: Unit = {
    this.agent.terminate
    this.model.pause
    this.view.showPause
  }

  override def resumeGame: Unit = {
    this.view.showGame
    this.agent = new GameControllerAgent
    this.model.resumeGame
    this.agent.start
  }

  override def moveTrainer(direction: Direction): Unit = {
    if (!this.model.isInPause) {
      this.model.moveTrainer(direction)
    }
  }

  private class GameControllerAgent extends Thread {
    var stopped: Boolean = false

    override def run: Unit = {
      while(model.isInGame && !stopped){
        if(!model.isInPause){
          try
            SwingUtilities.invokeAndWait(() => view.drawModel(model))
          catch {
            case e: Exception =>
              System.out.println(e)
          }
        }

        try
          Thread.sleep(10)
        catch {
          case e: InterruptedException =>
            System.out.println(e)
        }
      }
    }

    def terminate: Unit = {
      stopped = true
    }

  }

}
