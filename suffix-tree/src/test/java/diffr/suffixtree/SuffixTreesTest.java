package diffr.suffixtree;

import com.google.common.collect.Lists;
import diffr.suffixtree.SuffixTrees;
import diffr.suffixtree.impl.SuffixTreeImpl;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Tests {@link SuffixTrees}.
 *
 * @author Jakub D Kozlowski
 * @since 0.2
 */
public class SuffixTreesTest {
    
    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void testConstructor() {
        new SuffixTrees();
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void testNewSuffixTreeNullElements() {
        SuffixTrees.newSuffixTree(null);
    }

    @Test
    public void testNewSuffixTree() {
        assertThat(SuffixTrees.newSuffixTree(Lists.charactersOf("bla")), is(SuffixTreeImpl.class));
    }
}
