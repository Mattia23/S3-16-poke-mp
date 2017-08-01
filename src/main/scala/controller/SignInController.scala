package controller

import java.util
import javax.swing.{JOptionPane, JTextField}

import database.remote.DBConnect
import utilities.Settings
import view.{AccountData, View}

trait SignInController{
  def signIn(accountData: util.Map[String, JTextField], idImage: Int): Unit

  def back(): Unit
}

object SignInController{
  def apply(initialMenuController: InitialMenuController, view: View): SignInControllerImpl = new SignInControllerImpl(initialMenuController, view)
}

object SignInControllerImpl{
  private final val NAME_MIN_LENGTH = 3
  private final val SURNAME_MIN_LENGTH = 3
  private final val USERNAME_MIN_LENGTH = 4
  private final val PASSWORD_MIN_LENGTH = 8
  private final val WRONG_SING_IN = "WRONG SING IN"
  private final val SIGN_IN_SUCCEEDED = "SIGN IN SUCCEEDED"
  private final val SIGN_IN_FAILED = "SIGN IN FAILED"
}

class SignInControllerImpl(private val initialMenuController: InitialMenuController,
                           private val view: View) extends SignInController{

  import SignInControllerImpl._
  view showSignIn this

  override def signIn(accountData: util.Map[String, JTextField], idImage: Int): Unit = {
    new Thread(() => {
      val name = accountData.get(AccountData.Name.toString).getText()
      val surname = accountData.get(AccountData.Surname.toString).getText()
      val email = accountData.get(AccountData.Email.toString).getText()
      val username = accountData.get(AccountData.Username.toString).getText()
      val password = accountData.get(AccountData.Password.toString).getText()

      import Settings._
      (name, surname, email, username, password) match {
        case _ if name.length < NAME_MIN_LENGTH => view.showMessage(Strings.SIGN_IN_NAME_ERROR, WRONG_SING_IN, JOptionPane.ERROR_MESSAGE)
        case _ if surname.length < SURNAME_MIN_LENGTH => view.showMessage(Strings.SIGN_IN_SURNAME_ERROR, WRONG_SING_IN, JOptionPane.ERROR_MESSAGE)
        case _ if !(email contains String.valueOf('@')) | !(email contains String.valueOf('.')) => view.showMessage(Strings.SIGN_IN_EMAIL_ERROR, WRONG_SING_IN, JOptionPane.ERROR_MESSAGE)
        case _ if username.length < USERNAME_MIN_LENGTH => view.showMessage(Strings.SIGN_IN_USERNAME_ERROR, WRONG_SING_IN, JOptionPane.ERROR_MESSAGE)
        case _ if password.length < PASSWORD_MIN_LENGTH => view.showMessage(Strings.SIGN_IN_PASSWORD_ERROR, WRONG_SING_IN, JOptionPane.ERROR_MESSAGE)
        case _ =>


          if(DBConnect.insertCredentials(accountData, idImage)) {
            view.showMessage(Strings.CORRECT_SIGN_IN,SIGN_IN_SUCCEEDED,JOptionPane.INFORMATION_MESSAGE)
            view showInitialMenu initialMenuController
          } else {
            view.showMessage(Strings.SIGN_IN_FAILED,SIGN_IN_FAILED, JOptionPane.ERROR_MESSAGE)
          }
      }
    }).start()
  }

  override def back(): Unit = initialMenuController.show()
}
