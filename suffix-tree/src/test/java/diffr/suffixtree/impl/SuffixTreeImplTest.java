package diffr.suffixtree.impl;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Random;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Tests {@link SuffixTreeImpl}.
 *
 * @author Jakub D Kozlowski
 * @since 0.1
 */
@Test(groups = "fast")
public class SuffixTreeImplTest {

    private static final String DEFAULT_DATA_PROVIDER = "default-provider";

    @DataProvider(name = DEFAULT_DATA_PROVIDER)
    public String[][] getData() {
        final List<String[]> testStrings = Lists.newArrayList();

        testStrings.add(new String[]{"doodah"});
        testStrings.add(new String[]{"ababa"});
        testStrings.add(new String[]{"xabxa"});
        testStrings.add(new String[]{"bananas"});
        testStrings.add(new String[]{"bookkeeper"});
        testStrings.add(new String[]{"mississippi"});
        testStrings.add(new String[]{"I at once heard the rickety crickety creaking of the bridge."});

        final Random random = new Random();

        for (int i = 0; i < 2; i++) {
            final StringBuilder b = new StringBuilder();
            final int length = random.nextInt(1000);
            for (int j = 0; j < length; j++) {
                b.append(random.nextInt(10));
            }
            testStrings.add(new String[]{b.toString()});
        }

        return testStrings.toArray(new String[][]{});
    }

    @Test(dataProvider = DEFAULT_DATA_PROVIDER)
    public void testNewSuffixTree(final String testString) {

        final List<Character> string = Lists.charactersOf(testString);

        final SuffixTreeImpl<Character> suffixTree = SuffixTreeImpl.newSuffixTree(string);

        for (int i = 0; i < testString.length(); i++) {

            final String suffix = testString.substring(i);

            Node curNode = suffixTree.getRoot();
            int j = 0;
            int p = 0;
            Optional<Edge> curEdge = suffixTree.getEdge(NodeKey.lookup(curNode, suffix.charAt(j)));
            assertThat(curEdge.isPresent(), is(true));

            while (j < suffix.length()) {

                assertThat(suffix.charAt(j), is(testString.charAt(curEdge.get().getRange().lowerEndpoint() + p)));
                j++;
                p++;

                if (!curEdge.get().getRange().apply(curEdge.get().getRange().lowerEndpoint() + p) && j < suffix
                        .length()) {
                    curNode = curEdge.get().getChildNode();
                    curEdge = suffixTree.getEdge(NodeKey.lookup(curNode, suffix.charAt(j)));
                    assertThat(curEdge.isPresent(), is(true));
                    p = 0;

                }
            }

        }
    }


}
