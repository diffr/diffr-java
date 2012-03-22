package diffr.suffixtree;

import com.google.common.collect.Range;

/**
 * {@link SuffixTree} is a sequence of elements structured as a tree of suffixes that allows for quick retrieval of
 * sub sequences.
 *
 * @author Jakub D Kozlowski
 * @since 0.1
 */
public interface SuffixTree<E extends Comparable> {

    /**
     * Interface for traversing the {@link SuffixTree} in order to match suffixes.
     *
     * @since 0.2
     */
    public interface Matcher<E> {

        /**
         * Tries to match the next element in the suffix from the current position in the {@link SuffixTree}.
         *
         * @param element next element to match.
         *
         * @return {@link Matched#YES} if {@code element} was matched, {@link Matched#NO} otherwise.
         *
         * @throws NullPointerException  if {@code element} is null.
         * @throws IllegalStateException if this {@link Matcher} is finished, i.e. there was a previous call to
         *                               {@link #matchNext(Object)} that resulted in {@link Matched#NO}.
         */
        Matched matchNext(final E element);

        /**
         * Checks if this {@link Matcher} has already returned {@link Matched#NO}.
         *
         * @return {@code true} if this {@link Matcher} has not yet returned {@link Matched#NO},
         *         {@code false} otherwise.
         */
        boolean isFinished();

        /**
         * Checks if this {@link Matcher} is still at the root, i.e. it either did not match any elements or it
         * failed to match the first element.
         *
         * @return {@code true} if this {@link Matcher} still points to the root of the {@link SuffixTree},
         *         {@code false} otherwise.
         */
        boolean isRoot();

        /**
         * Gets the index of the last matched element. If this {@link Matcher} is {@link #isFinished()},
         * then this method returns the index of the element that was matched last. Because of the way the
         * {@link SuffixTree} is constructed, the values returned might not be contiguous.
         *
         * @return index of last matched element.
         *
         * @throws IllegalStateException if this {@link Matcher} did not match any elements yet,
         *                               or it failed to match the first attempt, i.e. the call to {@link #matchNext
         *                               (Object)} failed.
         */
        int lastIndex();

        /**
         * Gets a continuous range of elements that were matched so far. If {@link Matcher#isFinished} is {@code
         * false}, the range returned refers to the range of elements matched up to the point that {@link #matchNext
         * (Object)} returned {@link Matched#NO}.
         *
         * @return range of elements that were matched so far.
         *
         * @throws IllegalStateException if this {@link Matcher} did not match any elements yet, or it failed to match
         *                               the first attempt, i.e. the call to {@link #matchNext(Object)} failed.
         */
        Range<Integer> range();
    }

    /**
     * Indicates whether the element was matched.
     *
     * @since 0.2
     */
    public enum Matched {

        YES, NO;

        /**
         * Checks if the element was matched.
         *
         * @return {@code true} if the element was matched, {@code false} otherwise.
         */
        public boolean isMatched() {
            return this == YES;
        }
    }

    /**
     * Gets the {@link Matcher} for this {@link SuffixTree}.
     *
     * @return matcher implementation for this {@link SuffixTree}.
     *
     * @since 0.2
     */
    Matcher<E> matcher();
}
