package net.yangeorget.jelly;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

public class GameTest {
    private static final Logger LOG = LoggerFactory.getLogger(GameTest.class);

    @Test
    public void testSolve1() {
        testSolveOK(new BoardImpl("     R"));
    }

    @Test
    public void testSolve2() {
        testSolveOK(new BoardImpl("R R"));
    }

    @Test
    public void testSolve3() {
        testSolveOK(new BoardImpl("R R R"));
    }

    @Test
    public void testSolve4() {
        testSolveOK(new BoardImpl("R R B B"));
    }

    @Test
    public void testSolve5() {
        testSolveKO(new BoardImpl("R R B R"));
    }

    @Test
    public void testSolve6() {
        testSolveOK(new BoardImpl(" R B R ", " 00000 ", "       "));
    }


    @Test
    public void testSolve7() {
        testSolveOK(new BoardImpl("  G       B ", "0B111G 22222"));
    }


    @Test
    public void testSolve8() {
        testSolveOK(new BoardImpl("       P    ", "      00    ", "        P B ", "1B222GG33333"));
    }

    private void testSolveOK(final Board board) {
        final Game game = new GameImpl(board);
        LOG.debug(game.toString());
        Assert.assertTrue(game.solve());
    }

    private void testSolveKO(final Board board) {
        final Game game = new GameImpl(board);
        LOG.debug(game.toString());
        Assert.assertFalse(game.solve());
    }

    @Test
    public void testSolveLEVEL_1() {
        Assert.assertTrue(new GameImpl(Board.LEVELS[0]).solve());
    }

    @Test
    public void testSolveLEVEL_2() {
        Assert.assertTrue(new GameImpl(Board.LEVELS[1]).solve());
    }

    @Test
    public void testSolveLEVEL_3() {
        Assert.assertTrue(new GameImpl(Board.LEVELS[2]).solve());
    }

    @Test
    public void testSolveLEVEL_4() {
        Assert.assertTrue(new GameImpl(Board.LEVELS[3]).solve());
    }

    @Test
    public void testSolveLEVEL_5() {
        Assert.assertTrue(new GameImpl(Board.LEVELS[4]).solve());
    }

    @Test
    public void testSolveLEVEL_6() {
        Assert.assertTrue(new GameImpl(Board.LEVELS[5]).solve());
    }

    @Test
    public void testSolveLEVEL_7() {
        Assert.assertTrue(new GameImpl(Board.LEVELS[6]).solve());
    }

    @Test
    public void testSolveLEVEL_8() {
        Assert.assertTrue(new GameImpl(Board.LEVELS[7]).solve());
    }

    @Test
    public void testSolveLEVEL_9() {
        Assert.assertTrue(new GameImpl(Board.LEVELS[8]).solve());
    }

    @Test
    public void testSolveLEVEL_10() {
        Assert.assertTrue(new GameImpl(Board.LEVELS[9]).solve());
    }

    // @Test
    public void testSolveLEVEL_17() {
        Assert.assertTrue(new GameImpl(Board.LEVELS[16]).solve());
    }

    public static void main(final String[] args) {
        for (final Board b : Board.LEVELS) {
            if (b != null) {
                new GameImpl(b).solve();
            }
        }
    }
}
