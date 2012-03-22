package diffr.suffixtree.impl;

import com.google.common.collect.Lists;
import javolution.text.Text;
import org.testng.annotations.Test;

import java.util.List;

import static diffr.suffixtree.impl.SuffixTreeImplTestUtils.validateSuffixTree;

/**
 * Tests {@link SuffixTreeImpl}.
 *
 * @author Jakub D Kozlowski
 * @since 0.1
 */
public class SuffixTreeImplTest {


    @Test(dataProviderClass = SuffixTreeImplTestUtils.class,
          dataProvider = SuffixTreeImplTestUtils.STRING_DATA_PROVIDER)
    public void testNewSuffixTreeStrings(final String testString) {
        validateSuffixTree(SuffixTreeImpl.newSuffixTree(Lists.charactersOf(testString)));
    }

    @Test(dataProviderClass = SuffixTreeImplTestUtils.class,
          dataProvider = SuffixTreeImplTestUtils.FILE_DATA_PROVIDER)
    public void testNewSuffixTreeFiles(final List<Text> testFile) {
        validateSuffixTree(SuffixTreeImpl.newSuffixTree(testFile));
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void testAddEdgeNullEdge() {
        SuffixTreeImpl.newSuffixTree(Lists.charactersOf("123")).addEdge(null);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testAddEdgeContainsEdge() {
        final SuffixTreeImpl<Character> suffixTree
                = SuffixTreeImpl.newSuffixTree(Lists.charactersOf("123"));
        suffixTree.addEdge(suffixTree.getEdge(suffixTree.getRoot(), '1').get());
    }
}
