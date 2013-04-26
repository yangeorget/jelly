package net.yangeorget.jelly;

import java.util.Arrays;
import org.testng.Assert;
import org.testng.annotations.Test;

public class JellyTest {
    @Test
    public void testClone() {
        final Jelly jelly = new JellyImpl(5, 5, 'B', Arrays.asList(new Jelly.Position(1, 2),
                                                                   new Jelly.Position(1, 3),
                                                                   new Jelly.Position(2, 2)));
        Assert.assertEquals(jelly.toString(), jelly.clone()
                                                   .toString());
    }

    @Test
    public void testHMove() {
        final Jelly jelly1 = new JellyImpl(5, 5, 'B', Arrays.asList(new Jelly.Position(1, 2),
                                                                    new Jelly.Position(1, 3),
                                                                    new Jelly.Position(2, 2)));
        final Jelly jelly2 = new JellyImpl(5, 5, 'B', Arrays.asList(new Jelly.Position(1, 3),
                                                                    new Jelly.Position(1, 4),
                                                                    new Jelly.Position(2, 3)));
        jelly1.hMove(1);
        Assert.assertEquals(jelly1.toString(), jelly2.toString());
    }

    @Test
    public void testVMove() {
        final Jelly jelly1 = new JellyImpl(5, 5, 'B', Arrays.asList(new Jelly.Position(1, 2),
                                                                    new Jelly.Position(1, 3),
                                                                    new Jelly.Position(2, 2)));
        final Jelly jelly2 = new JellyImpl(5, 5, 'B', Arrays.asList(new Jelly.Position(2, 2),
                                                                    new Jelly.Position(2, 3),
                                                                    new Jelly.Position(3, 2)));
        jelly1.vMove(1);
        Assert.assertEquals(jelly1.toString(), jelly2.toString());
    }


    @Test
    public void testOverlaps1() {
        final Jelly jelly1 = new JellyImpl(5, 5, 'B', Arrays.asList(new Jelly.Position(1, 2),
                                                                    new Jelly.Position(1, 3),
                                                                    new Jelly.Position(2, 2)));
        final Jelly jelly2 = new JellyImpl(5, 5, 'B', Arrays.asList(new Jelly.Position(2, 1),
                                                                    new Jelly.Position(3, 1),
                                                                    new Jelly.Position(2, 2)));
        Assert.assertTrue(jelly1.overlaps(jelly2));
    }

    @Test
    public void testOverlaps2() {
        final Jelly jelly1 = new JellyImpl(5, 5, 'B', Arrays.asList(new Jelly.Position(1, 2),
                                                                    new Jelly.Position(1, 3),
                                                                    new Jelly.Position(2, 2)));
        final Jelly jelly2 = new JellyImpl(5, 5, 'B', Arrays.asList(new Jelly.Position(2, 1),
                                                                    new Jelly.Position(3, 1),
                                                                    new Jelly.Position(2, 3)));
        Assert.assertFalse(jelly1.overlaps(jelly2));
    }

    @Test
    public void testPosition() {
        final Jelly.Position position = new Jelly.Position(3, 2);
        Assert.assertEquals(position.getValue(), 50);
        Assert.assertEquals(position.getI(), 3);
        Assert.assertEquals(position.getJ(), 2);
        Assert.assertEquals(position.toString(), "(3,2)");
    }

    @Test
    public void testPositionHMove() {
        Assert.assertFalse(new Jelly.Position(0, 15).hMove(1, 16));
        Assert.assertTrue(new Jelly.Position(0, 14).hMove(1, 16));
    }

    @Test
    public void testPositionVMove() {
        Assert.assertFalse(new Jelly.Position(5, 15).vMove(1, 6));
        Assert.assertTrue(new Jelly.Position(4, 14).vMove(1, 6));
    }
}
