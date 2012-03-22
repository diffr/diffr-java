package diffr.suffixtree.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Range;
import diffr.suffixtree.SuffixTree;
import diffr.suffixtree.SuffixTree.Matched;
import diffr.suffixtree.SuffixTree.Matcher;
import diffr.suffixtree.SuffixTrees;
import org.testng.annotations.Test;

import java.util.ListIterator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Tests {@link MatcherImpl}.
 *
 * @author Jakub D Kozlowski
 * @since 0.2
 */
public class MatcherImplTest {

    @Test(expectedExceptions = NullPointerException.class)
    public void testConstructorNullSuffixTree() {
        new MatcherImpl<Integer>(null);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void testMatchNextNullElement() {
        new MatcherImpl<Integer>(SuffixTreeImpl.newSuffixTree(Lists.newArrayList(1, 2))).matchNext(null);
    }

    @Test
    public void testMatchNextElementMatches() {

        final SuffixTree<Character> suffixTree = SuffixTreeImpl.newSuffixTree(Lists.charactersOf("mississippi"));
        final Matcher<Character> matcher = suffixTree.matcher();

        for (Character c : Lists.charactersOf("issippi")) {
            assertThat(matcher.matchNext(c), is(Matched.YES));
        }

        assertThat(matcher.isFinished(), is(false));
    }

    @Test
    public void testMatchNextElementDoesNotMatch() {

        final SuffixTree<Character> suffixTree = SuffixTreeImpl.newSuffixTree(Lists.charactersOf("mississippi"));
        final Matcher<Character> matcher = suffixTree.matcher();

        for (Character c : Lists.charactersOf("issipp")) {
            assertThat(matcher.matchNext(c), is(Matched.YES));
        }

        assertThat(matcher.matchNext('p'), is(Matched.NO));
        assertThat(matcher.isFinished(), is(true));
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void testMatchNextElementMatcherFinished() {
        final SuffixTree<Character> suffixTree = SuffixTreeImpl.newSuffixTree(Lists.charactersOf("mississippi"));
        final Matcher<Character> matcher = suffixTree.matcher();

        for (Character c : Lists.charactersOf("issipp")) {
            assertThat(matcher.matchNext(c), is(Matched.YES));
        }

        assertThat(matcher.matchNext('p'), is(Matched.NO));
        assertThat(matcher.isFinished(), is(true));
        matcher.matchNext('b');
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void testLastIndexNotStarted() {
        SuffixTreeImpl.newSuffixTree(Lists.charactersOf("mississippi")).matcher().lastIndex();
    }

    @Test
    public void testIsRootNotStarted() {
        assertThat(SuffixTreeImpl.newSuffixTree(Lists.charactersOf("mississippi")).matcher().isRoot(), is(true));
    }

    @Test
    public void testIsRootFailedToMatchFirstElement() {
        final Matcher<Character> matcher = SuffixTrees.newSuffixTree(Lists.charactersOf("mississippi")).matcher();
        matcher.matchNext('k');
        assertThat(matcher.isRoot(), is(true));
    }

    @Test
    public void testIsRootMatcherNotAtRoot() {
        final Matcher<Character> matcher = SuffixTrees.newSuffixTree(Lists.charactersOf("mississippi")).matcher();
        matcher.matchNext('i');
        assertThat(matcher.isRoot(), is(false));
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void testGetRangeNotStarted() {
        SuffixTrees.newSuffixTree(Lists.charactersOf("mississippi")).matcher().range();
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void testGetRangeFailedToMatchFirstElement() {
        final Matcher<Character> matcher = SuffixTrees.newSuffixTree(Lists.charactersOf("mississippi")).matcher();
        matcher.matchNext('k');
        matcher.range();
    }

    @Test(dataProviderClass = SuffixTreeImplTestUtils.class,
          dataProvider = SuffixTreeImplTestUtils.STRING_DATA_PROVIDER)
    public void testGetRange(final String testString) {
        final SuffixTreeImpl<Character> suffixTree = SuffixTreeImpl.newSuffixTree(Lists.charactersOf(testString));
        for (int suffixIndex = 0; suffixIndex < suffixTree.getElements().size(); suffixIndex++) {

            final ListIterator<Character> suffixIterator = suffixTree.elementsListIterator(suffixIndex);
            final Matcher<Character> suffixMatcher = suffixTree.matcher();
            final StringBuilder b = new StringBuilder();

            while (suffixIterator.hasNext()) {
                final Character c = suffixIterator.next();
                b.append(c);
                assertThat(suffixMatcher.matchNext(c), is(Matched.YES));
                final Range<Integer> matchedRange = suffixMatcher.range();

                assertThat(testString.indexOf(b.toString()), is(matchedRange.lowerEndpoint()));
                assertThat(testString.indexOf(b.toString()) + b.length() - 1, is(matchedRange.upperEndpoint()));
            }
        }
    }
}
