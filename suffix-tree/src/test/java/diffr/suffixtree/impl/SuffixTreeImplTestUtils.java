package diffr.suffixtree.impl;

import com.google.common.collect.Lists;
import diffr.suffixtree.SuffixTree;
import diffr.suffixtree.SuffixTree.Matched;
import diffr.suffixtree.SuffixTree.Matcher;
import javolution.text.Text;
import org.testng.annotations.DataProvider;

import java.util.List;
import java.util.ListIterator;
import java.util.Random;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Utilities for testing {@link SuffixTree}s.
 *
 * @author Jakub D Kozlowski
 * @since 0.1
 */
public class SuffixTreeImplTestUtils {

    public static final String STRING_DATA_PROVIDER = "string-data-provider";

    public static final String FILE_DATA_PROVIDER = "file-data-provider";

    /**
     * Gets the test strings.
     *
     * @return the test strings.
     */
    @DataProvider(name = STRING_DATA_PROVIDER, parallel = true)
    public static String[][] getStrings() {
        final List<String[]> testStrings = Lists.newArrayList();

        testStrings.add(new String[]{"doodahde"});
        testStrings.add(new String[]{"ababa"});
        testStrings.add(new String[]{"xabxa"});
        testStrings.add(new String[]{"bananas"});
        testStrings.add(new String[]{"bookkeeper"});
        testStrings.add(new String[]{"mississippi"});
        testStrings.add(new String[]{"misrsissippi"});
        testStrings.add(new String[]{"I at once heard the rickety crickety creaking of the bridge."});

        final Random random = new Random(1341376661705837014L);

        for (int i = 0; i < 2; i++) {
            final StringBuilder b = new StringBuilder();
            final int length = random.nextInt(500);
            for (int j = 0; j < length; j++) {
                b.append(random.nextInt(10));
            }
            testStrings.add(new String[]{b.toString()});
        }

        return testStrings.toArray(new String[][]{});
    }

    /**
     * Gets the test files.
     *
     * @return the test files.
     */
    @DataProvider(name = FILE_DATA_PROVIDER, parallel = true)
    public static List[][] getFiles() {
        final List<List<Text>[]> textFiles = Lists.newArrayList();

        for (int i = 0; i < 5; i++) {
            textFiles.add(new List[]{getRandomFile(1000, 1341376661708488015L + i * 1000L)});
        }

        return textFiles.toArray(new List[][]{});
    }

    /**
     * Gets a randomly generated file of this {@code fileLength} using this {@code seed} to initialise the random
     * number generator.
     *
     * @param fileLength fileLength of the file to generate.
     * @param seed       initial seed.
     *
     * @return random file of this {@code fileLength}, generated from this {@code seed}.
     */
    public static List<Text> getRandomFile(final long fileLength, final long seed) {

        final List<Text> testFile = Lists.newArrayList();

        final Random random = new Random(seed);

        final char minChar = 33;
        final int charRange = 127 - 33 + 1;

        for (int i = 0; i < fileLength; i++) {

            if (0 == random.nextInt(4) || 0 == testFile.size()) {

                final StringBuilder b = new StringBuilder();
                final int lineLength = random.nextInt(100) + 1;
                for (int j = 0; j < lineLength; j++) {
                    b.append((char) (minChar + (random.nextInt(charRange))));
                }
                testFile.add(new Text(b.toString()));
            }
            else {
                testFile.add(testFile.get(random.nextInt(testFile.size())));
            }
        }

        return testFile;
    }

    /**
     * Validates this {@code suffixTree}.
     *
     * @param suffixTree {@link SuffixTree} to validate.
     * @param <E>        type of elements in {@code suffixTree}.
     */
    public static <E extends Comparable> void validateSuffixTree(final SuffixTreeImpl<E> suffixTree) {

        for (int suffixIndex = 0; suffixIndex < suffixTree.getElements().size(); suffixIndex++) {

            final ListIterator<E> suffixIterator = suffixTree.elementsListIterator(suffixIndex);
            final Matcher<E> suffixMatcher = suffixTree.matcher();

            while (suffixIterator.hasNext()) {
                assertThat(suffixMatcher.matchNext(suffixIterator.next()), is(Matched.YES));
            }
        }
    }
}
