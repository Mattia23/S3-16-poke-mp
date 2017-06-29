package model.entities

case class Pokemon(
                    id: Int,
                    name: String,
                    attacks: (Int,Int,Int,Int),
                    level: Int,
                    experiencePoints: Int,
                    levelExperience: Int,
                    imageName: String
                  )

