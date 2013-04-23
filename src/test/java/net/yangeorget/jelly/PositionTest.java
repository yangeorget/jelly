package net.yangeorget.jelly;

import org.testng.Assert;
import org.testng.annotations.Test;

public class PositionTest {
    @Test
    public void testPosition() {
        final Jelly.Position position = new Jelly.Position(3, 2);
        Assert.assertEquals(position.getValue(), 50);
        Assert.assertEquals(position.getI(), 3);
        Assert.assertEquals(position.getJ(), 2);
        Assert.assertEquals(position.toString(), "(3,2)");
    }

    @Test
    public void testHMove() {
        Assert.assertFalse(new Jelly.Position(0, 15).hMove(1, 16));
        Assert.assertTrue(new Jelly.Position(0, 14).hMove(1, 16));
    }

    @Test
    public void testVMove() {
        Assert.assertFalse(new Jelly.Position(5, 15).vMove(1, 6));
        Assert.assertTrue(new Jelly.Position(4, 14).vMove(1, 6));
    }
}
