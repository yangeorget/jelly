package net.yangeorget.jelly;

import java.util.HashSet;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author y.georget
 */
public class StateHashSetImpl
        implements StateSet {
    private static final Logger LOG = LoggerFactory.getLogger(StateHashSetImpl.class);
    private final Set<String> set;

    public StateHashSetImpl() {
        set = new HashSet<>(1 << 20, 0.75F);
    }

    @Override
    public boolean store(final State state) {
        final String ser = state.getSerialization()
                                .toString();
        // LOG.debug("serialization length: " + ser.length());
        return set.add(ser);
    }

    @Override
    public int size() {
        return set.size();
    }
}
