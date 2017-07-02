package model.entities

import utilities.Settings

sealed trait TrainerSprites {
  def back1: String
  def back2: String
  def backS: String
  def front1: String
  def front2: String
  def frontS: String
  def left1: String
  def left2: String
  def leftS: String
  def right1: String
  def right2: String
  def rightS: String
}

case class trainer1() extends TrainerSprites {
  override def back1: String = Settings.TRAINER_1_BACK_1_IMAGE_STRING

  override def back2: String = Settings.TRAINER_1_BACK_2_IMAGE_STRING

  override def backS: String = Settings.TRAINER_1_BACK_S_IMAGE_STRING

  override def front1: String = Settings.TRAINER_1_FRONT_1_IMAGE_STRING

  override def front2: String = Settings.TRAINER_1_FRONT_2_IMAGE_STRING

  override def frontS: String = Settings.TRAINER_1_FRONT_S_IMAGE_STRING

  override def left1: String = Settings.TRAINER_1_LEFT_1_IMAGE_STRING

  override def left2: String = Settings.TRAINER_1_LEFT_2_IMAGE_STRING

  override def leftS: String = Settings.TRAINER_1_LEFT_S_IMAGE_STRING

  override def right1: String = Settings.TRAINER_1_RIGHT_1_IMAGE_STRING

  override def right2: String = Settings.TRAINER_1_RIGHT_2_IMAGE_STRING

  override def rightS: String = Settings.TRAINER_1_RIGHT_S_IMAGE_STRING
}

case class trainer2() extends TrainerSprites {
  override def back1: String = Settings.TRAINER_2_BACK_1_IMAGE_STRING

  override def back2: String = Settings.TRAINER_2_BACK_2_IMAGE_STRING

  override def backS: String = Settings.TRAINER_2_BACK_S_IMAGE_STRING

  override def front1: String = Settings.TRAINER_2_FRONT_1_IMAGE_STRING

  override def front2: String = Settings.TRAINER_2_FRONT_2_IMAGE_STRING

  override def frontS: String = Settings.TRAINER_2_FRONT_S_IMAGE_STRING

  override def left1: String = Settings.TRAINER_2_LEFT_1_IMAGE_STRING

  override def left2: String = Settings.TRAINER_2_LEFT_2_IMAGE_STRING

  override def leftS: String = Settings.TRAINER_2_LEFT_S_IMAGE_STRING

  override def right1: String = Settings.TRAINER_2_RIGHT_1_IMAGE_STRING

  override def right2: String = Settings.TRAINER_2_RIGHT_2_IMAGE_STRING

  override def rightS: String = Settings.TRAINER_2_RIGHT_S_IMAGE_STRING
}

case class trainer3() extends TrainerSprites {
  override def back1: String = Settings.TRAINER_3_BACK_1_IMAGE_STRING

  override def back2: String = Settings.TRAINER_3_BACK_2_IMAGE_STRING

  override def backS: String = Settings.TRAINER_3_BACK_S_IMAGE_STRING

  override def front1: String = Settings.TRAINER_3_FRONT_1_IMAGE_STRING

  override def front2: String = Settings.TRAINER_3_FRONT_2_IMAGE_STRING

  override def frontS: String = Settings.TRAINER_3_FRONT_S_IMAGE_STRING

  override def left1: String = Settings.TRAINER_3_LEFT_1_IMAGE_STRING

  override def left2: String = Settings.TRAINER_3_LEFT_2_IMAGE_STRING

  override def leftS: String = Settings.TRAINER_3_LEFT_S_IMAGE_STRING

  override def right1: String = Settings.TRAINER_3_RIGHT_1_IMAGE_STRING

  override def right2: String = Settings.TRAINER_3_RIGHT_2_IMAGE_STRING

  override def rightS: String = Settings.TRAINER_3_RIGHT_S_IMAGE_STRING
}

case class trainer4() extends TrainerSprites {
  override def back1: String = Settings.TRAINER_4_BACK_1_IMAGE_STRING

  override def back2: String = Settings.TRAINER_4_BACK_2_IMAGE_STRING

  override def backS: String = Settings.TRAINER_4_BACK_S_IMAGE_STRING

  override def front1: String = Settings.TRAINER_4_FRONT_1_IMAGE_STRING

  override def front2: String = Settings.TRAINER_4_FRONT_2_IMAGE_STRING

  override def frontS: String = Settings.TRAINER_4_FRONT_S_IMAGE_STRING

  override def left1: String = Settings.TRAINER_4_LEFT_1_IMAGE_STRING

  override def left2: String = Settings.TRAINER_4_LEFT_2_IMAGE_STRING

  override def leftS: String = Settings.TRAINER_4_LEFT_S_IMAGE_STRING

  override def right1: String = Settings.TRAINER_4_RIGHT_1_IMAGE_STRING

  override def right2: String = Settings.TRAINER_4_RIGHT_2_IMAGE_STRING

  override def rightS: String = Settings.TRAINER_4_RIGHT_S_IMAGE_STRING
}