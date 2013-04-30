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
        Assert.assertTrue(new GameImpl(new BoardImpl(" R B R ", " ***** ", "       ")).solve());
    }


    @Test
    public void testSolve7() {
        Assert.assertTrue(new GameImpl(new BoardImpl("  G       B ", "#B@@@G *****")).solve());
    }


    @Test
    public void testSolve8() {
        Assert.assertTrue(new GameImpl(new BoardImpl("       P    ", "      **    ", "        P B ", "+B---GG@@@@@")).solve());
    }

    @Test
    public void testSolveLEVEL_1() {
        Assert.assertTrue(new GameImpl(Board.LEVEL_1).solve());
    }

    @Test
    public void testSolveLEVEL_2() {
        Assert.assertTrue(new GameImpl(Board.LEVEL_2).solve());
    }

    @Test
    public void testSolveLEVEL_3() {
        Assert.assertTrue(new GameImpl(Board.LEVEL_3).solve());
    }

    @Test
    public void testSolveLEVEL_4() {
        Assert.assertTrue(new GameImpl(Board.LEVEL_4).solve());
    }

    @Test
    public void testSolveLEVEL_5() {
        Assert.assertTrue(new GameImpl(Board.LEVEL_5).solve());
    }

    @Test
    public void testSolveLEVEL_6() {
        Assert.assertTrue(new GameImpl(Board.LEVEL_6).solve());
    }
}
