package net.yangeorget.jelly;

import org.apache.commons.collections.Trie;
import org.apache.commons.collections.trie.PatriciaTrie;
import org.apache.commons.collections.trie.StringKeyAnalyzer;

/**
 * Trie-based StateSet implementation.
 * @author y.georget
 */
public class StateSetTrieImpl
        implements StateSet {
    private final Trie<String, Boolean> trie;

    /**
     * Sole constructor.
     */
    public StateSetTrieImpl() {
        trie = new PatriciaTrie<>(new StringKeyAnalyzer());
    }

    @Override
    public boolean store(final State state) {
        final String ser = state.getSerialization()
                                // .reverse()
                                .toString();
        if (trie.containsKey(ser)) {
            return false;
        } else {
            trie.put(ser, Boolean.TRUE);
            return true;
        }
    }

    @Override
    public int size() {
        return trie.size();
    }
}
