package net.yangeorget.jelly;

import org.testng.Assert;
import org.testng.annotations.Test;

public class JellyTest {
    @Test
    public void testClone() {
        final Jelly jelly = new JellyImpl(5, 5, 'B', (byte) 0x12, (byte) 0x13, (byte) 0x22);
        Assert.assertEquals(jelly.toString(), jelly.clone()
                                                   .toString());
    }

    @Test
    public void testHMove() {
        final Jelly jelly1 = new JellyImpl(5, 5, 'B', (byte) 0x12, (byte) 0x13, (byte) 0x22);
        final Jelly jelly2 = new JellyImpl(5, 5, 'B', (byte) 0x13, (byte) 0x14, (byte) 0x23);
        jelly1.hMove(1);
        Assert.assertEquals(jelly1.toString(), jelly2.toString());
    }

    @Test
    public void testVMove() {
        final Jelly jelly1 = new JellyImpl(5, 5, 'B', (byte) 0x12, (byte) 0x13, (byte) 0x22);
        final Jelly jelly2 = new JellyImpl(5, 5, 'B', (byte) 0x22, (byte) 0x23, (byte) 0x32);
        jelly1.vMove(1);
        Assert.assertEquals(jelly1.toString(), jelly2.toString());
    }


    @Test
    public void testOverlaps1() {
        final Jelly jelly1 = new JellyImpl(5, 5, 'B', (byte) 0x12, (byte) 0x13, (byte) 0x22);
        final Jelly jelly2 = new JellyImpl(5, 5, 'B', (byte) 0x21, (byte) 0x31, (byte) 0x22);
        Assert.assertTrue(jelly1.overlaps(jelly2));
    }

    @Test
    public void testOverlaps2() {
        final Jelly jelly1 = new JellyImpl(5, 5, 'B', (byte) 0x12, (byte) 0x13, (byte) 0x22);
        final Jelly jelly2 = new JellyImpl(5, 5, 'B', (byte) 0x21, (byte) 0x31, (byte) 0x23);
        Assert.assertFalse(jelly1.overlaps(jelly2));
    }
}
