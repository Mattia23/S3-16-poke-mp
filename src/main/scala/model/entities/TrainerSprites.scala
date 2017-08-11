package model.entities

import utilities.Settings

import scala.collection.JavaConverters._

/**
  * TrainerSprites represents all the sprites of a trainer
  */
sealed trait TrainerSprites {
  /**
    * @return the back1 sprite as a Sprite
    */
  def back1: Sprite

  /**
    * @return the back2 sprite as a Sprite
    */
  def back2: Sprite

  /**
    * @return the backS sprite as a Sprite
    */
  def backS: Sprite

  /**
    * @return the front1 sprite as a Sprite
    */
  def front1: Sprite

  /**
    * @return the front2 sprite as a Sprite
    */
  def front2: Sprite

  /**
    * @return the frontS sprite as a Sprite
    */
  def frontS: Sprite

  /**
    * @return the left1 sprite as a Sprite
    */
  def left1: Sprite

  /**
    * @return the left2 sprite as a Sprite
    */
  def left2: Sprite

  /**
    * @return the leftS sprite as a Sprite
    */
  def leftS: Sprite

  /**
    * @return the right1 sprite as a Sprite
    */
  def right1: Sprite

  /**
    * @return the right2 sprite as a Sprite
    */
  def right2: Sprite

  /**
    * @return the rightS sprite as a Sprite
    */
  def rightS: Sprite
}

object TrainerSprites {
  object Trainers extends Enumeration {
    type Trainers = Value
    val Boy1, Boy2, Girl1, Girl2 = Value

    /**
      * @return the list of Trainers values as a java.util.List
      */
    def valueSetAsJavaList(): java.util.List[Trainers] = values.toList.asJava
  }

  import Sprite._

  def apply(idImage: Int): TrainerSprites = Trainers(idImage) match {
    case Trainers.Boy1 => Trainer1()
    case Trainers.Boy2 => Trainer2()
    case Trainers.Girl1 => Trainer3()
    case Trainers.Girl2 => Trainer4()
  }

  import Settings.Images._

  /**
    * @inheritdoc
    */
  case class Trainer1() extends TrainerSprites {
    override val back1 = Back1(TRAINER_1_BACK_1_IMAGE_STRING)

    override val back2 = Back2(TRAINER_1_BACK_2_IMAGE_STRING)

    override val backS = BackS(TRAINER_1_BACK_S_IMAGE_STRING)

    override val front1 = Front1(TRAINER_1_FRONT_1_IMAGE_STRING)

    override val front2 = Front2(TRAINER_1_FRONT_2_IMAGE_STRING)

    override val frontS = FrontS(TRAINER_1_FRONT_S_IMAGE_STRING)

    override val left1 = Left1(TRAINER_1_LEFT_1_IMAGE_STRING)

    override val left2 = Left2(TRAINER_1_LEFT_2_IMAGE_STRING)

    override val leftS = LeftS(TRAINER_1_LEFT_S_IMAGE_STRING)

    override val right1 = Right1(TRAINER_1_RIGHT_1_IMAGE_STRING)

    override val right2 = Right2(TRAINER_1_RIGHT_2_IMAGE_STRING)

    override val rightS = RightS(TRAINER_1_RIGHT_S_IMAGE_STRING)
  }

  /**
    * @inheritdoc
    */
  case class Trainer2() extends TrainerSprites {
    override val back1 = Back1(TRAINER_2_BACK_1_IMAGE_STRING)

    override val back2 = Back2(TRAINER_2_BACK_2_IMAGE_STRING)

    override val backS = BackS(TRAINER_2_BACK_S_IMAGE_STRING)

    override val front1 = Front1(TRAINER_2_FRONT_1_IMAGE_STRING)

    override val front2 = Front2(TRAINER_2_FRONT_2_IMAGE_STRING)

    override val frontS = FrontS(TRAINER_2_FRONT_S_IMAGE_STRING)

    override val left1 = Left1(TRAINER_2_LEFT_1_IMAGE_STRING)

    override val left2 = Left2(TRAINER_2_LEFT_2_IMAGE_STRING)

    override val leftS = LeftS(TRAINER_2_LEFT_S_IMAGE_STRING)

    override val right1 = Right1(TRAINER_2_RIGHT_1_IMAGE_STRING)

    override val right2 = Right2(TRAINER_2_RIGHT_2_IMAGE_STRING)

    override val rightS = RightS(TRAINER_2_RIGHT_S_IMAGE_STRING)
  }

  /**
    * @inheritdoc
    */
  case class Trainer3() extends TrainerSprites {
    override val back1 = Back1(TRAINER_3_BACK_1_IMAGE_STRING)

    override val back2 = Back2(TRAINER_3_BACK_2_IMAGE_STRING)

    override val backS = BackS(TRAINER_3_BACK_S_IMAGE_STRING)

    override val front1 = Front1(TRAINER_3_FRONT_1_IMAGE_STRING)

    override val front2 = Front2(TRAINER_3_FRONT_2_IMAGE_STRING)

    override val frontS = FrontS(TRAINER_3_FRONT_S_IMAGE_STRING)

    override val left1 = Left1(TRAINER_3_LEFT_1_IMAGE_STRING)

    override val left2 = Left2(TRAINER_3_LEFT_2_IMAGE_STRING)

    override val leftS = LeftS(TRAINER_3_LEFT_S_IMAGE_STRING)

    override val right1 = Right1(TRAINER_3_RIGHT_1_IMAGE_STRING)

    override val right2 = Right2(TRAINER_3_RIGHT_2_IMAGE_STRING)

    override val rightS = RightS(TRAINER_3_RIGHT_S_IMAGE_STRING)
  }

  /**
    * @inheritdoc
    */
  case class Trainer4() extends TrainerSprites {
    override val back1 = Back1(TRAINER_4_BACK_1_IMAGE_STRING)

    override val back2 = Back2(TRAINER_4_BACK_2_IMAGE_STRING)

    override val backS = BackS(TRAINER_4_BACK_S_IMAGE_STRING)

    override val front1 = Front1(TRAINER_4_FRONT_1_IMAGE_STRING)

    override val front2 = Front2(TRAINER_4_FRONT_2_IMAGE_STRING)

    override val frontS = FrontS(TRAINER_4_FRONT_S_IMAGE_STRING)

    override val left1 = Left1(TRAINER_4_LEFT_1_IMAGE_STRING)

    override val left2 = Left2(TRAINER_4_LEFT_2_IMAGE_STRING)

    override val leftS = LeftS(TRAINER_4_LEFT_S_IMAGE_STRING)

    override val right1 = Right1(TRAINER_4_RIGHT_1_IMAGE_STRING)

    override val right2 = Right2(TRAINER_4_RIGHT_2_IMAGE_STRING)

    override val rightS = RightS(TRAINER_4_RIGHT_S_IMAGE_STRING)
  }

  /**
    * Given a TrainerSprites return it's id as Int
    * @param trainerSprites the trainer's sprites of which I want the id
    * @return the id of the trainer's sprite as Int
    */
  def getIdImageFromTrainerSprite(trainerSprites: TrainerSprites): Int = trainerSprites match {
    case Trainer1() => Trainers.Boy1.id
    case Trainer2() => Trainers.Boy2.id
    case Trainer3() => Trainers.Girl1.id
    case Trainer4() => Trainers.Girl2.id
  }
}