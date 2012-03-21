package diffr.suffixtree.impl;

import com.google.common.collect.Lists;
import javolution.text.Text;

import java.util.List;
import java.util.Random;

/**
 * Utilities for generating random files.
 *
 * @author Jakub D Kozlowski
 * @since 0.1
 */
public class SuffixTreeImplTestUtils {

    /**
     * Gets a randomly generated file of this {@code fileLength}.
     *
     * @param fileLength fileLength of the file to generate.
     *
     * @return random file of this {@code fileLength}.
     */
    public static List<Text> getRandomFile(final long fileLength) {

        final List<Text> testFile = Lists.newArrayList();

        final Random random = new Random();

        final char minChar = 33;
        final int charRange = 127 - 33 + 1;

        for (int i = 0; i < fileLength; i++) {

            if (0 == random.nextInt(2) || 0 == testFile.size()) {

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
}
