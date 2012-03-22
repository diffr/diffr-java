package diffr.suffixtree.impl;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Optional;
import com.google.common.collect.Range;
import com.google.common.collect.Ranges;
import diffr.suffixtree.SuffixTree;
import javolution.util.FastCollection.Record;
import javolution.util.FastMap;
import javolution.util.FastTable;
import javolution.util.Index;

import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Default {@link SuffixTreeImpl} implementation.
 *
 * @author Jakub D Kozlowski
 * @since 0.1
 */
public final class SuffixTreeImpl<E extends Comparable> implements SuffixTree<E> {

    private final FastTable<E> elements;

    private final Map<NodeKey<E>, Edge> edges;

    private final Record root;

    private Record curRecord;

    /**
     * Default constructor.
     *
     * @param elements elements that this {@link SuffixTreeImpl} will be built for.
     *
     * @throws NullPointerException if {@code elements} is null.
     */
    private SuffixTreeImpl(final List<E> elements) {
        checkNotNull(elements);
        this.elements = FastTable.newInstance();
        this.elements.addAll(elements);
        this.curRecord = Index.ZERO.getNext();
        this.root = this.curRecord;
        this.edges = FastMap.newInstance();
    }

    /**
     * Adds a child to the {@code parent}.
     *
     * @param range range of the edge that will be created.
     *
     * @return the new node created.
     *
     * @throws NullPointerException     if any parameter is null.
     * @throws IllegalArgumentException if {@code range} does not have a lower bound.
     * @since 0.2
     */
    private Record addChild(final Record parent, final Range<Integer> range) {
        checkNotNull(parent);
        checkNotNull(range);
        checkArgument(range.hasLowerBound());
        this.curRecord = curRecord.getNext();
        final Edge newEdge = new Edge(parent, curRecord, range);
        addEdge(newEdge);
        return curRecord;
    }

    /**
     * Removes this {@code oldEdge} and instead creates:
     *
     * <ul>
     * <li>a {@code branchNode} with a {@code branchEdge} {@code [oldEdge.parentNode, branchNode,
     * [oldNode.range.lowerBound(), lastMatched]}</li>
     * <li>an {@code leftEdge} {@code [branchNode, oldEdge.childNode, [lastMatched, oldEdge.range.upperBound]]}</li>
     * <li>a {@code splitNode} with a {@code splitEdge} {@code [branchNode, splitNode, [firstNotMatchedSuffixIndex,
     * +∞]}</li>
     * </ul>
     *
     * @param oldEdge                    edge that will be removed.
     * @param lastMatchedEdgeIndex       index of the last element matched in the edge.
     * @param firstNotMatchedSuffixIndex index of the first element not matched from the suffix.
     *
     * @throws NullPointerException if {@code oldEdge} is null or it does not exists in this {@link SuffixTreeImpl}.
     * @since 0.2
     */
    private void splitEdge(final Edge oldEdge, final int lastMatchedEdgeIndex, final int firstNotMatchedSuffixIndex) {

        checkNotNull(edges.remove(NodeKey.newNodeKey(checkNotNull(oldEdge), this)));

        // {@code [oldEdge.parentNode, branch, [oldNode.range.lowerBound(), lastMatched]}
        final Record branch
                = addChild(oldEdge.getParent(),
                           Ranges.closed(oldEdge.getRange().lowerEndpoint(), lastMatchedEdgeIndex));

        // {@code [branch, oldEdge.childNode, [lastMatchedEdgeIndex + 1, oldEdge.range.lowerBound]]}
        final Edge leftEdge = Edge.newStartEdge(branch, oldEdge.getChild(), oldEdge.getRange(),
                                                lastMatchedEdgeIndex + 1);
        addEdge(leftEdge);

        // {@code [branch, splitNode, [firstNotMatchedSuffixIndex, +∞]}
        addChild(branch, Ranges.atLeast(firstNotMatchedSuffixIndex));
    }

