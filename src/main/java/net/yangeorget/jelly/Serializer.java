package net.yangeorget.jelly;

/**
 * @author y.georget
 */
public interface Serializer { // TODO: write a serializer which counts blanks and #

    StringBuilder serialize(State state);
}
