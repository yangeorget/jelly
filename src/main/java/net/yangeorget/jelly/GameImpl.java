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
    public final State solve(final boolean verbose) {
        final long time = System.currentTimeMillis();
        try {
            while (!states.isEmpty()) {
                final State state = pop();
                State clone = null;
                final Jelly[] jellies = state.getJellies();
                for (int j = 0; j < jellies.length; j++) {
                    final Jelly jelly = jellies[j];
                    if (jelly.mayMove(Board.LEFT)) { // TODO: clean this
                        if (clone == null) {
                            clone = state.clone();
                        }
                        if (clone.move(j, Board.LEFT)) {
                            if (process(clone)) {
                                if (verbose) {
                                    clone.explain(0);
                                }
                                return clone;
                            } else {
                                clone = null;
                            }
                        } else {
                            clone.undoMove(Board.LEFT);
                        }
                    }
                    if (jelly.mayMove(Board.RIGHT)) {
                        if (clone == null) {
                            clone = state.clone();
                        }
                        if (clone.move(j, Board.RIGHT)) {
                            if (process(clone)) {
                                if (verbose) {
                                    clone.explain(0);
                                }
                                return clone;
                            } else {
                                clone = null;
                            }
                        } else {
                            clone.undoMove(Board.RIGHT);
                        }
                    }
                }
            }
            return null;
        } finally {
            if (verbose) {
                LOG.info("solved in " + (System.currentTimeMillis() - time) + " ms");
                LOG.info(toString());
            }
        }
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
        if (explored.store(state.getSerialization())) {
            // it is not needed to store the serialization of the state
            state.clearSerialization();
            states.addLast(state);
        }
    }

    private final State pop() {
        return states.removeFirst();
    }

    @Override
    public final String toString() {
        final int statesSize = states.size();
        final int pushes = explored.size();
        final int pops = pushes - statesSize;
        return "#states=" + statesSize + "#pops=" + pops + ";#pushes=" + pushes + ";#ratio=" + ((float) pops / pushes);
    }

    public static void main(final String[] args) {
        for (final String arg : args) {
            new GameImpl(Board.LEVELS[Integer.parseInt(arg)]).solve(true);
        }
    }
}
