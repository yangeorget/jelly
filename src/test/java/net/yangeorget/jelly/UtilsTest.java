package net.yangeorget.jelly;


import org.testng.Assert;
import org.testng.annotations.Test;

@Test(groups = "fast")
public class UtilsTest {
    @Test
    public void testAsByte1() {
        testAsByte(0);
    }

    @Test
    public void testAsByte2() {
        testAsByte(0, false, false, false);
    }

    @Test
    public void testAsByte3() {
        testAsByte(1, true);
    }

    @Test
    public void testAsByte4() {
        testAsByte(4, true, false, false);
    }

    @Test
    public void testAsByte5() {
        testAsByte(10, true, false, true, false);
    }

    private void testAsByte(final int b, final boolean... a) {
        Assert.assertEquals(Utils.asByte(a), b);
    }
}
