package net.yangeorget.jelly;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

public class GameTest {
    private static final Logger LOG = LoggerFactory.getLogger(GameTest.class);

    @Test
    public void testSolve1() {
        Assert.assertTrue(new GameImpl(new BoardImpl("     R")).solve());
    }

    @Test
    public void testSolve2() {
        Assert.assertTrue(new GameImpl(new BoardImpl("R R")).solve());
    }

    @Test
    public void testSolve3() {
        Assert.assertTrue(new GameImpl(new BoardImpl("R R R")).solve());
    }

    @Test
    public void testSolve4() {
        Assert.assertTrue(new GameImpl(new BoardImpl("R R B B")).solve());
    }

    @Test
    public void testSolve5() {
        Assert.assertFalse(new GameImpl(new BoardImpl("R R B R")).solve());
    }

    @Test
    public void testSolve6() {
        Assert.assertTrue(new GameImpl(new BoardImpl(" R B R ", " 00000 ", "       ")).solve());
    }


    @Test
    public void testSolve7() {
        Assert.assertTrue(new GameImpl(new BoardImpl("  G       B ", "0B111G 22222")).solve());
    }


    @Test
    public void testSolve8() {
        Assert.assertTrue(new GameImpl(new BoardImpl("       P    ", "      00    ", "        P B ", "1B222GG33333")).solve());
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
