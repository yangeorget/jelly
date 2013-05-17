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

    @Test
    public void testGetJellies() {
        final Jelly[] jellies = Board.LEVELS[0].clone()
                                               .extractJellies();
        Assert.assertEquals(jellies.length, 6);
        Assert.assertEquals(jellies[0].getColor(), 'P');
        Assert.assertEquals(jellies[1].getColor(), 'G');
        Assert.assertEquals(jellies[2].getColor(), 'P');
        Assert.assertEquals(jellies[3].getColor(), 'B');
        Assert.assertEquals(jellies[4].getColor(), 'B');
        Assert.assertEquals(jellies[5].getColor(), 'G');
    }
}
