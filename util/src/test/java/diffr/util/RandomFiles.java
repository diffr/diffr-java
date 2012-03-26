package diffr.util;

import com.google.common.collect.Lists;
import javolution.text.Text;

import java.util.List;
import java.util.Random;

/**
 * Utilities for generating random files.
 *
 * @author Jakub D Kozlowski
 * @since 0.3
 */
public final class RandomFiles {

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
}
