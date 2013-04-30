package net.yangeorget.jelly;

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
        explored.add(board.toString());
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
                LOG.debug(state.toBoard()
                               .toString());
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
        return false;
    }

    void move(final State state, final int j, final int move) {
        // LOG.debug("state=" + state.toString() + ";j=" + j + ";move=" + move);
        // LOG.debug("board=" + state.toBoard()
        // .toString());
        final State newState = state.clone();
        final Board board = newState.move(newState.getJelly(j), move);
        // LOG.debug("newState=" + newState.toString());
        // LOG.debug("board=" + newState.toBoard()
        // .toString());
        if (board != null) {
            final String ser = board.toString();
            // LOG.debug("ser=" + ser);
            // LOG.debug("explored=" + explored.toString());
            if (!explored.contains(ser)) {
                states.addLast(newState);
                explored.add(ser);
                // LOG.debug("states=" + states.toString());
                // dump();
            }
        }
    }

    @Override
    public String toString() {
        return "distinctColorsNb=" + distinctColorsNb + ";#states=" + states.size() + ";#explored=" + explored.size();
    }
}
