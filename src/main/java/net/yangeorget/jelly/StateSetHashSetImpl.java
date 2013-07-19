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
        set = new HashSet<>(2000000, 0.75F);
    }

    @Override
    public final boolean store(final byte[] ser) {
        return set.add(getByteBuffer(ser));
    }

    @Override
    public final boolean contains(final byte[] ser) {
        return set.contains(getByteBuffer(ser));
    }

    @Override
    public final int size() {
        return set.size();
    }

    private ByteBuffer getByteBuffer(final byte[] ser) {
        return ByteBuffer.wrap(ser);
    }
}
