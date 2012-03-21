package diffr.suffixtree.impl;

import javax.annotation.concurrent.Immutable;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Represents either an internal node or the root of the {@link SuffixTreeImpl}.
 *
 * @author Jakub D Kozlowski
 * @since 0.1
 */
@Immutable
public final class Node {

    /**
     * Index of this node. Only for the purposes of {@link #toString()}.
     */
    private final int index;

    /**
     * Default constructor.
     *
     * @param index index of this {@link Node}.
     *
     * @throws IllegalArgumentException if {@code index} is smaller than 0.
     */
    public Node(final int index) {
        checkArgument(index >= 0);
        this.index = index;
    }

    /**
     * Gets the index.
     *
     * @return the index.
     */
    public int getIndex() {
        return index;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Node");
        sb.append("{index=").append(index);
        sb.append('}');
        return sb.toString();
    }
}
