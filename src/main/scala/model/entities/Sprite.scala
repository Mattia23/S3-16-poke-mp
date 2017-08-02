package model.entities

/**
  * Sprite represents a single image of a trainer
  */
sealed trait Sprite {
  /**
    * @return The String path to the image of the trainer's sprite
    */
  def image: String
}

object Sprite {

  /**
    * Sprite that represents the first step seen from the trainer's back
    * @param image The String path to the image of the trainer's sprite
    */
  case class Back1(override val image: String) extends Sprite

  /**
    * Sprite that represents the second step seen from the trainer's back
    * @param image The String path to the image of the trainer's sprite
    */
  case class Back2(override val image: String) extends Sprite

  /**
    * Sprite that represents the trainer's back when is not moving
    * @param image The String path to the image of the trainer's sprite
    */
  case class BackS(override val image: String) extends Sprite

  /**
    * Sprite that represents the first step seen from the trainer's front
    * @param image The String path to the image of the trainer's sprite
    */
  case class Front1(override val image: String) extends Sprite

  /**
    * Sprite that represents the second step seen from the trainer's front
    * @param image The String path to the image of the trainer's sprite
    */
  case class Front2(override val image: String) extends Sprite

  /**
    * Sprite that represents the trainer's front when is not moving
    * @param image The String path to the image of the trainer's sprite
    */
  case class FrontS(override val image: String) extends Sprite

  /**
    * Sprite that represents the first step seen from the trainer's left side
    * @param image The String path to the image of the trainer's sprite
    */
  case class Left1(override val image: String) extends Sprite

  /**
    * Sprite that represents the second step seen from the trainer's left side
    * @param image The String path to the image of the trainer's sprite
    */
  case class Left2(override val image: String) extends Sprite

  /**
    * Sprite that represents the trainer's left side when is not moving
    * @param image The String path to the image of the trainer's sprite
    */
  case class LeftS(override val image: String) extends Sprite

  /**
    * Sprite that represents the first step seen from the trainer's right side
    * @param image The String path to the image of the trainer's sprite
    */
  case class Right1(override val image: String) extends Sprite

  /**
    * Sprite that represents the second step seen from the trainer's right side
    * @param image The String path to the image of the trainer's sprite
    */
  case class Right2(override val image: String) extends Sprite

  /**
    * Sprite that represents the trainer's right side when is not moving
    * @param image The String path to the image of the trainer's sprite
    */
  case class RightS(override val image: String) extends Sprite
}
