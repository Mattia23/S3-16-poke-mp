package controller

import java.util.Optional
import javax.swing.JOptionPane

import com.rabbitmq.client.Connection
import database.remote.DBConnect
import distributed.client.PlayerLoginClientManager
import distributed.{ConnectedPlayers, DistributedConnectionImpl, PlayerImpl}
import model.entities.{Trainer, TrainerSprites}
import utilities.Settings
import view.View

trait LoginController{
  def login(username: String, password: String): Unit

  def back(): Unit
}

object LoginControllerImpl{
  private final val LOGIN_FAILED: String = "LOGIN FAILED"
}

class LoginControllerImpl(private val initialMenuController: InitialMenuController, private val view: View) extends LoginController{

  import LoginControllerImpl._
  view showLogin this

  override def login(username: String, password: String): Unit = {
    new Thread(() => {
      if(username == "" || password == "") {
        view.showMessage(Settings.LOGIN_ERROR_USERNAME_PASSWORD_EMPTY, LOGIN_FAILED, JOptionPane.ERROR_MESSAGE)
      }else{
        if (!DBConnect.checkCredentials(username, password)) {
           view.showMessage(Settings.LOGIN_ERROR_WRONG_USERNAME_PASSWORD, LOGIN_FAILED, JOptionPane.ERROR_MESSAGE)
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
      val connection = DistributedConnectionImpl().connection
      val connectedPlayers = ConnectedPlayers()
      val mapController = MapController(view, trainer, connection, connectedPlayers)

      serverInteraction(connection, username, trainer, connectedPlayers)
      mapController.start()
      initialMenuController.stopMainMusic()

    } else {
      view.showMessage(Settings.LOGIN_NO_TRAINER_ERROR, LOGIN_FAILED, JOptionPane.ERROR_MESSAGE)
    }
  }

  private def serverInteraction(connection: Connection, username: String, trainer: Trainer, connectedPlayers: ConnectedPlayers) = {
    val playerConnectionClientManager = PlayerLoginClientManager(connection)

    val player = PlayerImpl(trainer.id, username, TrainerSprites.getIdImageFromTrainerSprite(trainer.sprites))
    if (trainer.capturedPokemonId.isEmpty) player.isVisible = false

    playerConnectionClientManager.sendPlayer(player)
    playerConnectionClientManager.receivePlayersConnected(trainer.id, connectedPlayers)
  }

  override def back(): Unit = initialMenuController.show()
}
