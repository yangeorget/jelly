package net.yangeorget.jelly;

import java.util.Arrays;
import org.testng.Assert;
import org.testng.annotations.Test;

public class JellyTest {
    private static Frame F5_5 = new Frame() {
        @Override
        public int getHeight() {
            return 5;
        }

        @Override
        public int getWidth() {
            return 5;
        }
    };

    @Test
    public void testClone() {
        final Jelly jelly = new JellyImpl(F5_5, 'B', Arrays.asList(new Jelly.Position(1, 2),
                                                                   new Jelly.Position(1, 3),
                                                                   new Jelly.Position(2, 2)));
        Assert.assertEquals(jelly.toString(), jelly.clone()
                                                   .toString());
    }

    @Test
    public void testContains() {
        final Jelly jelly = new JellyImpl(F5_5, 'B', Arrays.asList(new Jelly.Position(1, 2),
                                                                   new Jelly.Position(1, 3),
                                                                   new Jelly.Position(2, 2)));
        Assert.assertTrue(jelly.contains(new Jelly.Position(1, 3)));
        Assert.assertTrue(jelly.contains(new Jelly.Position(1, 2)));
        Assert.assertTrue(jelly.contains(new Jelly.Position(2, 2)));
        Assert.assertFalse(jelly.contains(new Jelly.Position(1, 1)));
    }

    @Test
    public void testHMove() {
        final Jelly jelly1 = new JellyImpl(F5_5, 'B', Arrays.asList(new Jelly.Position(1, 2),
                                                                    new Jelly.Position(1, 3),
                                                                    new Jelly.Position(2, 2)));
        final Jelly jelly2 = new JellyImpl(F5_5, 'B', Arrays.asList(new Jelly.Position(1, 3),
                                                                    new Jelly.Position(1, 4),
                                                                    new Jelly.Position(2, 3)));
        jelly1.hMove(1);
        Assert.assertEquals(jelly1.toString(), jelly2.toString());
    }

    @Test
    public void testVMove() {
        final Jelly jelly1 = new JellyImpl(F5_5, 'B', Arrays.asList(new Jelly.Position(1, 2),
                                                                    new Jelly.Position(1, 3),
                                                                    new Jelly.Position(2, 2)));
        final Jelly jelly2 = new JellyImpl(F5_5, 'B', Arrays.asList(new Jelly.Position(2, 2),
                                                                    new Jelly.Position(2, 3),
                                                                    new Jelly.Position(3, 2)));
        jelly1.vMove(1);
        Assert.assertEquals(jelly1.toString(), jelly2.toString());
    }


    @Test
    public void testOverlaps1() {
        final Jelly jelly1 = new JellyImpl(F5_5, 'B', Arrays.asList(new Jelly.Position(1, 2),
                                                                    new Jelly.Position(1, 3),
                                                                    new Jelly.Position(2, 2)));
        final Jelly jelly2 = new JellyImpl(F5_5, 'B', Arrays.asList(new Jelly.Position(2, 1),
                                                                    new Jelly.Position(3, 1),
                                                                    new Jelly.Position(2, 2)));
        Assert.assertTrue(jelly1.overlaps(jelly2));
    }

    @Test
    public void testOverlaps2() {
        final Jelly jelly1 = new JellyImpl(F5_5, 'B', Arrays.asList(new Jelly.Position(1, 2),
                                                                    new Jelly.Position(1, 3),
                                                                    new Jelly.Position(2, 2)));
        final Jelly jelly2 = new JellyImpl(F5_5, 'B', Arrays.asList(new Jelly.Position(2, 1),
                                                                    new Jelly.Position(3, 1),
                                                                    new Jelly.Position(2, 3)));
        Assert.assertFalse(jelly1.overlaps(jelly2));
    }
}
