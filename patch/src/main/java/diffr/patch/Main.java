package diffr.patch;

import com.google.common.base.Optional;
import com.google.common.io.Files;
import diffr.util.ArgumentsProcessor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

/**
 * Main entry point to diffr's PATCH tool.
 * <p/>
 * <p>
 * Expects two arguments:
 * <ul>
 * <li>&lt;original-file&gt; - The original file.</li>
 * <li>&lt;diff-file&gt; - The diff file created using the diff tool.</li>
 * </ul>
 * </p>
 *
 * @author Jakub D Kozlowski
 * @author Amaury Couste
 * @author William Martin
 * @since 0.1
 */
public final class Main {

    /**
     * Prints the usage of this tool.
     */
    private static void printUsage() {
        System.out.println("Usage: \n" +
                "    patchr <original-file> <patch-file>\n" +
                "    patchr <original-file> <patch-file> -o <output-file>");
    }

    /**
     * Runs the patch tool on the original file.
     *
     * @param args arguments to this tool.
     */
    public static void main(final String... args) {

        try {
            if (ArgumentsProcessor.containsHelpArgument(args)
                    || (2 != args.length
                    && 4 != args.length)) {
                printUsage();
                System.exit(-1);
            }

            final File firstFile = new File(args[0]);
            final File patchFile = new File(args[1]);

            if (!firstFile.exists()) {
                System.err.println("File " + firstFile + " not found.");
                System.exit(-1);
            }

            if (!patchFile.exists()) {
                System.err.println("File " + patchFile + " not found.");
                System.exit(-1);
            }

            final List<String> firstFileStrings = Files.readLines(firstFile, Charset.defaultCharset());
            final List<String> patchFileStrings = Files.readLines(patchFile, Charset.defaultCharset());

            final Patchr patchr = new Patchr(firstFileStrings, patchFileStrings);

            final List<String> newFileStrings = patchr.patch();

            final Optional<String> outputFile = ArgumentsProcessor.extractOutputFile(args);
            if (4 == args.length
                    && outputFile.isPresent()) {
                final File file = new File(outputFile.get());
                final BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
                for (final String line : newFileStrings) {
                    bufferedWriter.write(line);
                    bufferedWriter.write("\n");
                }
                bufferedWriter.close();
            } else {
                for (final String line : newFileStrings) {
                    System.out.println(line);
                }
            }

        } catch (final IOException io) {
            System.err.println("There was a problem reading the files: " + io);
        } catch (final IllegalPatchFileException ipfe) {
            System.err.println("The patch file is incorrect, exiting.");
        }
    }
}
