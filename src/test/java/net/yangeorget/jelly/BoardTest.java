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
    public void testGet() {
        Assert.assertEquals(Board.LEVELS[0].get(0, 0), ' ');
        Assert.assertEquals(Board.LEVELS[0].get(0, 1), ' ');
        Assert.assertEquals(Board.LEVELS[0].get(1, 0), ' ');
        Assert.assertEquals(Board.LEVELS[0].get(1, 1), ' ');
        Assert.assertEquals(Board.LEVELS[0].get(7, 0), '1');
        Assert.assertEquals(Board.LEVELS[0].get(7, 1), 'B');
        Assert.assertEquals(Board.LEVELS[0].get(7, 2), '2');
        Assert.assertEquals(Board.LEVELS[0].get(7, 3), '2');
        Assert.assertEquals(Board.LEVELS[0].get(7, 4), '2');
        Assert.assertEquals(Board.LEVELS[0].get(7, 5), 'G');
        Assert.assertEquals(Board.LEVELS[0].get(7, 6), ' ');
    }

    @Test
    public void testGetJellies() {
        final Jelly[] jellies = Board.LEVELS[0].getJellies();
        Assert.assertEquals(jellies.length, 10);
        Assert.assertEquals(jellies[0].getColor(), 'P');
        Assert.assertFalse(jellies[0].isFixed());
        Assert.assertEquals(jellies[1].getColor(), '0');
        Assert.assertTrue(jellies[1].isFixed());
        Assert.assertEquals(jellies[2].getColor(), 'G');
        Assert.assertFalse(jellies[2].isFixed());
        Assert.assertEquals(jellies[3].getColor(), 'P');
        Assert.assertFalse(jellies[3].isFixed());
        Assert.assertEquals(jellies[4].getColor(), 'B');
        Assert.assertFalse(jellies[4].isFixed());
        Assert.assertEquals(jellies[5].getColor(), '1');
        Assert.assertTrue(jellies[5].isFixed());
        Assert.assertEquals(jellies[6].getColor(), 'B');
        Assert.assertFalse(jellies[6].isFixed());
        Assert.assertEquals(jellies[7].getColor(), '2');
        Assert.assertTrue(jellies[7].isFixed());
        Assert.assertEquals(jellies[8].getColor(), 'G');
        Assert.assertFalse(jellies[8].isFixed());
        Assert.assertEquals(jellies[9].getColor(), '3');
        Assert.assertTrue(jellies[9].isFixed());
    }
}
