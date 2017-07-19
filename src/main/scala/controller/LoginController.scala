package controller

import java.util.Optional
import java.util.concurrent.ConcurrentHashMap
import javax.swing.JOptionPane

import database.remote.DBConnect
import distributed.User
import distributed.client.PlayerConnectionClientManagerImpl
import model.entities.{Trainer, TrainerSprites}
import utilities.Settings
import view.View

trait LoginController{
  def login(username: String, password: String): Unit

  def back(): Unit
}

class LoginControllerImpl(private val initialMenuController: InitialMenuController, private val view: View) extends LoginController{

  view showLogin this

  override def login(username: String, password: String): Unit = {
    new Thread(() => {
      if(username == "" || password == "") {
        view.showMessage("Username and/or password must not be empty", "LOGIN FAILED", JOptionPane.ERROR_MESSAGE)
      }else{
        if (!DBConnect.checkCredentials(username, password)) {
           view.showMessage("Wrong username or password", "LOGIN FAILED", JOptionPane.ERROR_MESSAGE)
        } else {
          newGame(username)
        }
      }
    }).start()
  }

  private def newGame(username: String): Unit = {
    val optionalTrainer: Optional[Trainer] = DBConnect.getTrainerFromDB(username)
    if(optionalTrainer.isPresent) {
      val trainer = optionalTrainer.get()
      val connectedUsers = new ConcurrentHashMap[Int, User]()
      serverInteraction(username, trainer, connectedUsers)
      MapController(view, trainer, connectedUsers).start()
    } else {
      view.showMessage("There is no trainer for this user", "LOGIN FAILED", JOptionPane.ERROR_MESSAGE)
    }
  }

  private def serverInteraction(username: String, trainer: Trainer, connectedUsers: ConcurrentHashMap[Int, User]) = {
    val playerConnectionClientManager = PlayerConnectionClientManagerImpl()
    playerConnectionClientManager.sendUserInformation(trainer.id, username,
      TrainerSprites.getIdImageFromTrainerSprite(trainer.sprites), Settings.INITIAL_PLAYER_POSITION)
    playerConnectionClientManager.receivePlayersConnected(trainer.id, connectedUsers)
  }

  override def back(): Unit = initialMenuController.show()
}
