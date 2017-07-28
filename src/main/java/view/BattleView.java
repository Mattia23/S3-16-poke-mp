package view;

import java.util.List;

public interface BattleView {
    void setPokemonLife();

    void setPokemonLifeProgressBar(int life, int owner);

    void pokemonIsDead(int owner);

    void pokemonWildAttacksAfterTrainerChoice();

    int[] getOtherPokemonAttacks();
}
