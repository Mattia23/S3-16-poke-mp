package controller

import java.util.Optional
import javax.swing.JOptionPane

import database.remote.DBConnect
import model.entities.Trainer
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
      new MapController(view, optionalTrainer.get()).start()
    } else {
      view.showMessage("There is no trainer for this user", "LOGIN FAILED", JOptionPane.ERROR_MESSAGE)
    }
  }

  override def back(): Unit = initialMenuController.show()
}
