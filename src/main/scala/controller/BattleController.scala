package controller

import java.util.concurrent.{Executors, TimeUnit}

import model.entities.Trainer
import model.game.{Battle, BattleImpl}
import view.View

import scala.util.Random

trait BattleController {
  def myPokemonAttacks(attackId: Int): Unit

  def pokemonWildAttacks(): Unit
}

class BattleControllerImpl(val trainer: Trainer, val view: View) extends BattleController {
  val battle: Battle = new BattleImpl(trainer)
  battle.startBattleRound(trainer.getFirstAvailableFavouritePokemon)
  view.showBattle(battle.myPokemon,battle.wildPokemon)

  override def myPokemonAttacks(attackId: Int): Unit = {
    battle.round.myPokemonAttack(attackId)
    if(battle.battleFinished) {
        //qualcosa
    }
  }

  override def pokemonWildAttacks(): Unit = {
    val executorService = Executors.newSingleThreadScheduledExecutor
    executorService.scheduleAtFixedRate(() => {
      battle.round.wildPokemonAttack(Random.nextInt(4))
    }, 3, 3, TimeUnit.SECONDS)
    if(battle.battleFinished) {
      //qualcosa
    }
  }



}