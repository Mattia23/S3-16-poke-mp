package controller

import java.util
import javax.swing.{JOptionPane, JTextField}

import database.remote.DBConnect
import view.{AccountData, View}

trait SignInController{
  def signIn(accountData: util.Map[String, JTextField], idImage: Int): Unit

  def back(): Unit
}

class SignInControllerImpl(private val initialMenuController: InitialMenuController, private val view: View) extends SignInController{

  private final val NAME_MIN_LENGTH = 3
  private final val SURNAME_MIN_LENGTH = 3
  private final val USERNAME_MIN_LENGTH = 4
  private final val PASSWORD_MIN_LENGTH = 8

  view showSignIn this

  override def signIn(accountData: util.Map[String, JTextField], idImage: Int): Unit = {
    new Thread(() => {
      val name = accountData.get(AccountData.Name.toString).getText()
      val surname = accountData.get(AccountData.Surname.toString).getText()
      val email = accountData.get(AccountData.Email.toString).getText()
      val username = accountData.get(AccountData.Username.toString).getText()
      val password = accountData.get(AccountData.Password.toString).getText()

      (name, surname, email, username, password) match {
        case _ if name.length < NAME_MIN_LENGTH => view.showMessage("Name must be at least 3 characters", "WRONG SINGIN", JOptionPane.ERROR_MESSAGE)
        case _ if surname.length < SURNAME_MIN_LENGTH => view.showMessage("Surname must be at least 3 characters", "WRONG SINGIN", JOptionPane.ERROR_MESSAGE)
        case _ if !email.contains(String.valueOf('@')) | !email.contains(String.valueOf('.')) => view.showMessage("Wrong e-mail", "WRONG SINGIN", JOptionPane.ERROR_MESSAGE)
        case _ if username.length < USERNAME_MIN_LENGTH => view.showMessage("Username must be at least 4 characters", "WRONG SINGIN", JOptionPane.ERROR_MESSAGE)
        case _ if password.length < PASSWORD_MIN_LENGTH => view.showMessage("Password must be at least 7 characters", "WRONG SINGIN", JOptionPane.ERROR_MESSAGE)
        case _ =>


          if(DBConnect.insertCredentials(accountData, idImage)) {
            view.showMessage("You have registered correctly","SIGNIN SUCCEEDED",JOptionPane.INFORMATION_MESSAGE)
            view showInitialMenu initialMenuController
          } else {
            view.showMessage("Username not available","SIGNIN FAILED", JOptionPane.ERROR_MESSAGE)
          }
      }
    }).start()
  }

  override def back(): Unit = initialMenuController.show()
}
