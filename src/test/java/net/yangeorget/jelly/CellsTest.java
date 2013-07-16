package net.yangeorget.jelly;


import org.testng.Assert;
import org.testng.annotations.Test;

@Test(groups = "fast")
public class CellsTest {
    @Test
    public void testI0J0() {
        test(0, 0);
    }

    @Test
    public void testI1J1() {
        test(1, 1);
    }

    @Test
    public void testI0JMAX() {
        test(0, Board.MAX_WIDTH - 1);
    }

    @Test
    public void testIMAXJ0() {
        test(Board.MAX_HEIGHT - 1, 0);
    }

    @Test
    public void testIMAXJMAX() {
        test(Board.MAX_HEIGHT - 1, Board.MAX_WIDTH - 1);
    }

    private void test(final int i, final int j) {
        final byte pos = Cells.value(i, j);
        Assert.assertEquals(Cells.getI(pos), i);
        Assert.assertEquals(Cells.getJ(pos), j);
    }
}
