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
        states = new LinkedList<>();
        push(state);
    }

    @Override
    public final State solve() {
        while (!states.isEmpty()) {
            final State state = pop();
            State clone = null;
            final Jelly[] jellies = state.getJellies();
            for (int j = 0; j < jellies.length; j++) {
                final Jelly jelly = jellies[j];
                if (jelly.mayMoveLeft()) {
                    if (clone == null) {
                        clone = state.clone();
                    }
                    if (clone.moveLeft(j)) {
                        if (process(clone)) {
                            return clone;
                        } else {
                            clone = null;
                        }
                    } else {
                        clone.undoMoveLeft();
                    }
                }
                if (jelly.mayMoveRight()) {
                    if (clone == null) {
                        clone = state.clone();
                    }
                    if (clone.moveRight(j)) {
                        if (process(clone)) {
                            return clone;
                        } else {
                            clone = null;
                        }
                    } else {
                        clone.undoMoveRight();
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
        push(clone);
        return false;
    }

    private final void push(final State state) {
        if (explored.add(state.getSerialization())) {
            states.addLast(state);
        }
    }

    private final State pop() {
        return states.removeFirst();
    }

    @Override
    public final void explain(final State state) {
        LOG.debug(toString());
        state.explain(0);
    }

    @Override
    public final String toString() {
        final int statesSize = states.size();
        final int pushes = explored.size();
        final int pops = pushes - statesSize;
        return "#states=" + statesSize + "#pops=" + pops + ";#pushes=" + pushes + ";#ratio=" + (pops / pushes);
    }
}
