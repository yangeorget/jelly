package net.yangeorget.jelly;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author y.georget
 */
@Test(groups = "fast")
public abstract class StateSetAbstractTest {
    @Test
    public void testStore() {
        final StateSet set = newStateSet();
        Assert.assertTrue(set.store(new byte[] { 1, 2, 3, Board.SER_DELIM_BYTE, 1, 2, 3 }));
        Assert.assertEquals(set.size(), 1);
        Assert.assertTrue(set.store(new byte[] { 1, 2, 3 }));
        Assert.assertEquals(set.size(), 2);
        Assert.assertFalse(set.store(new byte[] { 1, 2, 3, Board.SER_DELIM_BYTE, 1, 2, 3 }));
        Assert.assertEquals(set.size(), 2);
    }

    abstract StateSet newStateSet();
}
