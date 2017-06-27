package model.game

trait Battle {
  def startBattle(idTrainer: Int): Unit

  def getTrainerExperiencePoint: Int
}
