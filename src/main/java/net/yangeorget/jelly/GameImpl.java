package net.yangeorget.jelly;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author y.georget
 */
public class GameImpl
        implements Game {
    private static final Logger LOG = LoggerFactory.getLogger(GameImpl.class);
    private final LinkedList<State> states;
    private final Set<String> explored;

    public GameImpl(final Board board) {
        final State state = new StateImpl(board);
        explored = new HashSet<>(1 << 20, 0.75F);
        explored.add(state.getSerialization());
        states = new LinkedList<>();
        states.add(state);
    }

    @Override
    public final State solve() {
        while (!states.isEmpty()) {
            final State state = states.removeFirst();
            final Jelly[] jellies = state.getJellies();
            for (int j = 0; j < jellies.length; j++) {
                final Jelly jelly = jellies[j];
                if (jelly.mayMoveLeft()) {
                    final State clone = state.clone();
                    if (clone.moveLeft(j) && process(clone)) {
                        return clone;
                    }
                }
                if (jelly.mayMoveRight()) {
                    final State clone = state.clone();
                    if (clone.moveRight(j) && process(clone)) {
                        return clone;
                    }
                }
            }
        }
        return null;
    }

    private final boolean process(final State clone) {
        clone.gravity();
        if (clone.isSolved()) {
            return true;
        }
        if (explored.add(clone.getSerialization())) {
            states.addLast(clone);
        }
        return false;
    }

    @Override
    public final void explain(final State state) {
        LOG.debug("states explored: " + explored.size());
        state.explain(0);
    }

    @Override
    public final String toString() {
        return "#states=" + states.size() + ";#explored=" + explored.size();
    }
}
