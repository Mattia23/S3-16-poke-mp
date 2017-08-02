package model.entities

sealed trait Sprite {
  def image: String
}

object Sprite {
  case class Back1(override val image: String) extends Sprite

  case class Back2(override val image: String) extends Sprite

  case class BackS(override val image: String) extends Sprite

  case class Front1(override val image: String) extends Sprite

  case class Front2(override val image: String) extends Sprite

  case class FrontS(override val image: String) extends Sprite

  case class Left1(override val image: String) extends Sprite

  case class Left2(override val image: String) extends Sprite

  case class LeftS(override val image: String) extends Sprite

  case class Right1(override val image: String) extends Sprite

  case class Right2(override val image: String) extends Sprite

  case class RightS(override val image: String) extends Sprite
}
