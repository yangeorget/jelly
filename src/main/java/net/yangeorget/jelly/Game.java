package net.yangeorget.jelly;


public interface Game {
    State solve();

    void explain(State state);
}
