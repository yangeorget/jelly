package net.yangeorget.jelly;


import org.testng.Assert;
import org.testng.annotations.Test;

@Test(groups = "fast")
public class AbstractSerializerTest {
    @Test
    public void testSerializeBooleanArray1() {
        testSerializeBooleanArray("");
    }

    @Test
    public void testSerializeBooleanArray2() {
        testSerializeBooleanArray("", false, false, false);
    }

    @Test
    public void testSerializeBooleanArray3() {
        testSerializeBooleanArray("1", true);
    }

    @Test
    public void testSerializeBooleanArray4() {
        testSerializeBooleanArray("4", true, false, false);
    }

    @Test
    public void testSerializeBooleanArray5() {
        testSerializeBooleanArray("10", true, false, true, false);
    }

    private void testSerializeBooleanArray(final String s, final boolean... a) {
        final StringBuilder b = new StringBuilder();
        AbstractSerializer.serializeBooleanArray(b, a);
        Assert.assertEquals(b.toString(), s);
    }

    @Test
    public void testSerializeByteArray() {
        testSerializeByteArray("0A0B0C0D0E0F101121", new byte[] { 10, 11, 12, 13, 14, 15, 16, 17, 33 });
    }

    private void testSerializeByteArray(final String s, final byte... a) {
        final StringBuilder b = new StringBuilder();
        AbstractSerializer.serializeByteArray(b, a);
        Assert.assertEquals(b.toString(), s);
    }
}
