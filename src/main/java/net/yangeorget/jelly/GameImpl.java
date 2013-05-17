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
            final State state = states.removeFirst();
            final Jelly[] jellies = state.getJellies();
            for (int j = 0; j < jellies.length; j++) {
                final Jelly jelly = jellies[j];
                if (jelly.mayMoveLeft()) {
                    final State clone = state.clone();
                    if (clone.moveLeft(j)) {
                        clone.gravity();
                        if (clone.getJellies().length == distinctColorsNb) {
                            LOG.debug(new BoardImpl(clone).toString());
                            return true;
                        }
                        if (explored.add(clone.getSerialization())) {
                            states.addLast(clone);
                        }
                    }
                }
                if (jelly.mayMoveRight()) {
                    final State clone = state.clone();
                    if (clone.moveRight(j)) {
                        clone.gravity();
                        if (clone.getJellies().length == distinctColorsNb) {
                            LOG.debug(new BoardImpl(clone).toString());
                            return true;
                        }
                        if (explored.add(clone.getSerialization())) {
                            states.addLast(clone);
                        }
                    }
                }
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "distinctColorsNb=" + distinctColorsNb + ";#states=" + states.size() + ";#explored=" + explored.size();
    }
}
