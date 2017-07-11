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
    if(accountData.get(AccountData.Name.toString).getText().length() > 2  &&
      accountData.get(AccountData.Surname.toString).getText().length() > 2 &&
      accountData.get(AccountData.Email.toString).getText().contains(String.valueOf('@'))  &&
      accountData.get(AccountData.Username.toString).getText().length() > 3 &&
      accountData.get(AccountData.Password.toString).getText().length() > 7) {

      if(DBConnect.insertCredentials(accountData,1)) {
        view.showMessage("You have registered correctly","SIGNIN SUCCEEDED",JOptionPane.INFORMATION_MESSAGE)
      } else {
        view.showMessage("Username not available","SIGNIN FAILED", JOptionPane.ERROR_MESSAGE)
      }
    } else {
      view.showMessage("Error in entering data", "WRONG SINGIN", JOptionPane.ERROR_MESSAGE)
    }
  }

  override def back(): Unit = initialMenuController.show()
}
