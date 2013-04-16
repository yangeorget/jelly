package net.yangeorget.jelly;

import java.util.List;

public interface Game {
	List<State> getStates();
	
	int getHeight();
	
	int getWidth() ;

	boolean solve();
}