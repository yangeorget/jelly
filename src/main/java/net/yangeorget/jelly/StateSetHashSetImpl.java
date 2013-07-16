package net.yangeorget.jelly;

import java.nio.ByteBuffer;
import java.util.HashSet;
import java.util.Set;

/**
 * HashSet-based implementation of a StateSet.
 * @author y.georget
 */
public final class StateSetHashSetImpl
        implements StateSet {
    private final Set<ByteBuffer> set;

    /**
     * Sole constructor.
     */
    public StateSetHashSetImpl() {
        set = new HashSet<>(1 << 20, 0.75F);
    }

    @Override
    public boolean store(final byte[] ser) {
        return set.add(ByteBuffer.wrap(ser));
    }

    @Override
    public int size() {
        return set.size();
    }
}
