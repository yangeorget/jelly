package net.yangeorget.jelly;

/**
 * A game.
 * @author y.georget
 */
public interface Game {
    /**
     * Solves the game and returns the state corresponding to success or null in case of failure.
     * @return a state
     */
    State solve();

    /**
     * Explains the resolution starting from the success state.
     * @param state the success state
     */
    void explain(State state);
}
