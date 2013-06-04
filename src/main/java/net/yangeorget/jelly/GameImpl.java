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
    private final LinkedList<State> states;
    private final Set<String> explored;

    public GameImpl(final Board board) {
        final State state = new StateImpl(board);
        explored = new HashSet<>(1 << 16, 0.75F);
        explored.add(state.getSerialization());
        states = new LinkedList<>();
        states.add(state);
    }

    @Override
    public boolean solve() { // TODO: store parent state
        while (!states.isEmpty()) {
            final State state = states.removeFirst();
            final Jelly[] jellies = state.getJellies();
            // LOG.debug("state: " + state.toString());
            for (int j = 0; j < jellies.length; j++) {
                final Jelly jelly = jellies[j];
                // LOG.debug("jelly: " + jelly);
                if (jelly.mayMoveLeft()) {
                    // LOG.debug("moveLeft");
                    final State clone = state.clone();
                    if (clone.moveLeft(j) && process(clone)) {
                        return true;
                    }
                    // LOG.debug("moveLeft failed");
                }
                if (jelly.mayMoveRight()) {
                    // LOG.debug("moveRight");
                    final State clone = state.clone();
                    if (clone.moveRight(j) && process(clone)) {
                        return true;
                    }
                    // LOG.debug("moveRight failed");
                }
            }
        }
        return false;
    }

    private boolean process(final State clone) {
        clone.gravity();
        if (clone.isSolved()) {
            clone.updateBoard();
            LOG.debug(clone.getBoard()
                           .toString());
            return true;
        }
        if (explored.add(clone.getSerialization())) {
            states.addLast(clone);
        }
        return false;
    }

    @Override
    public String toString() {
        return "#states=" + states.size() + ";#explored=" + explored.size();
    }
}
