package controller

import javax.swing.JOptionPane

import database.remote.DBConnect
import utilities.Settings
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
        if(!DBConnect.checkCredentials(username, password)) {
          JOptionPane.showMessageDialog(loginPanel,"Wrong username or password","LOGIN FAILED",JOptionPane.ERROR_MESSAGE);
        }else{
          //initialMenuController.newGame();
        }
    }).start()
  }

  override def back(): Unit = initialMenuController.show()
}
