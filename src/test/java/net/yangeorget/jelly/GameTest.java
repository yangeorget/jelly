package net.yangeorget.jelly;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

public class GameTest {
    private static final Logger LOG = LoggerFactory.getLogger(GameTest.class);

    @Test
    public void testSolve1() {
        testSolveOK(new BoardImpl(new String[] { "     R" }));
    }

    @Test
    public void testSolve2() {
        testSolveOK(new BoardImpl(new String[] { "R R" }));
    }

    @Test
    public void testSolve3() {
        testSolveOK(new BoardImpl(new String[] { "R R R" }));
    }

    @Test
    public void testSolve4() {
        testSolveOK(new BoardImpl(new String[] { "R R B B" }));
    }

    @Test
    public void testSolve5() {
        testSolveKO(new BoardImpl(new String[] { "R R B R" }));
    }

    @Test
    public void testSolve6() {
        testSolveOK(new BoardImpl(new String[] { " R B R ", " ##### ", "       " }));
    }


    @Test
    public void testSolve7() {
        testSolveOK(new BoardImpl(new String[] { "  G       B ", "#B###G #####" }));
    }


    @Test
    public void testSolve8() {
        testSolveOK(new BoardImpl(new String[] { "       P    ", "      ##    ", "        P B ", "#B###GG#####" }));
    }

    private void testSolveOK(final Board board) {
        final Game game = new GameImpl(board);
        Assert.assertTrue(game.solve());
    }

    private void testSolveKO(final Board board) {
        final Game game = new GameImpl(board);
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

    /*
     * @Test public void testSolveLEVEL_10() { Assert.assertTrue(new GameImpl(Board.LEVELS[9]).solve()); }
     * @Test public void testSolveLEVEL_11() { Assert.assertTrue(new GameImpl(Board.LEVELS[10]).solve()); }
     * @Test public void testSolveLEVEL_12() { Assert.assertTrue(new GameImpl(Board.LEVELS[11]).solve()); }
     * @Test public void testSolveLEVEL_13() { Assert.assertTrue(new GameImpl(Board.LEVELS[12]).solve()); }
     * @Test public void testSolveLEVEL_14() { Assert.assertTrue(new GameImpl(Board.LEVELS[13]).solve()); }
     * @Test public void testSolveLEVEL_15() { Assert.assertTrue(new GameImpl(Board.LEVELS[14]).solve()); }
     * @Test public void testSolveLEVEL_16() { Assert.assertTrue(new GameImpl(Board.LEVELS[15]).solve()); }
     * @Test public void testSolveLEVEL_17() { Assert.assertTrue(new GameImpl(Board.LEVELS[16]).solve()); }
     * @Test public void testSolveLEVEL_18() { Assert.assertTrue(new GameImpl(Board.LEVELS[17]).solve()); }
     * @Test public void testSolveLEVEL_19() { Assert.assertTrue(new GameImpl(Board.LEVELS[18]).solve()); }
     * @Test public void testSolveLEVEL_20() { Assert.assertTrue(new GameImpl(Board.LEVELS[19]).solve()); }
     */
    public static void main(final String[] args) {
        for (int i = 0; i < Board.LEVELS.length; i++) {
            new GameImpl(Board.LEVELS[i]).solve();
        }
    }
}
