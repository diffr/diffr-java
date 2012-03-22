package diffr.suffixtree.impl;

import com.google.common.base.Optional;
import com.google.common.collect.Range;
import com.google.common.collect.Ranges;
import diffr.suffixtree.SuffixTree;
import diffr.suffixtree.SuffixTree.Matched;
import diffr.suffixtree.SuffixTree.Matcher;
import javolution.util.FastCollection.Record;

import java.util.ListIterator;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

/**
 * {@link Matcher} implementation for {@link SuffixTreeImpl}.
 *
 * @author Jakub D Kozlowski
 * @since 0.2
 */
public class MatcherImpl<E extends Comparable> implements Matcher<E> {

    private final SuffixTreeImpl<E> suffixTree;

    private Optional<Edge> curEdge;

    private Optional<ListIterator<E>> curEdgeIterator;

    private boolean finished;

    private int matched;

    /**
     * Default constructor.
     *
     * @param suffixTree {@link SuffixTree} to traverse.
     *
     * @throws NullPointerException if {@code suffixTree} is null.
     */
    public MatcherImpl(final SuffixTreeImpl<E> suffixTree) {
        this.suffixTree = checkNotNull(suffixTree);
        this.curEdge = Optional.absent();
        this.curEdgeIterator = Optional.absent();
        this.finished = false;
        this.matched = 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Matched matchNext(final E element) {

        checkNotNull(element);
        checkState(!finished);

        // We are at root
        if (!curEdge.isPresent()) {
            final Optional<Edge> edgeLookup = suffixTree.getEdge(suffixTree.getRoot(), element);
            if (!edgeLookup.isPresent()) {
                return notMatched();
            }

            curEdge = edgeLookup;
            curEdgeIterator = Optional.of(curEdge.get().iterator(suffixTree));
            curEdgeIterator.get().next();
            matched++;
            return Matched.YES;
        }

        checkStarted();
        if (!curEdgeIterator.get().hasNext()) {

            final Optional<Edge> edgeLookup = suffixTree.getEdge(curEdge.get().getChild(), element);
            if (!edgeLookup.isPresent()) {
                return notMatched();
            }

            curEdge = edgeLookup;
            curEdgeIterator = Optional.of(curEdge.get().iterator(suffixTree));
            curEdgeIterator.get().next();
            matched++;
            return Matched.YES;
        }

        final E edgeElement = curEdgeIterator.get().next();
        if (edgeElement.equals(element)) {
            matched++;
            return Matched.YES;
        }

        curEdgeIterator.get().previous();
        return notMatched();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isFinished() {
        return finished;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isRoot() {
        return !curEdge.isPresent();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int lastIndex() {
        checkStarted();
        checkState(curEdge.isPresent());
        return curEdgeIterator.get().previousIndex();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Range<Integer> range() {
        checkStarted();
        checkState(curEdge.isPresent());
        return Ranges.closed(curEdgeIterator.get().previousIndex() - matched + 1,
                             curEdgeIterator.get().previousIndex());
    }

    /**
     * Checks if the current edge has next element.
     *
     * @return {@code true} if the current edge has next element.
     */
    public boolean edgeHasNext() {
        checkStarted();
        return curEdgeIterator.isPresent() ? curEdgeIterator.get().hasNext() : false;
    }

    /**
     * Gets the last edge matched.
     *
     * @return last matched edge.
     *
     * @throws IllegalStateException if this {@link MatcherImpl} has not yet matched any elements.
     */
    public Edge lastEdge() {
        checkStarted();
        if (curEdge.isPresent()) {
            return curEdge.get();
        }
        throw new IllegalStateException();
    }

    /**
     * Gets the last matched node.
     *
     * @return last matched node or root node if the attempt to match at root was unsuccessful.
     *
     * @throws IllegalStateException if this {@link MatcherImpl} has not yet matched any elements.
     */
    public Record lastNode() {
        checkStarted();
        if (curEdge.isPresent()) {
            return curEdge.get().getChild();
        }
        return suffixTree.getRoot();
    }

    /**
     * Checks if this {@link MatcherImpl} has already matched an element.
     *
     * @throws IllegalStateException if this {@link MatcherImpl} has not yet matched any elements.
     */
    private void checkStarted() {

        if (!finished) {
            checkState(curEdge.isPresent());
            checkState(curEdgeIterator.isPresent());
        }
    }

    /**
     * Sets {@code finished} to {@code true} and returns {@link Matched#NO}.
     *
     * @return {@link Matched#NO}.
     */
    private Matched notMatched() {
        finished = true;
        return Matched.NO;
    }
}
