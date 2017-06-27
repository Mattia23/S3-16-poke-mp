package model.entities

trait Entity {

  def name: String

  def level: Int

  def name_=(name: String): Unit

}
