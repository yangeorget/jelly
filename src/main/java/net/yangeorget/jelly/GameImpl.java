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
        explored = new HashSet<>();
        states = new LinkedList<>();
        final State state = new StateImpl(board);
        states.add(state);
        distinctColorsNb = state.getDistinctColorsNb();
    }

    @Override
    public List<State> getStates() {
        return states;
    }

    @Override
    public boolean solve() {
        for (final State state = states.removeFirst(); !states.isEmpty(); explored.add(state.toBoard()
                                                                                            .toString())) {
            if (state.getJellies()
                     .size() == distinctColorsNb) {
                return true;
            }
            for (int j = 0; j < state.getJellies()
                                     .size(); j++) {
                if (!state.getJellies()
                          .get(j)
                          .isFixed()) {
                    final State leftState = state.move(j, -1);
                    if (leftState != null && !explored.contains(leftState.toBoard()
                                                                         .toString())) {
                        states.addLast(leftState);
                    }
                    final State rightState = state.move(j, 1);
                    if (rightState != null && !explored.contains(rightState.toBoard()
                                                                           .toString())) {
                        states.addLast(rightState);
                    }
                }
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "distinctColorsNb=" + distinctColorsNb + ";states=" + states + ";explored=" + explored;
    }
}
