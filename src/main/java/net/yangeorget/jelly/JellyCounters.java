package net.yangeorget.jelly;

/**
 * Counters that remain constant during resolution.
 * @author y.georget
 */
public interface JellyCounters {
    /**
     * Returns the number of distinct jelly colors.
     * @return an int
     */
    int getJellyColorNb();

    /**
     * Returns the number of jelly positions.
     * @return an int
     */
    int getJellyPositionNb();
}
