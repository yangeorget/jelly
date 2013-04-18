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
    public void testGravityOK1() {
        testGravityOK(new String[] { " BB ", "    ", "    " }, new String[] { "    ", "    ", " BB " });
    }

    @Test
    public void testGravityOK2() {
        testGravityOK(new String[] { "  GG ", " BB  ", "     " }, new String[] { "     ", "  GG ", " BB  " });
    }

    @Test
    public void testGravityOK3() {
        testGravityOK(new String[] { "  GG ", " BBG ", "  GG ", "     " }, new String[] { "     ",
                                                                                         "  GG ",
                                                                                         " BBG ",
                                                                                         "  GG " });
    }

    private void testGravityOK(final String[] input, final String[] output) {
        final Game game = new GameImpl(input);
        final State state = game.getStates()
                                .get(0);
        state.gravity(game.getHeight(), game.getWidth());
        LOG.debug(state.toString());
        Boards.assertEquals(state.toBoard(game.getHeight(), game.getWidth()), Boards.toCharMatrix(output));
    }

    @Test
    public void testSlideKO1() {
        testSlideKO(new String[] { " BBBB" }, 'B', 0, 1);
    }

    @Test
    public void testSlideKO2() {
        testSlideKO(new String[] { " BBBG" }, 'B', 0, 1);
    }

    @Test
    public void testSlideKO3() {
        testSlideKO(new String[] { " BBw " }, 'B', 0, 1);
    }

    @Test
    public void testSlideKO4() {
        testSlideKO(new String[] { " BGw " }, 'B', 0, 1);
    }

    private void testSlideKO(final String[] input, final char color, final int index, final int move) {
        final Game game = new GameImpl(input);
        final State state = game.getStates()
                                .get(0);
        Assert.assertNull(state.slide(color, index, move, game.getHeight(), game.getWidth()));
    }

    @Test
    public void testSlideOK1() {
        testSlideOK(new String[] { " BB  " }, 'B', 0, 1, new String[] { "  BB " });
    }

    @Test
    public void testSlideOK2() {
        testSlideOK(new String[] { " BB  ", " GBB " }, 'G', 0, 1, new String[] { "  BB ", "  GBB" });
    }

    @Test
    public void testSlideOK3() {
        testSlideOK(new String[] { " BBYRR ", " GBB R " }, 'B', 0, 1, new String[] { "  BBYRR", " G BB R" });
    }

    @Test
    public void testSlideOK4() {
        testSlideOK(new String[] { " YYGGG ", " GGG B ", "     w " }, 'B', 0, 1, new String[] { "       ",
                                                                                               " YYGGG ",
                                                                                               " GGG wB" });
    }

    private void testSlideOK(final String[] input,
                             final char color,
                             final int index,
                             final int move,
                             final String[] output) {
        final Game game = new GameImpl(input);
        Boards.assertEquals(game.getStates()
                                .get(0)
                                .slide(color, index, move, game.getHeight(), game.getWidth())
                                .toBoard(game.getHeight(), game.getWidth()), Boards.toCharMatrix(output));
    }
}
