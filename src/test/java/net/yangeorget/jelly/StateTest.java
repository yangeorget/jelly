package net.yangeorget.jelly;

import org.testng.Assert;
import org.testng.annotations.Test;

public class StateTest {
	@Test
	public void testState1() {
		testState(new String[] {
				"BBBB",
				"  BY",
				"  YY",
				"YB  "}, 4);
	}

	@Test
	public void testState2() {
		testState(new String[] {
				"BBBB",
				"  BB",
				"  BB",
				"BB  "				
		}, 2);
	}
	
	@Test
	public void testState3() {
		testState(new String[] {
				"BYBB",
				"  GG",
				"  BB",
				" B  "				
		}, 6);
	}
	
	@Test
	public void testStateBoard1() {
		testState(Boards.BOARD1, 10);
	}
	
	private void testState(String[] board, int size) {
		Assert.assertEquals(new Game(board).getStates().get(0).getJellies().size(), size);
	}
}