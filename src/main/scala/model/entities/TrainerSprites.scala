package model.entities

import utilities.Settings

import scala.collection.JavaConverters._

sealed trait TrainerSprites {
  def back1: Sprite
  def back2: Sprite
  def backS: Sprite
  def front1: Sprite
  def front2: Sprite
  def frontS: Sprite
  def left1: Sprite
  def left2: Sprite
  def leftS: Sprite
  def right1: Sprite
  def right2: Sprite
  def rightS: Sprite
}

object Trainers extends Enumeration {
  type Trainers = Value
  val Boy1, Boy2, Girl1, Girl2 = Value

  def valueSetAsJavaList(): java.util.List[Trainers] = {
    values.toList.asJava
  }
}

object TrainerSprites {
  def selectTrainerSprite(idImage: Int): TrainerSprites = Trainers(idImage) match {
    case Trainers.Boy1 => Trainer1()
    case Trainers.Boy2 => Trainer2()
    case Trainers.Girl1 => Trainer3()
    case Trainers.Girl2 => Trainer4()
  }

  def getIdImageFromTrainerSprite(trainerSprites: TrainerSprites): Int = trainerSprites match {
    case Trainer1() => Trainers.Boy1.id
    case Trainer2() => Trainers.Boy2.id
    case Trainer3() => Trainers.Girl1.id
    case Trainer4() => Trainers.Girl2.id
  }
}

case class Trainer1() extends TrainerSprites {
  override val back1 = Back1(Settings.TRAINER_1_BACK_1_IMAGE_STRING)

  override val back2 = Back2(Settings.TRAINER_1_BACK_2_IMAGE_STRING)

  override val backS = BackS(Settings.TRAINER_1_BACK_S_IMAGE_STRING)

  override val front1 = Front1(Settings.TRAINER_1_FRONT_1_IMAGE_STRING)

  override val front2 = Front2(Settings.TRAINER_1_FRONT_2_IMAGE_STRING)

  override val frontS = FrontS(Settings.TRAINER_1_FRONT_S_IMAGE_STRING)

  override val left1 = Left1(Settings.TRAINER_1_LEFT_1_IMAGE_STRING)

  override val left2 = Left2(Settings.TRAINER_1_LEFT_2_IMAGE_STRING)

  override val leftS = LeftS(Settings.TRAINER_1_LEFT_S_IMAGE_STRING)

  override val right1 = Right1(Settings.TRAINER_1_RIGHT_1_IMAGE_STRING)

  override val right2 = Right2(Settings.TRAINER_1_RIGHT_2_IMAGE_STRING)

  override val rightS = RightS(Settings.TRAINER_1_RIGHT_S_IMAGE_STRING)
}

case class Trainer2() extends TrainerSprites {
  override val back1 = Back1(Settings.TRAINER_2_BACK_1_IMAGE_STRING)

  override val back2 = Back2(Settings.TRAINER_2_BACK_2_IMAGE_STRING)

  override val backS = BackS(Settings.TRAINER_2_BACK_S_IMAGE_STRING)

  override val front1 = Front1(Settings.TRAINER_2_FRONT_1_IMAGE_STRING)

  override val front2 = Front2(Settings.TRAINER_2_FRONT_2_IMAGE_STRING)

  override val frontS = FrontS(Settings.TRAINER_2_FRONT_S_IMAGE_STRING)

  override val left1 = Left1(Settings.TRAINER_2_LEFT_1_IMAGE_STRING)

  override val left2 = Left2(Settings.TRAINER_2_LEFT_2_IMAGE_STRING)

  override val leftS = LeftS(Settings.TRAINER_2_LEFT_S_IMAGE_STRING)

  override val right1 = Right1(Settings.TRAINER_2_RIGHT_1_IMAGE_STRING)

  override val right2 = Right2(Settings.TRAINER_2_RIGHT_2_IMAGE_STRING)

  override val rightS = RightS(Settings.TRAINER_2_RIGHT_S_IMAGE_STRING)
}

case class Trainer3() extends TrainerSprites {
  override val back1 = Back1(Settings.TRAINER_3_BACK_1_IMAGE_STRING)

  override val back2 = Back2(Settings.TRAINER_3_BACK_2_IMAGE_STRING)

  override val backS = BackS(Settings.TRAINER_3_BACK_S_IMAGE_STRING)

  override val front1 = Front1(Settings.TRAINER_3_FRONT_1_IMAGE_STRING)

  override val front2 = Front2(Settings.TRAINER_3_FRONT_2_IMAGE_STRING)

  override val frontS = FrontS(Settings.TRAINER_3_FRONT_S_IMAGE_STRING)

  override val left1 = Left1(Settings.TRAINER_3_LEFT_1_IMAGE_STRING)

  override val left2 = Left2(Settings.TRAINER_3_LEFT_2_IMAGE_STRING)

  override val leftS = LeftS(Settings.TRAINER_3_LEFT_S_IMAGE_STRING)

  override val right1 = Right1(Settings.TRAINER_3_RIGHT_1_IMAGE_STRING)

  override val right2 = Right2(Settings.TRAINER_3_RIGHT_2_IMAGE_STRING)

  override val rightS = RightS(Settings.TRAINER_3_RIGHT_S_IMAGE_STRING)
}

case class Trainer4() extends TrainerSprites {
  override def back1 = Back1(Settings.TRAINER_4_BACK_1_IMAGE_STRING)

  override def back2 = Back2(Settings.TRAINER_4_BACK_2_IMAGE_STRING)

  override def backS = BackS(Settings.TRAINER_4_BACK_S_IMAGE_STRING)

  override def front1 = Front1(Settings.TRAINER_4_FRONT_1_IMAGE_STRING)

  override def front2 = Front2(Settings.TRAINER_4_FRONT_2_IMAGE_STRING)

  override def frontS = FrontS(Settings.TRAINER_4_FRONT_S_IMAGE_STRING)

  override def left1 = Left1(Settings.TRAINER_4_LEFT_1_IMAGE_STRING)

  override def left2 = Left2(Settings.TRAINER_4_LEFT_2_IMAGE_STRING)

  override def leftS = LeftS(Settings.TRAINER_4_LEFT_S_IMAGE_STRING)

  override def right1 = Right1(Settings.TRAINER_4_RIGHT_1_IMAGE_STRING)

  override def right2 = Right2(Settings.TRAINER_4_RIGHT_2_IMAGE_STRING)

  override def rightS = RightS(Settings.TRAINER_4_RIGHT_S_IMAGE_STRING)
}