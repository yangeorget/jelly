package net.yangeorget.jelly;

import org.testng.Assert;
import org.testng.annotations.Test;

public class StateTest {

	@Test
	public void testState1() {
		Assert.assertEquals(new State(new String[] {
				"BBBB",
				"  BY",
				"  YY",
				"YB  "				
		}).getJellies().size(), 4);
	}
	
	@Test
	public void testState2() {
		Assert.assertEquals(new State(new String[] {
				"BBBB",
				"  BB",
				"  BB",
				"BB  "				
		}).getJellies().size(), 2);
	}
	
	@Test
	public void testState3() {
		Assert.assertEquals(new State(new String[] {
				"BYBB",
				"  GG",
				"  BB",
				" B  "				
		}).getJellies().size(), 6);
	}
	
	@Test
	public void testStateBoard1() {
		Assert.assertEquals(new State(Boards.BOARD1).getJellies().size(), 10);
	}
}