package utilities

import java.awt.{Dimension, Toolkit}
import java.util

import model.environment.{Coordinate, CoordinateImpl}

object Settings {

  val SCREEN_DIMENSION: Dimension = Toolkit.getDefaultToolkit.getScreenSize

  val GAME_REFRESH_TIME: Int = 100

  val SCREEN_WIDTH: Int = SCREEN_DIMENSION.width

  val SCREEN_HEIGHT: Int = SCREEN_DIMENSION.height

  val FRAME_SIDE: Int = SCREEN_WIDTH / 3

  val MAP_WIDTH: Int = 50

  val MAP_HEIGHT: Int = 50

  val TILE_HEIGHT: Int = 1

  val TILE_WIDTH: Int = 1

  val TILE_PIXEL: Int = 32

  val POKEMON_CENTER_HEIGHT = 5

  val POKEMON_CENTER_WIDTH = 5

  val POKEMON_CENTER_DOOR_X = 2

  val POKEMON_CENTER_DOOR_Y = 4

  val LABORATORY_HEIGHT = 4

  val LABORATORY_WIDTH = 7

  val LABORATORY_DOOR_X = 3

  val LABORATORY_DOOR_Y = 3

  val PANELS_FOLDER: String =  "/panels/"

  val IMAGES_FOLDER: String =  "/images/"

  val MAP_ELEMENTS_IMAGES_FOLDER: String = IMAGES_FOLDER + "mapElements/"

  val POKEMON_IMAGES: String = IMAGES_FOLDER + "pokemon/"

  val POKEBALL_IMAGES: String = IMAGES_FOLDER + "pokeball/"

  val GAME_MENU_IMAGES: String = IMAGES_FOLDER + "gameMenu/"

  val POKEMON_IMAGES_FRONT_FOLDER: String = POKEMON_IMAGES + "front/"

  val POKEMON_IMAGES_BACK_FOLDER: String = POKEMON_IMAGES + "back/"

  val POKEMON_IMAGES_ICON_FOLDER: String = POKEMON_IMAGES + "icon/"

  val TRAINER_IMAGES_FOLDER: String = IMAGES_FOLDER + "trainer/"

  val GRASS_IMAGE_STRING: String = MAP_ELEMENTS_IMAGES_FOLDER + "grass.png"

  val TALL_GRASS_IMAGE_STRING: String = MAP_ELEMENTS_IMAGES_FOLDER+ "tall-grass.png"

  val LABORATORY_IMAGE_STRING: String = MAP_ELEMENTS_IMAGES_FOLDER + "laboratory.png"

  val POKEMON_CENTER_IMAGE_STRING: String = MAP_ELEMENTS_IMAGES_FOLDER + "pokemon-center.png"

  val TREE_IMAGE_STRING: String = MAP_ELEMENTS_IMAGES_FOLDER + "tree.png"

  val WATER_IMAGE_STRING: String = MAP_ELEMENTS_IMAGES_FOLDER + "water.png"

  val WATER_MARGIN_TOP_LEFT_IMAGE_STRING: String = MAP_ELEMENTS_IMAGES_FOLDER + "water-margin-top-left.png"

  val WATER_MARGIN_TOP_RIGHT_IMAGE_STRING: String = MAP_ELEMENTS_IMAGES_FOLDER + "water-margin-top-right.png"

  val WATER_MARGIN_TOP_IMAGE_STRING: String = MAP_ELEMENTS_IMAGES_FOLDER + "water-margin-top.png"

  val WATER_MARGIN_BOTTOM_LEFT_IMAGE_STRING: String = MAP_ELEMENTS_IMAGES_FOLDER + "water-margin-bottom-left.png"

  val WATER_MARGIN_BOTTOM_RIGHT_IMAGE_STRING: String = MAP_ELEMENTS_IMAGES_FOLDER + "water-margin-bottom-right.png"

  val WATER_MARGIN_BOTTOM_IMAGE_STRING: String = MAP_ELEMENTS_IMAGES_FOLDER + "water-margin-bottom.png"

  val WATER_MARGIN_LEFT_IMAGE_STRING: String = MAP_ELEMENTS_IMAGES_FOLDER + "water-margin-left.png"

  val WATER_MARGIN_RIGHT_IMAGE_STRING: String = MAP_ELEMENTS_IMAGES_FOLDER + "water-margin-right.png"

  val ROAD_IMAGE_STRING: String = MAP_ELEMENTS_IMAGES_FOLDER + "road.png"

  val ROAD_MARGIN_TOP_LEFT_IMAGE_STRING: String = MAP_ELEMENTS_IMAGES_FOLDER + "road-margin-top-left.png"

  val ROAD_MARGIN_TOP_RIGHT_IMAGE_STRING: String = MAP_ELEMENTS_IMAGES_FOLDER + "road-margin-top-right.png"

  val ROAD_MARGIN_TOP_IMAGE_STRING: String = MAP_ELEMENTS_IMAGES_FOLDER + "road-margin-top.png"

  val ROAD_MARGIN_BOTTOM_LEFT_IMAGE_STRING: String = MAP_ELEMENTS_IMAGES_FOLDER + "road-margin-bottom-left.png"

