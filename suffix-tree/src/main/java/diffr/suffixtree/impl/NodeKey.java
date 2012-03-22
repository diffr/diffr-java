package diffr.suffixtree.impl;

import com.google.common.base.Objects;
import diffr.suffixtree.SuffixTree;
import javolution.util.FastCollection.Record;

import javax.annotation.concurrent.Immutable;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Represents a {@code [parent, element]} pair that can be used as a key to a map of {@link Edge}s.
 *
 * @param <E> type of element this {@link NodeKey} points to.
 *
 * @author Jakub D Kozlowski
 * @since 0.1
 */
@Immutable
public final class NodeKey<E extends Comparable> {

    private final Record parent;

    private final E element;

    private final int hashCode;

    /**
     * Default constructor.
     *
     * @param parent  parent that this {@link NodeKey} points to.
     * @param element element for this {@link NodeKey}.
     *
     * @throws NullPointerException if any parameter is null.
     */
    private NodeKey(final Record parent, final E element) {
        this.parent = checkNotNull(parent);
        this.element = checkNotNull(element);
        this.hashCode = Objects.hashCode(parent, element);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;

        final NodeKey that = (NodeKey) o;

        if (!element.equals(that.element)) return false;
        if (parent != that.parent) return false;

        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return hashCode;
    }

    /**
     * Gets an instance of {@link NodeKey} for this {@code parent} and {@code element.}
     *
     * @param parent  parent of this {@link NodeKey}.
     * @param element element of this {@link NodeKey}.
     * @param <E>     type of this {@link NodeKey}.
     *
     * @return new instance of {@link NodeKey}.
     *
     * @throws NullPointerException if any parameter is null.
     */
    public static <E extends Comparable> NodeKey<E> lookup(final Record parent, final E element) {
        return new NodeKey<E>(parent, element);
    }

    /**
     * Gets an instance of {@link NodeKey} for the parent of this {@code edge} and the element pointed to by the
     * lower boundary of this {@code edge}.
     *
     * @param edge       edge for the parent of which this {@link NodeKey} will be created.
     * @param suffixTree parent {@link SuffixTree} to lookup the element.
     * @param <E>        type of the {@link SuffixTree}.
     *
     * @return instance of {@link NodeKey} for this {@code edge}.
     *
     * @throws NullPointerException      if any parameter is null.
     * @throws IndexOutOfBoundsException if lower boundary of this {@code edge} is out of range,
     *                                   of this {@code suffixTree}, i.e. {@code index < 0 || index >= size())}.
     */
    public static <E extends Comparable<E>> NodeKey<E> newNodeKey(final Edge edge,
                                                                  final SuffixTreeImpl<E> suffixTree) {
        return new NodeKey(checkNotNull(edge).getParent(),
                           checkNotNull(suffixTree.getElement(edge.getRange().lowerEndpoint())));
    }
}
