package model.game

import java.util.Optional

import controller.BattleController
import model.entities.{Owner, PokemonFactory, PokemonWithLife, Trainer}
import utilities.Settings

class TrainersBattle(_trainer: Trainer, controller: BattleController, val otherTrainer: Trainer) extends Battle {
  var _round: BattleRound = _
  override var battleFinished: Boolean = false
  override var roundFinished: Boolean = false
  override def trainer: Trainer = _trainer
  override def round: BattleRound = _round
  override var pokeball: Int = 0
  override var myPokemon: PokemonWithLife = _
  override var otherPokemon: PokemonWithLife = _
  private var myPokemonId = 0
  private var otherPokemonId = 0

  override def startBattleRound(pokemonId: Int): Unit = {
    this.myPokemonId = myPokemonId
    myPokemon = PokemonFactory.createPokemon(Owner.TRAINER,Optional.of(myPokemonId),Optional.empty()).get()
    _round = new BattleRoundImpl(myPokemon, myPokemonId, otherPokemon, this)
  }

  override def startBattleRound(myPokemonId: Int, otherPokemonId: Int): Unit = {
    myPokemon = PokemonFactory.createPokemon(Owner.TRAINER,Optional.of(myPokemonId),Optional.empty()).get()
    otherPokemon = PokemonFactory.createPokemon(Owner.TRAINER,Optional.of(otherPokemonId),Optional.empty()).get()
    this.myPokemonId = myPokemonId
    this.otherPokemonId = otherPokemonId
    _trainer.addMetPokemon(otherPokemonId)
    _round = new BattleRoundImpl(myPokemon, myPokemonId, otherPokemon, this)
  }

  override def myPokemonKillsOtherPokemon(won: Boolean): Unit = {
    var pointsEarned: Int = 0
    /*
    if(won){
      roundFinished = true
      val t: Thread = new Thread {
        override def run() {
          val newOtherPokemonId = otherTrainer.getFirstAvailableFavouritePokemon
          if (newOtherPokemonId > 0) {
            //startBattleRound(myPokemonId, newOtherPokemonId)
            Thread.sleep(1000)
            roundFinished = false
          } else {
            battleFinished = true
            pointsEarned = (otherTrainer.level * math.pow(1.2, _trainer.level)).toInt
            _trainer.updateTrainer(pointsEarned)
          }
        }
      }
      t.start()
    } else {
      roundFinished = true
      val newMyPokemonId = _trainer.getFirstAvailableFavouritePokemon
      if(newMyPokemonId > 0){
        startBattleRound(newMyPokemonId, otherPokemonId)
        val t: Thread = new Thread {
          override def run() {
            Thread.sleep(1000)
            roundFinished = false
          }
        }
        t.start()
      } else {
        battleFinished = true
        pointsEarned = 40
        _trainer.updateTrainer(pointsEarned)
        controller.resumeGame()
      }
    }
    */
  }

  override def updatePokemonAndTrainer(event: Int): Unit = event match {
    case Settings.BATTLE_EVENT_ESCAPE =>
      _trainer.updateTrainer(0)
      _round.updatePokemon()
  }

  override def pokeballLaunched(): Boolean = {false}

  override def getMyPokemonId: Int = this.myPokemonId

  override def getOtherPokemonId: Int = this.otherPokemonId
}