    /**
     * Adds this {@code edge}. No check is performed whether the {@code parentNode} and {@code childNode} is present
     * in this {@link SuffixTreeImpl}.
     *
     * @param edge new edge.
     *
     * @throws NullPointerException     if {@code edge} is null.
     * @throws IllegalArgumentException if this {@link SuffixTreeImpl} already contains this {@code edge}.
     * @since 0.2
     */
    @VisibleForTesting
    void addEdge(final Edge edge) {
        final NodeKey<E> nodeKey = NodeKey.newNodeKey(checkNotNull(edge), this);
        checkArgument(!edges.containsKey(nodeKey));
        edges.put(nodeKey, edge);
    }

    /**
     * Gets the edge from this {@code parent} to this {@code element}.
     *
     * @param parent  parent of the {@link Edge} to get.
     * @param element the first element of the {@code Edge} to get.
     *
     * @return edge from this {@code parent} to this {@code element}.
     *
     * @throws NullPointerException if any parameter is null.
     */
    @VisibleForTesting
    Optional<Edge> getEdge(final Record parent, final E element) {
        return Optional.fromNullable(edges.get(NodeKey.lookup(checkNotNull(parent), checkNotNull(element))));
    }

    /**
     * Gets the root node of this {@link SuffixTree}.
     *
     * @return root of this {@link SuffixTree}.
     */
    @VisibleForTesting
    Record getRoot() {
        return root;
    }

    /**
     * Gets a {@link ListIterator} of elements in this {@link SuffixTreeImpl}, starting from {@code index}. The
     * returned iterator supports all the mutation operations like {@link ListIterator#remove()} or {@link
     * ListIterator#set(Object)}, however it should only be used to retrieve elements.
     *
     * @param index starting index for this {@link ListIterator}.
     *
     * @return {@link ListIterator} of elements in this {@link SuffixTreeImpl}, starting from {@code index}.
     *
     * @since 0.2
     */
    ListIterator<E> elementsListIterator(final int index) {
        return elements.listIterator(index);
    }

    /**
     * Gets the element at this {@code element}.
     *
     * @param index index of the element.
     *
     * @return element at this {@code element}.
     */
    E getElement(int index) {
        return elements.get(index);
    }

    /**
     * Returns a read-only view of elements in this {@link SuffixTree}.
     *
     * @return read-only view of elements in this {@link SuffixTree}.
     */
    List<E> getElements() {
        return elements.unmodifiable();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MatcherImpl<E> matcher() {
        return new MatcherImpl<E>(this);
    }

    /**
     * Factory for {@link SuffixTreeImpl}s.
     *
     * @param elements elements to build the {@link SuffixTreeImpl} for.
     *
     * @return new {@link SuffixTreeImpl} for {@code elements}.
     *
     * @throws NullPointerException if {@code elements} is null.
     */
    public static <E extends Comparable<E>> SuffixTreeImpl<E> newSuffixTree(final List<E> elements) {

        checkNotNull(elements);

        final SuffixTreeImpl<E> suffixTree = new SuffixTreeImpl<E>(elements);

        final ListIterator<E> suffixes = elements.listIterator();

        while (suffixes.hasNext()) {

            suffixes.next();

            final MatcherImpl<E> suffixMatcher = suffixTree.matcher();
            final ListIterator<E> suffixIterator = elements.listIterator(suffixes.previousIndex());

            while (suffixIterator.hasNext()) {

                if (!suffixMatcher.matchNext(suffixIterator.next()).isMatched()) {
                    if (suffixMatcher.edgeHasNext()) {
                        suffixTree.splitEdge(suffixMatcher.lastEdge(),
                                             suffixMatcher.lastIndex(),
                                             suffixIterator.previousIndex());
                    }
                    else {
                        suffixTree.addChild(suffixMatcher.lastNode(),
                                            Ranges.atLeast(suffixIterator.previousIndex()));

                    }
                    break;
                }
            }
        }

        return suffixTree;
    }
}
