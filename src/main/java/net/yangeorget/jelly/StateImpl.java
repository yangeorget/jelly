package net.yangeorget.jelly;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StateImpl
        implements State {
    private static final Logger LOG = LoggerFactory.getLogger(StateImpl.class);

    // TODO: only store a list of jellies containing : color, fixed
    private final Map<Character, List<Jelly>> floatingJellies;
    private final Map<Character, List<Jelly>> fixedJellies;
    private final Frame frame;

    public StateImpl(final Board board) {
        this.frame = board;
        floatingJellies = new HashMap<Character, List<Jelly>>();
        fixedJellies = new HashMap<Character, List<Jelly>>();
        final boolean[][] visited = new boolean[board.getHeight()][board.getWidth()];
        for (int i = 0; i < board.getHeight(); i++) {
            for (int j = 0; j < board.getWidth(); j++) {
                if (!visited[i][j]) {
                    final Character color = board.get(i, j);
                    if (!Character.isWhitespace(color)) {
                        final Jelly jelly = new JellyImpl(board);
                        final boolean fixed = update(visited, jelly, board, color, i, j);
                        if (fixed) {
                            store(fixedJellies, color, jelly);
                        } else {
                            store(floatingJellies, color, jelly);
                        }
                    }
                }
            }
        }
    }

    // TODO: move to Jelly
    private boolean update(final boolean[][] visited,
                           final Jelly jelly,
                           final Board board,
                           final char color,
                           final int i,
                           final int j) {
        boolean fixed = false;
        final char c = board.get(i, j);
        if (!visited[i][j] && Character.toUpperCase(color) == Character.toUpperCase(c)) {
            fixed |= Character.isLowerCase(c);
            visited[i][j] = true;
            jelly.store(i, j);
            if (i > 0) {
                update(visited, jelly, board, color, i - 1, j);
            }
            if (i < board.getHeight() - 1) {
                update(visited, jelly, board, color, i + 1, j);
            }
            if (j > 0) {
                update(visited, jelly, board, color, i, j - 1);
            }
            if (j < board.getWidth() - 1) {
                update(visited, jelly, board, color, i, j + 1);
            }
        }
        return fixed;
    }

    @Override
    public State clone() {
        return new StateImpl(toBoard());
    }

    private static void store(final Map<Character, List<Jelly>> map, final Character color, final Jelly jelly) {
        List<Jelly> list = map.get(color);
        if (list == null) {
            list = new LinkedList<Jelly>();
            map.put(color, list);
        }
        list.add(jelly);
    }


    @Override
    public Map<Character, List<Jelly>> getFloatingJellies() {
        return floatingJellies;
    }

    @Override
    public Map<Character, List<Jelly>> getFixedJellies() {
        return fixedJellies;
    }

    @Override
    public State slide(final Character color, final int index, final int move) {
        final State state = clone();
        LOG.debug("\n" + state.toBoard()
                              .toString());
        if (state.slide(state.getFloatingJellies()
                             .get(color)
                             .get(index), move)) {
            state.gravity();
            LOG.debug("\n" + state.toBoard()
                                  .toString());
            state.join();
            return state;
        } else {
            return null;
        }
    }

    @Override
    public boolean slide(final Jelly jelly, final int move) {
        if (!jelly.hMove(move)) {
            return false;
        }
        for (final Character c : fixedJellies.keySet()) {
            for (final Jelly j : fixedJellies.get(c)) {
                if (jelly.overlaps(j)) { // cannot move
                    return false;
                }
            }
        }
        for (final Character c : floatingJellies.keySet()) {
            for (final Jelly j : floatingJellies.get(c)) {
                if (!jelly.equals(j) && jelly.overlaps(j)) {
                    if (!slide(j, move)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public void gravity() {
        // LOG.debug("\n" + Boards.toString(toBoard(height, width)));
        for (final Character c : floatingJellies.keySet()) {
            for (final Jelly j : floatingJellies.get(c)) { // ideally start with jellies at bottom
                final Jelly jc = j.clone();
                if (gravity(j)) {
                    gravity();
                } else {
                    j.restore(jc);
                }
            }
        }
    }

    @Override
    public boolean gravity(final Jelly jelly) {
        if (!jelly.vMove(1)) {
            return false;
        }
        for (final Character c : fixedJellies.keySet()) {
            for (final Jelly j : fixedJellies.get(c)) {
                if (jelly.overlaps(j)) { // cannot move
                    return false;
                }
            }
        }
        for (final Character c : floatingJellies.keySet()) {
            for (final Jelly j : floatingJellies.get(c)) {
                if (!jelly.equals(j) && jelly.overlaps(j)) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void join() {
        // TODO: also join fixed jellies
        for (final Character c : floatingJellies.keySet()) {
            final List<Jelly> jellies = floatingJellies.get(c);
            for (int i = 0; i < jellies.size() - 1; i++) {
                final Jelly jelly = jellies.get(i);
                for (int j = i + 1; j < jellies.size(); j++) {
                    final Jelly je = jellies.get(j);
                    if (jelly.adjacentTo(je)) {
                        jelly.merge(je); // TODO: tst merge & join
                        jellies.remove(je);
                    }
                }
            }
        }
    }

    @Override
    public String toString() {
        return "fixed=" + fixedJellies + ";nonFixed=" + floatingJellies;
    }

    @Override
    public Board toBoard() {
        final char[][] m = new char[frame.getHeight()][frame.getWidth()];
        for (int i = 0; i < frame.getHeight(); i++) {
            for (int j = 0; j < frame.getWidth(); j++) {
                m[i][j] = ' ';
            }
        }
        for (final Character color : fixedJellies.keySet()) {
            for (final Jelly jelly : fixedJellies.get(color)) {
                for (final Position position : jelly.getPositions()) {
                    m[position.getI()][position.getJ()] = color;
                }
            }
        }
        for (final Character color : floatingJellies.keySet()) {
            for (final Jelly jelly : floatingJellies.get(color)) {
                for (final Position position : jelly.getPositions()) {
                    m[position.getI()][position.getJ()] = color;
                }
            }
        }
        return new BoardImpl(m);
    }
}
