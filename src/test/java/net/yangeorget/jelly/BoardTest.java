package net.yangeorget.jelly;

import org.testng.Assert;
import org.testng.annotations.Test;

public class BoardTest {

    @Test
    public void testIsFixed() {
        Assert.assertTrue(BoardImpl.isFixed('a'));
        Assert.assertTrue(BoardImpl.isFixed('1'));
        Assert.assertFalse(BoardImpl.isFixed('A'));
    }

    @Test
    public void testToFloating() {
        Assert.assertEquals(BoardImpl.toFloating('a'), 'A');
        Assert.assertEquals(BoardImpl.toFloating('A'), 'A');
        Assert.assertEquals(BoardImpl.toFloating('1'), '1');
    }

    @Test
    public void testHeight() {
        Assert.assertEquals(Board.LEVELS[0].getHeight(), 8);
    }

    @Test
    public void testWidth() {
        Assert.assertEquals(Board.LEVELS[0].getWidth(), 12);
    }
}
