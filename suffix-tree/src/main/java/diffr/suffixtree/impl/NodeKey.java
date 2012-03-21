package diffr.suffixtree.impl;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Jakub D Kozlowski
 * @since 0.1
 */
public final class NodeKey<E extends Comparable> {

    private final Node parentNode;

    private final E element;

    private final int hashCode;

    public NodeKey(final Node parentNode, final E element) {
        this.parentNode = checkNotNull(parentNode);
        this.element = checkNotNull(element);
        this.hashCode = 31 * parentNode.hashCode() + element.hashCode();
    }

    public static <E extends Comparable<E>> NodeKey<E> lookup(final Node parentNode, final E element) {
        return new NodeKey(parentNode, element);
    }

    public static <E extends Comparable<E>> NodeKey<E>  newNodeKey(final Edge edge, final SuffixTreeImpl<E> suffixTree) {
        return lookup(edge.getParentNode(), suffixTree.getElement(edge.getRange().lowerEndpoint()));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final NodeKey nodeKey = (NodeKey) o;

        if (this.hashCode != nodeKey.hashCode) return false;
        if (!element.equals(nodeKey.element)) return false;
        if (!parentNode.equals(nodeKey.parentNode)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return hashCode;
    }
}
