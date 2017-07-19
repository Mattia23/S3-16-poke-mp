package distributed.client

import distributed.CommunicationManager

object NewPlayerInGameClientManager {
  def apply(): CommunicationManager = new NewPlayerInGameClientManager()
}

class NewPlayerInGameClientManager extends CommunicationManager{

  override def start(): Unit = ???
}