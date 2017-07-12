package controller

import java.util
import javax.swing.{JOptionPane, JTextField}

import database.remote.DBConnect
import view.{AccountData, View}

trait SignInController{
  def signIn(accountData: util.Map[String, JTextField]): Unit

  def back(): Unit
}

class SignInControllerImpl(private val initialMenuController: InitialMenuController, private val view: View) extends SignInController{

  view showSignIn this

  override def signIn(accountData: util.Map[String, JTextField]): Unit = {
    val name = accountData.get(AccountData.Name.toString).getText()
    val surname = accountData.get(AccountData.Surname.toString).getText()
    val email = accountData.get(AccountData.Email.toString).getText()
    val username = accountData.get(AccountData.Username.toString).getText()
    val password = accountData.get(AccountData.Password.toString).getText()

    (name, surname, email, username, password) match {
      case _ if name.length < 2 => view.showMessage("Name must be at least 3 characters", "WRONG SINGIN", JOptionPane.ERROR_MESSAGE)
      case _ if surname.length < 2 => view.showMessage("Surname must be at least 3 characters", "WRONG SINGIN", JOptionPane.ERROR_MESSAGE)
      case _ if !email.contains(String.valueOf('@'), String.valueOf('.')) => view.showMessage("Wrong e-mail", "WRONG SINGIN", JOptionPane.ERROR_MESSAGE)
      case _ if username.length < 3 => view.showMessage("Username must be at least 4 characters", "WRONG SINGIN", JOptionPane.ERROR_MESSAGE)
      case _ if password.length < 7 => view.showMessage("Password must be at least 7 characters", "WRONG SINGIN", JOptionPane.ERROR_MESSAGE)
      case _ =>


        if(DBConnect.insertCredentials(accountData, 1)) {
          view.showMessage("You have registered correctly","SIGNIN SUCCEEDED",JOptionPane.INFORMATION_MESSAGE)
          view showInitialMenu initialMenuController
        } else {
          view.showMessage("Username not available","SIGNIN FAILED", JOptionPane.ERROR_MESSAGE)
        }
    }
  }

  override def back(): Unit = initialMenuController.show()
}
