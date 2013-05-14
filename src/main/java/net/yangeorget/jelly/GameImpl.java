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
    private final int distinctColorsNb;
    private final LinkedList<State> states;
    private final Set<String> explored;

    /**
     * @param board
     */
    public GameImpl(final Board board) {
        final State state = new StateImpl(board);
        explored = new HashSet<>();
        explored.add(board.toString());
        states = new LinkedList<>();
        states.add(state);
        distinctColorsNb = state.getDistinctColorsNb();
    }

    @Override
    public boolean solve() {
        while (!states.isEmpty()) {
            // LOG.debug(toString());
            final State state = states.removeFirst();
            final Jelly[] jellies = state.getJellies();
            final int size = jellies.length;
            if (size == distinctColorsNb) {
                LOG.debug(state.toBoard()
                               .toString());
                return true;
            }
            for (int j = 0; j < size; j++) {
                if (!jellies[j].isFixed()) {
                    move(state, j, -1);
                    move(state, j, 1);
                }
            }
        }
        return false;
    }

    void move(final State state, final int j, final int move) {
        final State newState = state.clone();
        final Jelly[] newStateJellies = newState.getJellies();
        final String ser = newState.move(newStateJellies[j], move);
        if (ser != null) {
            if (!explored.contains(ser)) {
                explored.add(ser);
                states.addLast(newState);
            }
        }
    }

    @Override
    public String toString() {
        return "distinctColorsNb=" + distinctColorsNb + ";#states=" + states.size() + ";#explored=" + explored.size();
    }
}
