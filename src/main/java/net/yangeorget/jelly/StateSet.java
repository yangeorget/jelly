package net.yangeorget.jelly;

/**
 * Describes a set of states.
 * @author y.georget
 */
public interface StateSet {

    boolean store(byte[] serialization);

    boolean contains(byte[] serialization);

    /**
     * Returns the size of the StateSet.
     * @return the size as an int
     */
    int size();
}