  val ROAD_MARGIN_BOTTOM_RIGHT_IMAGE_STRING: String = MAP_ELEMENTS_IMAGES_FOLDER + "road-margin-bottom-right.png"

  val ROAD_MARGIN_BOTTOM_IMAGE_STRING: String = MAP_ELEMENTS_IMAGES_FOLDER + "road-margin-bottom.png"

  val ROAD_MARGIN_LEFT_IMAGE_STRING: String = MAP_ELEMENTS_IMAGES_FOLDER + "road-margin-left.png"

  val ROAD_MARGIN_RIGHT_IMAGE_STRING: String = MAP_ELEMENTS_IMAGES_FOLDER + "road-margin-right.png"

  val TRAINER_1_BACK_1_IMAGE_STRING: String = TRAINER_IMAGES_FOLDER + "1B1.png"

  val TRAINER_1_BACK_2_IMAGE_STRING: String = TRAINER_IMAGES_FOLDER + "1B2.png"

  val TRAINER_1_BACK_S_IMAGE_STRING: String = TRAINER_IMAGES_FOLDER + "1BS.png"

  val TRAINER_1_FRONT_1_IMAGE_STRING: String = TRAINER_IMAGES_FOLDER + "1F1.png"

  val TRAINER_1_FRONT_2_IMAGE_STRING: String = TRAINER_IMAGES_FOLDER + "1F2.png"

  val TRAINER_1_FRONT_S_IMAGE_STRING: String = TRAINER_IMAGES_FOLDER + "1FS.png"

  val TRAINER_1_LEFT_1_IMAGE_STRING: String = TRAINER_IMAGES_FOLDER + "1L1.png"

  val TRAINER_1_LEFT_2_IMAGE_STRING: String = TRAINER_IMAGES_FOLDER + "1L2.png"

  val TRAINER_1_LEFT_S_IMAGE_STRING: String = TRAINER_IMAGES_FOLDER + "1LS.png"

  val TRAINER_1_RIGHT_1_IMAGE_STRING: String = TRAINER_IMAGES_FOLDER + "1R1.png"

  val TRAINER_1_RIGHT_2_IMAGE_STRING: String = TRAINER_IMAGES_FOLDER + "1R2.png"

  val TRAINER_1_RIGHT_S_IMAGE_STRING: String = TRAINER_IMAGES_FOLDER + "1RS.png"

  val TRAINER_2_BACK_1_IMAGE_STRING: String = TRAINER_IMAGES_FOLDER + "2B1.png"

  val TRAINER_2_BACK_2_IMAGE_STRING: String = TRAINER_IMAGES_FOLDER + "2B2.png"

  val TRAINER_2_BACK_S_IMAGE_STRING: String = TRAINER_IMAGES_FOLDER + "2BS.png"

  val TRAINER_2_FRONT_1_IMAGE_STRING: String = TRAINER_IMAGES_FOLDER + "2F1.png"

  val TRAINER_2_FRONT_2_IMAGE_STRING: String = TRAINER_IMAGES_FOLDER + "2F2.png"

  val TRAINER_2_FRONT_S_IMAGE_STRING: String = TRAINER_IMAGES_FOLDER + "2FS.png"

  val TRAINER_2_LEFT_1_IMAGE_STRING: String = TRAINER_IMAGES_FOLDER + "2L1.png"

  val TRAINER_2_LEFT_2_IMAGE_STRING: String = TRAINER_IMAGES_FOLDER + "2L2.png"

  val TRAINER_2_LEFT_S_IMAGE_STRING: String = TRAINER_IMAGES_FOLDER + "2LS.png"

  val TRAINER_2_RIGHT_1_IMAGE_STRING: String = TRAINER_IMAGES_FOLDER + "2R1.png"

  val TRAINER_2_RIGHT_2_IMAGE_STRING: String = TRAINER_IMAGES_FOLDER + "2R2.png"

  val TRAINER_2_RIGHT_S_IMAGE_STRING: String = TRAINER_IMAGES_FOLDER + "2RS.png"

  val TRAINER_3_BACK_1_IMAGE_STRING: String = TRAINER_IMAGES_FOLDER + "3B1.png"

  val TRAINER_3_BACK_2_IMAGE_STRING: String = TRAINER_IMAGES_FOLDER + "3B2.png"

  val TRAINER_3_BACK_S_IMAGE_STRING: String = TRAINER_IMAGES_FOLDER + "3BS.png"

  val TRAINER_3_FRONT_1_IMAGE_STRING: String = TRAINER_IMAGES_FOLDER + "3F1.png"

  val TRAINER_3_FRONT_2_IMAGE_STRING: String = TRAINER_IMAGES_FOLDER + "3F2.png"

  val TRAINER_3_FRONT_S_IMAGE_STRING: String = TRAINER_IMAGES_FOLDER + "3FS.png"

  val TRAINER_3_LEFT_1_IMAGE_STRING: String = TRAINER_IMAGES_FOLDER + "3L1.png"

