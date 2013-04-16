package net.yangeorget.jelly;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class StateImpl
        implements State {
    private final Map<Character, List<Jelly>> floatingJellies;
    private final Map<Character, List<Jelly>> fixedJellies;

    public StateImpl() {
        floatingJellies = new HashMap<Character, List<Jelly>>();
        fixedJellies = new HashMap<Character, List<Jelly>>();
    }

    public StateImpl(final char[][] board) {
        this();
        for (int i = 0; i < Boards.getHeight(board); i++) {
            for (int j = 0; j < Boards.getWidth(board); j++) {
                final Character color = Character.toUpperCase(board[i][j]);
                if (color != 0 && !Character.isWhitespace(color)) {
                    final Jelly jelly = new JellyImpl();
                    final boolean fixed = update(jelly, color, i, j, board);
                    if (fixed) {
                        store(fixedJellies, color, jelly);
                    } else {
                        store(floatingJellies, color, jelly);
                    }
                }
            }
        }
    }

    @Override
    public State clone() {
        final State state = new StateImpl();
        for (final Character color : fixedJellies.keySet()) {
            for (final Jelly jelly : fixedJellies.get(color)) {
                store(state.getFixedJellies(), color, jelly.clone());
            }
        }
        for (final Character color : floatingJellies.keySet()) {
            for (final Jelly jelly : floatingJellies.get(color)) {
                store(state.getFloatingJellies(), color, jelly.clone());
            }
        }
        return state;
    }

    private static void store(final Map<Character, List<Jelly>> map, final Character color, final Jelly jelly) {
        List<Jelly> list = map.get(color);
        if (list == null) {
            list = new LinkedList<Jelly>();
            map.put(color, list);
        }
        list.add(jelly);
    }

    private boolean update(final Jelly jelly, final char color, final int i, final int j, final char[][] board) {
        boolean fixed = false;
        if (color == Character.toUpperCase(board[i][j])) {
            fixed |= Character.isLowerCase(board[i][j]);
            board[i][j] = 0;
            jelly.store(i, j);
            if (i > 0) {
                update(jelly, color, i - 1, j, board);
            }
            if (i < Boards.getHeight(board) - 1) {
                update(jelly, color, i + 1, j, board);
            }
            if (j > 0) {
                update(jelly, color, i, j - 1, board);
            }
            if (j < Boards.getWidth(board) - 1) {
                update(jelly, color, i, j + 1, board);
            }
        }
        return fixed;
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
    public State move(final Character color, final int index, final int move, final int height, final int width) {
        final State state = clone();
        if (!state.moveHorizontally(state.getFloatingJellies()
                                         .get(color)
                                         .get(index), move, width)) {
            return null;
        }
        state.moveDown(height);
        // join
        return state;
    }

    @Override
    public boolean moveHorizontally(final Jelly jelly, final int move, final int width) {
        if (!jelly.moveHorizontally(move, width)) {
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
                    if (!moveHorizontally(j, move, width)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public void moveDown(final int height) {
        for (boolean loop = false; loop;) {
            for (final Character c : floatingJellies.keySet()) {
                for (final Jelly j : floatingJellies.get(c)) {

                    // TODO : clone jelly
                    loop |= moveDown(j, height);
                }
            }
        }
    }

    @Override
    public boolean moveDown(final Jelly jelly, final int height) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public String toString() {
        return "fixed=" + fixedJellies + ";nonFixed=" + floatingJellies;
    }

    @Override
    public char[][] toBoard(final int height, final int width) {
        final char[][] board = new char[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                board[i][j] = ' ';
            }
        }
        for (final Character color : fixedJellies.keySet()) {
            for (final Jelly jelly : fixedJellies.get(color)) {
                jelly.updateBoard(board, color);
            }
        }
        for (final Character color : floatingJellies.keySet()) {
            for (final Jelly jelly : floatingJellies.get(color)) {
                jelly.updateBoard(board, color);
            }
        }
        return board;
    }
}
