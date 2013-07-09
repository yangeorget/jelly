package net.yangeorget.jelly;

import java.util.HashSet;
import java.util.Set;

/**
 * HashSet-based implementation of a StateSet.
 * @author y.georget
 */
public class StateHashSetImpl
        implements StateSet {
    private final Set<String> set;

    /**
     * Sole constructor.
     */
    public StateHashSetImpl() {
        set = new HashSet<>(1 << 20, 0.75F);
    }

    @Override
    public boolean store(final State state) {
        final String ser = state.getSerialization();
        // LOG.debug("serialization length: " + ser.length());
        return set.add(ser);
    }

    @Override
    public int size() {
        return set.size();
    }
}
