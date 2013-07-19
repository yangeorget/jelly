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
    State solve(boolean verbose);
}
