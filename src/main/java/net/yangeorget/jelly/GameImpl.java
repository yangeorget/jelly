package net.yangeorget.jelly;

import java.util.LinkedList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author y.georget
 */
public final class GameImpl
        implements Game {
    private static final Logger LOG = LoggerFactory.getLogger(GameImpl.class);
    private final LinkedList<State> states;
    private final StateSet explored;

    public GameImpl(final Board board) {
        LOG.debug("jellyColorNb=" + board.getJellyColorNb());
        LOG.debug("jellyPositionNb=" + board.getJellyPositionNb());
        explored = new StateSetHashSetImpl();
        states = new LinkedList<>();
        push(new StateImpl(board));
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
        // LOG.debug(toString());
        clone.process();
        if (clone.isSolved()) {
            return true;
        } else {
            push(clone);
            return false;
        }
    }

    private final void push(final State state) {
        final boolean store = explored.store(state.getSerialization());
        if (store) {
            states.addLast(state);
            // it is not needed to store the serialization of the state
            state.clearSerialization();
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
        return "#states=" + statesSize + "#pops=" + pops + ";#pushes=" + pushes + ";#ratio=" + ((float) pops / pushes);
    }
}
