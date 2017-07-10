package controller

import javax.swing.JOptionPane

import database.remote.DBConnect
import view.{LoginPanel, View}

trait LoginController{
  def login(username: String, password: String): Unit

  def back(): Unit
}

class LoginControllerImpl(private val initialMenuController: InitialMenuController, private val view: View) extends LoginController{

  private def loginPanel: LoginPanel = new LoginPanel(this)
  view showPanel loginPanel

  override def login(username: String, password: String): Unit = {
    new Thread(() => {
      if(username == "" || password == "") {
        JOptionPane.showMessageDialog(loginPanel, "Username and/or password must not be empty", "LOGIN FAILED", JOptionPane.ERROR_MESSAGE);
      }else{
        if (!DBConnect.checkCredentials(username, password)) {
          JOptionPane.showMessageDialog(loginPanel, "Wrong username or password", "LOGIN FAILED", JOptionPane.ERROR_MESSAGE);
        } else {
          newGame()
        }
      }
    }).start()
  }

  private def newGame(): Unit = {
    new MapController(view).startGame()
  }

  override def back(): Unit = initialMenuController.show()
}
