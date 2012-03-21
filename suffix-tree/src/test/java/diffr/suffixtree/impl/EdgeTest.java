package diffr.suffixtree.impl;

import com.google.common.collect.Range;
import com.google.common.collect.Ranges;
import org.testng.annotations.Test;

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
@Test(groups = "fast")
public class EdgeTest {

    private static final Node parentNode = new Node(0);

    private static final Node childNode = new Node(1);

    private static final Range<Integer> range = Ranges.atLeast(0);

    private static final Edge edge = new Edge(parentNode, childNode, range);

    @Test(expectedExceptions = NullPointerException.class)
    public void testContructorNullParentNode() {
        new Edge(null, childNode, range);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void testContructorNullChildNode() {
        new Edge(parentNode, null, range);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void testConstructorNullRange() {
        new Edge(parentNode, childNode, null);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testConstructorRangeWithoutLowerBound() {
        new Edge(parentNode, childNode, Ranges.atMost(2));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testConstructorParentChildSame() {
        new Edge(parentNode, parentNode, range);
    }

    @Test
    public void testConstructor() {
        final Edge edge = new Edge(parentNode, childNode, range);
        assertThat(edge.getParentNode(), is(parentNode));
        assertThat(edge.getChildNode(), is(childNode));
        assertThat(edge.getRange(), is(range));
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void testNewStartEdgeNullParentNode() {
        Edge.newStartEdge(null, childNode, range, 1);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void testNewStartEdgeNullChildNode() {
        Edge.newStartEdge(parentNode, null, range, 1);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void testNewStartEdgeNullRange() {
        Edge.newStartEdge(parentNode, childNode, null, 1);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testNewStartEdgeNewStartNegative() {
        Edge.newStartEdge(parentNode, childNode, range, -1);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testNewStartEdgeNewStartGreaterThanUpperBound() {
        Edge.newStartEdge(parentNode, childNode, Ranges.closed(0, 1), 2);
    }

    @Test
    public void testNewStartEdgeNoUpperBound() {
        assertThat(Edge.newStartEdge(parentNode, childNode, range, 1).getRange().hasUpperBound(), is(false));
    }

    @Test
    public void testNewStartEdgeHasUpperBound() {
        assertThat(Edge.newStartEdge(parentNode, childNode, Ranges.closed(1, 3), 2).getRange().upperEndpoint(),
                   is(3));
    }

    @Test
    public void testToString() {
        assertThat(edge,
                   hasToString(equalTo("Edge{" + "parentNode=" + parentNode + ", " +
                                               "childNode=" + childNode + ", range=" + range + "}")));
    }
}
