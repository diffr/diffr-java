package diffr.suffixtree.impl;

import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.Range;
import com.google.common.collect.Ranges;
import diffr.suffixtree.SuffixTree;
import diffr.suffixtree.SuffixTrees;
import javolution.util.FastCollection.Record;
import javolution.util.Index;
import org.testng.annotations.Test;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.is;

/**
 * Tests {@link Edge}.
 *
 * @author Jakub D Kozlowski
 * @since 0.1
 */
public class EdgeTest {

    private static final Record parent = Index.ZERO.getNext();

    private static final Record child = parent.getNext();

    private static final Range<Integer> range = Ranges.atLeast(0);

    private static final Edge edge = new Edge(parent, child, range);

    @Test(expectedExceptions = NullPointerException.class)
    public void testContructorNullParentNode() {
        new Edge(null, child, range);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void testContructorNullChildNode() {
        new Edge(parent, null, range);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void testConstructorNullRange() {
        new Edge(parent, child, null);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testConstructorRangeWithoutLowerBound() {
        new Edge(parent, child, Ranges.atMost(2));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testConstructorRangeLowerBoundNegative() {
        new Edge(parent, child, Ranges.atMost(-1));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testConstructorParentChildSame() {
        new Edge(parent, parent, range);
    }

    @Test
    public void testConstructor() {
        final Edge edge = new Edge(parent, child, range);
        assertThat(edge.getParent(), is(parent));
        assertThat(edge.getChild(), is(child));
        assertThat(edge.getRange(), is(range));
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void testIteratorNullSuffixTree() {
        edge.iterator(null);
    }

    @Test
    public void testIteratorEdgeNoUpperBoundary() {

        final Edge edge = new Edge(parent, child, Ranges.atLeast(1));
        final SuffixTreeImpl<Integer> suffixTree = SuffixTreeImpl.newSuffixTree(Lists.newArrayList(1, 2, 3, 4, 5));

        final ListIterator<Integer> edgeIterator = edge.iterator(suffixTree);
        final ListIterator<Integer> elementsIterator = suffixTree.elementsListIterator(1);
        assertThat(Iterators.elementsEqual(edgeIterator, elementsIterator), is(true));
    }

    @Test
    public void testIteratorEdgeUpperBoundary() {

        final List<Integer> elements = Lists.newArrayList(1, 2, 3, 4, 5, 6);
        final SuffixTreeImpl<Integer> suffixTree = SuffixTreeImpl.newSuffixTree(elements);

        for (int i = 0; i < elements.size(); i++) {
            final Edge edge = new Edge(parent, child, Ranges.closed(0, i));
            final Iterator<Integer> iterator = edge.iterator(suffixTree);
            assertThat(Iterators.elementsEqual(iterator, elements.subList(0, i + 1).iterator()), is(true));

            final Edge edge2 = new Edge(parent, child, Ranges.closed(i, i));
            final Iterator<Integer> iterator2 = edge2.iterator(suffixTree);
            assertThat(Iterators.elementsEqual(iterator2, elements.subList(i, i + 1).iterator()), is(true));
        }
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testIteratorEdgeNotFromSuffixTree() {

        final List<Integer> elements = Lists.newArrayList(1, 2, 3, 4, 5, 6);
        final SuffixTreeImpl<Integer> suffixTree = SuffixTreeImpl.newSuffixTree(elements);
        final Edge edge = new Edge(parent, child, Ranges.closed(1, 6));

        edge.iterator(suffixTree);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void testNewStartEdgeNullParentNode() {
        Edge.newStartEdge(null, child, range, 1);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void testNewStartEdgeNullChildNode() {
        Edge.newStartEdge(parent, null, range, 1);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void testNewStartEdgeNullRange() {
        Edge.newStartEdge(parent, child, null, 1);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testNewStartEdgeNewStartNegative() {
        Edge.newStartEdge(parent, child, range, -1);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testNewStartEdgeNewStartGreaterThanUpperBound() {
        Edge.newStartEdge(parent, child, Ranges.closed(0, 1), 2);
    }

    @Test
    public void testNewStartEdgeNoUpperBound() {
        assertThat(Edge.newStartEdge(parent, child, range, 1).getRange().hasUpperBound(), is(false));
    }

    @Test
    public void testNewStartEdgeHasUpperBound() {
        assertThat(Edge.newStartEdge(parent, child, Ranges.closed(1, 3), 2).getRange().upperEndpoint(),
                   is(3));
    }

    @Test
    public void testToString() {
        assertThat(edge,
                   hasToString(equalTo("Edge{" + "parent=" + parent + ", " +
                                               "child=" + child + ", range=" + range + "}")));
    }
}
