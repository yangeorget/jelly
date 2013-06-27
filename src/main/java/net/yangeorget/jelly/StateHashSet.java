package net.yangeorget.jelly;

import java.util.HashSet;
import java.util.Set;

/**
 * @author y.georget
 */
public class StateHashSet
        implements StateSet {
    private final Set<String> set;

    public StateHashSet() {
        set = new HashSet<>(1 << 20, 0.75F);
    }

    @Override
    public boolean store(final State state) {
        final String ser = state.getSerialization()
                                .toString();
        return set.add(ser);
    }

    @Override
    public int size() {
        return set.size();
    }
}
