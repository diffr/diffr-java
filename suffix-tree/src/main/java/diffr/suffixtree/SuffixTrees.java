package diffr.suffixtree;

import diffr.suffixtree.impl.SuffixTreeImpl;

import java.util.List;

/**
 * Factory method for creating {@link SuffixTree}s.
 *
 * @author Jakub D Kozlowski
 * @since 0.2
 */
public final class SuffixTrees {

    private static final String ERROR_MSG = "This class should not be instantiated";

    /**
     * This class should not be instantiated.
     *
     * @throws UnsupportedOperationException this class should not be instantiated.
     */
    public SuffixTrees() {
        throw new UnsupportedOperationException(ERROR_MSG);
    }

    /**
     * Gets a {@link SuffixTree} for these {@code elements}, optimised for checking for existence of suffixes in
     * {@code elements}.
     *
     * @param elements elements to build the {@link SuffixTree} for.
     * @param <E>      type of elements.
     *
     * @return {@link SuffixTree} for these {@code elements}.
     */
    public static <E extends Comparable> SuffixTree<E> newSuffixTree(final List<E> elements) {
        return SuffixTreeImpl.newSuffixTree(elements);
    }
}
