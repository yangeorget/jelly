package net.yangeorget.jelly;

/**
 * Describes a set of states.
 * @author y.georget
 */
public interface StateSet {

    /**
     * Stores a state.
     * @param state the state
     * @return true iff the state was not contained in the StateSet before
     */
    boolean store(State state);

    /**
     * Returns the size of the StateSet.
     * @return the size as an int
     */
    int size();
}
