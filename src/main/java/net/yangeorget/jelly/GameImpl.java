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
    private State clone;
    private long time;
    private long pushes;
    private double serSum;

    public GameImpl(final Board board) {
        LOG.debug("jellyColorNb=" + board.getJellyColorNb());
        LOG.debug("jellyPositionNb=" + board.getJellyPositionNb());
        explored = new StateSetHashSetImpl();
        states = new LinkedList<>();
        push(new StateImpl(board));
    }

    @Override
    public final State solve(final boolean verbose) {
        time = System.currentTimeMillis();
        while (!states.isEmpty()) {
            final State state = states.removeFirst();
            clone = null;
            final Jelly[] jellies = state.getJellies();
            for (int j = 0; j < jellies.length; j++) {
                final Jelly jelly = jellies[j];
                if (process(state, jelly, j, Board.LEFT, verbose) || process(state, jelly, j, Board.RIGHT, verbose)) {
                    return clone;
                }
            }
        }
        return null;
    }

    boolean process(final State state, final Jelly jelly, final int j, final int vec, final boolean verbose) {
        if (jelly.mayMove(vec)) {
            if (clone == null) {
                clone = state.clone();
            }
            if (clone.move(j, vec)) {
                clone.process();
                if (clone.isSolved()) {
                    if (verbose) {
                        LOG.info("solved in " + (System.currentTimeMillis() - time) + " ms");
                        LOG.debug(toString());
                        clone.explain(0);
                    }
                    return true;
                } else {
                    push(clone);
                    clone = null;
                }
            } else {
                clone.undoMove(vec);
            }
        }
        return false;
    }

    private final void push(final State state) {
        pushes++;
        final byte[] serialization = state.getSerialization();
        serSum += serialization.length;
        if (explored.store(serialization)) {
            // it is not needed to store the serialization of the state
            state.clearSerialization();
            states.addLast(state);
        }
    }

    @Override
    public final String toString() {
        final int statesSize = states.size();
        final int exploredSize = explored.size();
        return "ser~="
               + (serSum / pushes)
               + ";#pushes="
               + pushes
               + ";#exploredSize="
               + exploredSize
               + ";#states="
               + statesSize;
    }
}
