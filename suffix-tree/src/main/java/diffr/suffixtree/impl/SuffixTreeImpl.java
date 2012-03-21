package diffr.suffixtree.impl;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Optional;
import com.google.common.collect.Ranges;
import diffr.suffixtree.SuffixTree;
import javolution.util.FastList;
import javolution.util.FastMap;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
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

    private final List<E> elements;

    private final Map<NodeKey<E>, Edge> edges;

    private final Node root;

    private final List<Node> nodes;

    public SuffixTreeImpl(final List<E> elements) {
        checkNotNull(elements);
        this.elements = FastList.newInstance();
        this.elements.addAll(elements);
        this.nodes = FastList.newInstance();
        this.root = new Node(nodes.size());
        this.nodes.add(this.root);
        this.edges = FastMap.newInstance();
    }

    public Node newNode() {
        final Node node = new Node(nodes.size());
        nodes.add(node);
        return node;
    }

    public Optional<Edge> getEdge(final NodeKey<E> nodeKey) {
        return Optional.fromNullable(edges.get(nodeKey));
    }

    public Collection<Edge> getEdges() {
        return Collections.unmodifiableCollection(edges.values());
    }

    public void addEdge(final NodeKey<E> nodeKey, final Edge edge) {
        checkArgument(!edges.containsValue(nodeKey));
        edges.put(nodeKey, edge);
    }

    public E getElement(int index) {
        return elements.get(index);
    }

    public Node getRoot() {
        return root;
    }

    public List<E> getElements() {
        return Collections.unmodifiableList(elements);
    }

    public void removeEdge(final Edge edge) {
        checkNotNull(edges.remove(NodeKey.lookup(edge.getParentNode(), elements.get(edge.getRange().lowerEndpoint()))));
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

        final SuffixTreeImpl<E> suffixTree = new SuffixTreeImpl<E>(elements);

        int curIndex = 0;
        while (elements.size() > curIndex) {

            final E e = elements.get(curIndex);
            final NodeKey<E> lookupKey = NodeKey.lookup(suffixTree.getRoot(), e);
            final Optional<Edge> lookup = suffixTree.getEdge(lookupKey);

            if (lookup.isPresent()) {
                searchEdge(elements, suffixTree, curIndex, lookup);
            }
            else {
                // There isn't a matching edge for out current element, so we just add a new edge/node pair.
                final Node newNode = suffixTree.newNode();
                final Edge newEdge = new Edge(suffixTree.getRoot(), newNode, Ranges.atLeast(curIndex));
                suffixTree.addEdge(NodeKey.newNodeKey(newEdge, suffixTree), newEdge);
            }

            curIndex++;
        }

        return suffixTree;
    }

    @VisibleForTesting
    static <E extends Comparable<E>> void searchEdge(List<E> elements, SuffixTreeImpl<E> suffixTree,
                                                     final int curIndex, Optional<Edge> startEdge) {

        Edge curEdge = startEdge.get();

        // We know that the first elements match, because the lookup was successful.
        int edgeCur = curEdge.getRange().lowerEndpoint() + 1;
        int indexCur = curIndex + 1;

        // Let's check the other characters
        while (indexCur < elements.size()) {

            final E e1 = elements.get(indexCur);

            // Check if we are at the end of the edge
            if (!curEdge.getRange().apply(edgeCur)) {

                final Optional<Edge> newEdge = suffixTree.getEdge(NodeKey.lookup(curEdge.getChildNode(), e1));
                if (!newEdge.isPresent()) {
                    final Node newNode = suffixTree.newNode();
                    final Edge newBranch = new Edge(curEdge.getChildNode(), newNode, Ranges.atLeast(indexCur));
                    suffixTree.addEdge(NodeKey.newNodeKey(newBranch, suffixTree), newBranch);

                    return;
                }

                curEdge = newEdge.get();
                edgeCur = curEdge.getRange().lowerEndpoint() + 1;
                indexCur++;

//                checkState(indexCur < elements.size());

                continue;
            }

            // If the elements are not equal, we have to split this node.
            if (!e1.equals(elements.get(edgeCur))) {

                suffixTree.removeEdge(curEdge);

                // Create a new node that is a closed range of elements that matched.
                final Node newNode = suffixTree.newNode();
                final Edge newEdge1 = new Edge(curEdge.getParentNode(), newNode,
                                               Ranges.closed(curEdge.getRange().lowerEndpoint(), edgeCur - 1));
                suffixTree.addEdge(NodeKey.lookup(curEdge.getParentNode(),
                                                  suffixTree.getElement(curEdge.getRange().lowerEndpoint())),
                                   newEdge1);

                // Link the old node, to this new node, but starting from the first character that didn't match
                final Edge newEdge2 = Edge.newStartEdge(newNode, curEdge.getChildNode(), curEdge.getRange(),
                                                        edgeCur);
                suffixTree.addEdge(NodeKey.newNodeKey(newEdge2, suffixTree), newEdge2);

                final Node newBranchNode = suffixTree.newNode();
                final Edge newBranchNodeEdge
                        = new Edge(newNode, newBranchNode, Ranges.atLeast(indexCur));
                suffixTree.addEdge(NodeKey.newNodeKey(newBranchNodeEdge, suffixTree), newBranchNodeEdge);

                // Split finish, can jump out.
                return;
            }

            edgeCur++;
            indexCur++;
        }
    }
}
