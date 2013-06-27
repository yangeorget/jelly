package net.yangeorget.jelly;

import org.apache.commons.collections.Trie;
import org.apache.commons.collections.trie.PatriciaTrie;
import org.apache.commons.collections.trie.StringKeyAnalyzer;

/**
 * @author y.georget
 */
public class TrieStateSet
        implements StateSet {
    private final Trie<String, Boolean> trie;

    public TrieStateSet() {
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
