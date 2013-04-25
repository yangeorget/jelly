package net.yangeorget.jelly;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
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
        explored.add(state.serialize());
        states = new LinkedList<>();
        states.add(state);
        distinctColorsNb = state.getDistinctColorsNb();
    }

    @Override
    public boolean solve() {
        while (!states.isEmpty()) {
            LOG.debug("before pop" + toString());
            // dump();
            final State state = states.removeFirst();
            // LOG.debug("state=" + state.toString());
            // LOG.debug("board=" + state.toBoard()
            // .toString());
            // LOG.debug("after pop" + toString());
            // dump();
            // LOG.debug("explored=" + explored.toString());
            final List<Jelly> jellies = state.getJellies();
            // LOG.debug("#jellies=" + jellies.size());
            if (jellies.size() == distinctColorsNb) {
                // LOG.debug(toString());
                return true;
            }
            for (int j = 0; j < jellies.size(); j++) {
                if (!jellies.get(j)
                            .isFixed()) {
                    move(state, j, -1);
                    move(state, j, 1);
                }
            }
        }
        // LOG.debug(toString());
        return false;
    }

    void move(final State state, final int j, final int move) {
        // LOG.debug("state=" + state.toString() + ";j=" + j + ";move=" + move);
        // LOG.debug("board=" + state.toBoard()
        // .toString());
        final State newState = state.clone();
        final boolean moveOk = newState.move(newState.getJellies()
                                                     .get(j), move);
        // LOG.debug("newState=" + newState.toString());
        // LOG.debug("board=" + newState.toBoard()
        // .toString());
        if (moveOk) {
            final String ser = newState.serialize();
            // LOG.debug("ser=" + ser);
            // LOG.debug("explored=" + explored.toString());
            if (!explored.contains(ser)) {
                states.addLast(newState);
                explored.add(newState.serialize());
                // LOG.debug("states=" + states.toString());
                // dump();
            }
        }
    }

    void dump() {
        final List<String> serStates = new LinkedList<>();
        for (final State state : states) {
            serStates.add(state.serialize());
        }
        Collections.sort(serStates);
        for (final String s : serStates) {
            LOG.debug(s);
        }


    }

    @Override
    public String toString() {
        return "distinctColorsNb=" + distinctColorsNb + ";#states=" + states.size() + ";#explored=" + explored.size();
    }
}
