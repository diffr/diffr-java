package diffr.suffixtree.impl;

import com.google.common.base.Objects;
import com.google.common.base.Objects.ToStringHelper;
import com.google.common.collect.Range;
import com.google.common.collect.Ranges;
import diffr.util.ListIterators;
import javolution.util.FastCollection.Record;

import java.util.ListIterator;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Represents an edge between a parent and child nodes, that points to a certain {@link Range} of elements:
 * the ranges are inclusive on both sides, e.g. {@code [1, 3]} indicates that the {@link Edge} refers to elements at
 * indexes {@code 1, 2, 3}, conversely {@code [1, 1]} indicates that the {@link Edge} refers to just element at index
 * {@code 1}. Ranges can also be of type {@code [a,+∞]}, which indicates that the edge refers to all the elements
 * from index {@code a} onwards.
 *
 * @author Jakub D Kozlowski
 * @since 0.1
 */
public final class Edge {

    private final Record parent;

    private final Record child;

    private final Range<Integer> range;

    /**
     * Default constructor.
     *
     * @param parent parent of this {@link Edge}.
     * @param child  child of this {@link Edge}.
     * @param range  range of this {@link Edge}.
     *
     * @throws NullPointerException     if any parameter is null.
     * @throws IllegalArgumentException if {@link Range#hasLowerBound()} on {@code range} is false,
     *                                  {@code parent} and {@code child} are the same or the lower endpoint
     *                                  of the {@code range} is negative.
     */
    public Edge(final Record parent, final Record child, final Range<Integer> range) {
        this.parent = checkNotNull(parent);
        this.child = checkNotNull(child);
        checkArgument(!parent.equals(child));
        checkNotNull(range);
        checkArgument(range.hasLowerBound());
        checkArgument(range.lowerEndpoint() >= 0);
        this.range = range;
    }

    /**
     * Gets the parent that this edge links to.
     *
     * @return the parent.
     */
    public Record getParent() {
        return parent;
    }

    /**
     * Gets the child that this edge links to.
     *
     * @return the child.
     */
    public Record getChild() {
        return child;
    }

    /**
     * Gets the range of elements that this node holds.
     *
     * @return range of elements.
     */
    public Range<Integer> getRange() {
        return range;
    }

    /**
     * Gets an iterator for the elements pointed to by this {@link Edge}.
     *
     * @param suffixTree parent of this {@link Edge}.
     * @param <E>        type of the elements.
     *
     * @return iterator for the elements of this {@link Edge}.
     *
     * @throws NullPointerException     if {@code suffixTree} is null.
     * @throws IllegalArgumentException if this edge has an upper boundary and the upper boundary exceeds the size of
     *                                  the list of elements of this {@code suffixTree}.
     */
    public <E extends Comparable> ListIterator<E> iterator(final SuffixTreeImpl<E> suffixTree) {
        checkNotNull(suffixTree);
        checkArgument(!range.hasUpperBound() || range.upperEndpoint() < suffixTree.getElements().size());
        return range.hasUpperBound() ?
                ListIterators.limit(suffixTree.elementsListIterator(range.lowerEndpoint()),
                                    range.upperEndpoint() - range.lowerEndpoint() + 1) :
                suffixTree.elementsListIterator(range.lowerEndpoint());
    }

    /**
     * Gets an edge between {@code newParent} and {@code newChild} that starts at {@code newStart} and has the same
     * upper bound as {@code oldRange}.
     *
     * @param newParent parent for the edge.
     * @param newChild  child for the edge.
     * @param oldRange  range which upper bound will be the upper bound of the new edge.
     * @param newStart  start of the edge.
     *
     * @return edge between {@code newParent} and {@code newChild} that starts at {@code newStart} and has the same
     *         upper bound as {@code oldRange}.
     *
     * @throws NullPointerException     if any parameter is null.
     * @throws IllegalArgumentException if {@code newStart} is negative or it is greater than the upper bound of
     *                                  {@code oldRange}.
     */
    public static Edge newStartEdge(final Record newParent, final Record newChild, final Range<Integer> oldRange,
                                    final int newStart) {

        checkNotNull(newParent);
        checkNotNull(newChild);
        checkNotNull(oldRange);
        checkArgument(newStart >= 0);

        return oldRange.hasUpperBound() ?
                new Edge(newParent, newChild, Ranges.closed(newStart, oldRange.upperEndpoint())) :
                new Edge(newParent, newChild, Ranges.atLeast(newStart));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        final ToStringHelper toString = Objects.toStringHelper(this);
        toString.add("parent", parent);
        toString.add("child", child).add("range", range);
        return toString.toString();
    }
}
