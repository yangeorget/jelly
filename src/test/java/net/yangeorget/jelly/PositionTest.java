package net.yangeorget.jelly;

import org.testng.Assert;
import org.testng.annotations.Test;

public class PositionTest {	
	@Test
	public void testPosition() {
		Position position = new Position(3, 2);
		Assert.assertEquals(position.getValue(), 50);
		Assert.assertEquals(position.getI(), 3);
		Assert.assertEquals(position.getJ(), 2);
		Assert.assertEquals(position.toString(), "(3,2)");
	}
	
	@Test
	public void testPositionMove() {
		Assert.assertFalse(new Position(0, 15).move(0, 1, 4, 16));
		Assert.assertTrue(new Position(0, 14).move(0, 1, 4, 16));
	}
}