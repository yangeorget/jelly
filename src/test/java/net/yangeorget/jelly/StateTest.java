package net.yangeorget.jelly;

import org.testng.Assert;
import org.testng.annotations.Test;

public class StateTest {
	@Test
	public void testClone() {
		State state = new Game(Boards.BOARD1).getStates().get(0);
		Assert.assertEquals(state.toString(), state.clone().toString());
	}
}