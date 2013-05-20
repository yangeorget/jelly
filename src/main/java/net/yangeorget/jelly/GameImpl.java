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
    private final Board board;
    private final LinkedList<State> states;
    private final Set<String> explored;

    public GameImpl(final Board board) {
        this.board = board;
        final State state = new StateImpl(board);
        explored = new HashSet<>();
        explored.add(board.toString());
        states = new LinkedList<>();
        states.add(state);
    }

    @Override
    public boolean solve() {
        while (!states.isEmpty()) {
            final State state = states.removeFirst();
            final Jelly[] jellies = state.getJellies();
            for (int j = 0; j < jellies.length; j++) {
                final Jelly jelly = jellies[j];
                if (jelly.mayMoveLeft()) {
                    final State clone = state.clone();
                    if (clone.moveLeft(j) && process(clone)) {
                        return true;
                    }
                }
                if (jelly.mayMoveRight()) {
                    final State clone = state.clone();
                    if (clone.moveRight(j) && process(clone)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean process(final State clone) {
        clone.gravity();
        if (clone.isSolved()) {
            LOG.debug(new BoardImpl(clone).toString());
            return true;
        }
        if (explored.add(clone.getSerialization())) {
            states.addLast(clone);
        }
        return false;
    }

    @Override
    public String toString() {
        return "#colors=" + board.getJellyColorNb() + "#states=" + states.size() + ";#explored=" + explored.size();
    }
}
