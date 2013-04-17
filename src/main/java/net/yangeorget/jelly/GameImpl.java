package net.yangeorget.jelly;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameImpl
        implements Game {
    private static final Logger LOG = LoggerFactory.getLogger(GameImpl.class);
    private final int width;
    private final int height;
    private final int distinctColorsNb;
    private final LinkedList<State> states;
    private final Set<State> explored;

    public GameImpl(final String[] board) {
        this(Boards.toCharMatrix(board));
    }

    public GameImpl(final char[][] board) {
        explored = new HashSet<State>();
        states = new LinkedList<State>();
        final State state = new StateImpl(board);
        LOG.debug(state.toString());
        states.add(state);
        final Set<Character> colors = new HashSet<Character>();
        colors.addAll(state.getFloatingJellies()
                           .keySet());
        colors.addAll(state.getFixedJellies()
                           .keySet());
        distinctColorsNb = colors.size();
        width = Boards.getWidth(board);
        height = Boards.getHeight(board);
    }

    @Override
    public List<State> getStates() {
        return states;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public boolean solve() {
        for (final State state = states.removeFirst(); explored.add(state);) {
            final Map<Character, List<Jelly>> map = state.getFloatingJellies();
            for (final Character color : map.keySet()) {
                for (int i = map.get(color)
                                .size() - 1; i >= 0; i--) {
                    for (int move = -1; move <= 1; move += 2) {
                        if (check(state.move(color, i, move, height, width))) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private boolean check(final State state) {
        if (state != null && !explored.contains(state)) {
            if (state.getFloatingJellies()
                     .size() + state.getFixedJellies()
                                    .size() == distinctColorsNb) {
                return true;
            }
            states.addLast(state);
        }
        return false;
    }

    @Override
    public String toString() {
        return "distinctColorsNb=" + distinctColorsNb + ";states=" + states + ";explored=" + explored;
    }
}