  val TRAINER_3_LEFT_2_IMAGE_STRING: String = TRAINER_IMAGES_FOLDER + "3L2.png"

  val TRAINER_3_LEFT_S_IMAGE_STRING: String = TRAINER_IMAGES_FOLDER + "3LS.png"

  val TRAINER_3_RIGHT_1_IMAGE_STRING: String = TRAINER_IMAGES_FOLDER + "3R1.png"

  val TRAINER_3_RIGHT_2_IMAGE_STRING: String = TRAINER_IMAGES_FOLDER + "3R2.png"

  val TRAINER_3_RIGHT_S_IMAGE_STRING: String = TRAINER_IMAGES_FOLDER + "3RS.png"

  val TRAINER_4_BACK_1_IMAGE_STRING: String = TRAINER_IMAGES_FOLDER + "4B1.png"

  val TRAINER_4_BACK_2_IMAGE_STRING: String = TRAINER_IMAGES_FOLDER + "4B2.png"

  val TRAINER_4_BACK_S_IMAGE_STRING: String = TRAINER_IMAGES_FOLDER + "4BS.png"

  val TRAINER_4_FRONT_1_IMAGE_STRING: String = TRAINER_IMAGES_FOLDER + "4F1.png"

  val TRAINER_4_FRONT_2_IMAGE_STRING: String = TRAINER_IMAGES_FOLDER + "4F2.png"

  val TRAINER_4_FRONT_S_IMAGE_STRING: String = TRAINER_IMAGES_FOLDER + "4FS.png"

  val TRAINER_4_LEFT_1_IMAGE_STRING: String = TRAINER_IMAGES_FOLDER + "4L1.png"

  val TRAINER_4_LEFT_2_IMAGE_STRING: String = TRAINER_IMAGES_FOLDER + "4L2.png"

  val TRAINER_4_LEFT_S_IMAGE_STRING: String = TRAINER_IMAGES_FOLDER + "4LS.png"

  val TRAINER_4_RIGHT_1_IMAGE_STRING: String = TRAINER_IMAGES_FOLDER + "4R1.png"

  val TRAINER_4_RIGHT_2_IMAGE_STRING: String = TRAINER_IMAGES_FOLDER + "4R2.png"

  val TRAINER_4_RIGHT_S_IMAGE_STRING: String = TRAINER_IMAGES_FOLDER + "4RS.png"

  val DATABASE_FOLDER: String =  "/database/"

  val INITIAL_TRAINER_LEVEL: Int = 1

  val LEVEL_STEP: Int = 50

  val MAP_IMAGES_FOLDER = "/images/maps/"

  val CHARACTER_IMAGES_FOLDER = "/images/characters/"

  val CLASSIC_DIALOGUE_BUTTON: util.List[String] = util.Arrays.asList("next")

  val DOCTOR_DIALOGUE_BUTTON: util.List[String] = util.Arrays.asList("yes", "no")

  val TRAINER_DIALOGUE_BUTTON: util.List[String] = util.Arrays.asList("fight", "bye")

  val AUDIO_FOLDER: String = "/audio/"

  val MAP_SONG2: String = AUDIO_FOLDER + "town_theme.wav"

  val MAP_SONG: String = AUDIO_FOLDER + "base-music.wav"

  val POKEMONCENTER_SONG: String = AUDIO_FOLDER + "pokemoncenter.wav"

  val LABORATORY_SONG: String = AUDIO_FOLDER + "laboratory.wav"

  val LOGIN_BUTTON: String =  "Login"

  val SIGN_IN_BUTTON: String =  "Sign in"

  val QUIT_BUTTON: String =  "Quit"

  val SUBMIT_BUTTON: String =  "Submit"

  val USERNAME: String =  "Username"

  val PASSWORD: String =  "Password"

  val BATTLE_EVENT_CHANGE_POKEMON: Int = 1

  val BATTLE_EVENT_CAPTURE_POKEMON: Int = 1

  val BATTLE_EVENT_ESCAPE: Int = 2

  val LOCAL_HOST_ADDRESS: String = "localhost"

  val REMOTE_HOST_ADDRESS: String = "ec2-13-58-204-113.us-east-2.compute.amazonaws.com"

  val REMOTE_HOST_PORT: Int = 5672

  val REMOTE_HOST_USERNAME: String = "guest"

  val REMOTE_HOST_PASSWORD: String = "guest"

  val PLAYER_CONNECTION_CHANNEL_QUEUE: String = "player_connection"

  val PLAYERS_CONNECTED_CHANNEL_QUEUE: String = "players_connected"

  val PLAYER_POSITION_CHANNEL_QUEUE: String = "player_position"

  val PLAYER_LOGOUT_CHANNEL_QUEUE: String = "player_logout"

  val PLAYER_POSITION_EXCHANGE: String = "player_position_exchange"

  val NEW_PLAYER_EXCHANGE: String = "new_player_exchange"

  val PLAYER_LOGOUT_EXCHANGE: String = "player_logout_exchange"

  val INITIAL_PLAYER_POSITION: Coordinate = CoordinateImpl(25, 25)
}