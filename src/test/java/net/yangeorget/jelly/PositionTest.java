package net.yangeorget.jelly;

import org.testng.Assert;
import org.testng.annotations.Test;

public class PositionTest {
	@Test
	public void testPositionValue() {
		Assert.assertEquals(new Position(1,1).intValue(), 17);
	}
	
	@Test
	public void testPositionString() {
		Assert.assertEquals(new Position(1,1).toString(), "(1,1)");
	}
}