package net.yangeorget.jelly;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

public class GameTest {
    private static final Logger LOG = LoggerFactory.getLogger(GameTest.class);

    @Test
    public void testGetStates() {
        Assert.assertEquals(new GameImpl(Board.BOARD1).getStates()
                                                      .size(), 1);
    }

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
}
