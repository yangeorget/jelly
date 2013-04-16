package net.yangeorget.jelly;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

public class StateTest {
    private static final Logger LOG = LoggerFactory.getLogger(StateTest.class);

    @Test
    public void testClone() {
        final State state = new GameImpl(Boards.BOARD1).getStates()
                                                       .get(0);
        Assert.assertEquals(state.toString(), state.clone()
                                                   .toString());
    }

    @Test
    public void testMoveKO1() {
        testMoveKO(new String[] { " BBBB" }, 'B', 0, 1);
    }

    @Test
    public void testMoveKO2() {
        testMoveKO(new String[] { " BBBG" }, 'B', 0, 1);
    }

    @Test
    public void testMoveKO3() {
        testMoveKO(new String[] { " BBw " }, 'B', 0, 1);
    }

    @Test
    public void testMoveKO4() {
        testMoveKO(new String[] { " BGw " }, 'B', 0, 1);
    }

    private void testMoveKO(final String[] input, final char color, final int index, final int move) {
        final Game game = new GameImpl(input);
        final State state = game.getStates()
                                .get(0);
        Assert.assertNull(state.move(color, index, move, game.getHeight(), game.getWidth()));
    }

    @Test
    public void testMoveOK1() {
        testMoveOK(new String[] { " BB  " }, 'B', 0, 1, new String[] { "  BB " });
    }

    @Test
    public void testMoveOK2() {
        testMoveOK(new String[] { " BB  ", " GBB " }, 'G', 0, 1, new String[] { "  BB ", "  GBB" });
    }

    @Test
    public void testMoveOK3() {
        testMoveOK(new String[] { " BBYRR ", " GBB R " }, 'B', 0, 1, new String[] { "  BBYRR", " G BB R" });
    }

    private void testMoveOK(final String[] input,
                            final char color,
                            final int index,
                            final int move,
                            final String[] output) {
        final Game game = new GameImpl(input);
        Boards.assertEquals(game.getStates()
                                .get(0)
                                .move(color, index, move, game.getHeight(), game.getWidth())
                                .toBoard(game.getHeight(), game.getWidth()), Boards.toCharMatrix(output));
    }

    @Test
    public void testMoveDownOK1() {
        testMoveDownOK(new String[] { " BB ", "    ", "    " }, new String[] { "    ", "    ", " BB " });
    }

    private void testMoveDownOK(final String[] input, final String[] output) {
        final Game game = new GameImpl(input);
        final State state = game.getStates()
                                .get(0);
        state.moveDown(game.getHeight());
        LOG.debug(state.toString());
        Boards.assertEquals(state.toBoard(game.getHeight(), game.getWidth()), Boards.toCharMatrix(output));
    }
}
