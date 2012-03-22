package diffr.suffixtree.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Ranges;
import javolution.util.FastCollection.Record;
import javolution.util.Index;
import org.testng.annotations.Test;

/**
 * Tests {@link NodeKey}.
 *
 * @author Jakub D Kozlowski
 * @since 0.1
 */
public class NodeKeyTest {

    private static final Record parent = Index.ZERO.getNext();

    private static final Record child = parent.getNext();
    
    private final SuffixTreeImpl<Integer> suffixTree = SuffixTreeImpl.newSuffixTree(Lists.newArrayList(1,2,3));

    @Test(expectedExceptions = NullPointerException.class)
    public void testLookupNullParent() {
        NodeKey.lookup(null, 1);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void testLookupNullElement() {
        NodeKey.lookup(parent, null);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void testNewNodeKeyNullEdge() {
        NodeKey.newNodeKey(null, suffixTree);
    }
    
    @Test(expectedExceptions = NullPointerException.class)
    public void testNewNodeKeyNullSuffixTree() {
        NodeKey.newNodeKey(new Edge(parent, child, Ranges.atLeast(1)),
                           (SuffixTreeImpl<Integer>) null);
    }
    
    @Test(expectedExceptions = IndexOutOfBoundsException.class)
    public void testNewNodeIndexOutOfBound() {
        NodeKey.newNodeKey(new Edge(parent, child, Ranges.atLeast(3)), suffixTree);
    }
}
