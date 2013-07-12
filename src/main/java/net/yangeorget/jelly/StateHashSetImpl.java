package net.yangeorget.jelly;

import java.util.HashSet;
import java.util.Set;

/**
 * HashSet-based implementation of a StateSet.
 * @author y.georget
 */
public class StateHashSetImpl
        implements StateSet {
    private final Set set;

    /**
     * Sole constructor.
     */
    public StateHashSetImpl() {
        set = new HashSet<>(1 << 20, 0.75F);
    }

    @Override
    public boolean store(final State state) {
        return set.add(state.getSerialization());
    }

    @Override
    public int size() {
        return set.size();
    }
}
