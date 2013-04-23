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
        explored = new HashSet<>();
        states = new LinkedList<>();
        final State state = new StateImpl(board);
        states.add(state);
        distinctColorsNb = state.getDistinctColorsNb();
    }

    @Override
    public boolean solve() {
        while (!states.isEmpty()) {
            final State state = states.removeFirst();
            explored.add(state.toBoard()
                              .toString());
            if (state.getJellies()
                     .size() == distinctColorsNb) {
                return true;
            }
            solve(state);
        }
        return false;
    }

    void solve(final State state) {
        for (int j = 0; j < state.getJellies()
                                 .size(); j++) {
            solve(state, j);
        }
    }

    void solve(final State state, final int j) {
        if (!state.getJellies()
                  .get(j)
                  .isFixed()) {
            check(state.move(j, -1));
            check(state.move(j, 1));
        }
    }

    void check(final State state) {
        if (state != null && !explored.contains(state.toBoard()
                                                     .toString())) {
            states.addLast(state);
        }
    }

    @Override
    public String toString() {
        return "distinctColorsNb=" + distinctColorsNb + ";states=" + states + ";explored=" + explored;
    }
}
