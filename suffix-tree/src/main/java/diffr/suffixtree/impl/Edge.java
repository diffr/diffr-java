package diffr.suffixtree.impl;

import com.google.common.collect.Range;
import com.google.common.collect.Ranges;

import javax.annotation.concurrent.Immutable;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Represents an edge between a parent and child {@link Node}s, that holds a certain {@link Range} of elements.
 *
 * @author Jakub D Kozlowski
 * @since 0.1
 */
@Immutable
public final class Edge {

    private final Node parentNode;

    private final Node childNode;

    private final Range<Integer> range;

    /**
     * Default constructor.
     *
     * @param parentNode parent of this {@link Edge}.
     * @param childNode  child of this {@link Edge}.
     * @param range      range of this {@link Edge}.
     *
     * @throws NullPointerException     if any parameter is null.
     * @throws IllegalArgumentException if {@link Range#hasLowerBound()} on {@code range} is false or {@code
     *                                  parentNode} and {@code childNode} are the same..
     */
    public Edge(final Node parentNode, final Node childNode, final Range<Integer> range) {
        this.parentNode = checkNotNull(parentNode);
        this.childNode = checkNotNull(childNode);
        checkArgument(!parentNode.equals(childNode));
        checkNotNull(range);
        checkArgument(range.hasLowerBound());
        this.range = range;
    }

    /**
     * Gets the parent that this edge links to.
     *
     * @return the parent node.
     */
    public Node getParentNode() {
        return parentNode;
    }

    /**
     * Gets the child that this edge links to.
     *
     * @return the child node.
     */
    public Node getChildNode() {
        return childNode;
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
    public static Edge newStartEdge(final Node newParent, final Node newChild, final Range<Integer> oldRange,
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
        final StringBuilder sb = new StringBuilder();
        sb.append("Edge");
        sb.append("{parentNode=").append(parentNode);
        sb.append(", childNode=").append(childNode);
        sb.append(", range=").append(range);
        sb.append('}');
        return sb.toString();
    }
}
