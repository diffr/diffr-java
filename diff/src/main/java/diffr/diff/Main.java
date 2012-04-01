package diffr.diff;

import com.google.common.base.Optional;
import com.google.common.io.Files;
import diffr.util.ArgumentsProcessor;
import diffr.util.instruction.Instruction;
import diffr.util.instruction.InstructionComposer;
import diffr.util.instruction.Instructions;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

/**
 * Main entry point to diffr's DIFF tool.
 * <p/>
 * <p>
 * Expects two arguments:
 * <ul>
 * <li>&lt;original-file&gt; - The original file to diff.</li>
 * <li>&lt;new-file&gt; - The new version of the original file to diff.</li>
 * </ul>
 * </p>
 *
 * @author Jakub D Kozlowski
 * @author Sarina Gurung
 * @since 0.1
 */
public final class Main {

    /**
     * Prints the usage of this tool.
     */
    private static void printUsage() {
        System.out.println("Usage: \n" +
                "    diffr <original-file> <new-file>\n" +
                "    diffr <original-file> <new-file> -o <output-file>");
    }

    /**
     * Runs the diff tool on two files.
     *
     * @param args arguments to this tool.
     */
    public static void main(String... args) throws IOException {

        try {
            if (ArgumentsProcessor.containsHelpArgument(args)
                    || (2 != args.length
                    && 4 != args.length)) {
                printUsage();
                System.exit(-1);
            }

            final File firstFile = new File(args[0]);
            final File secondFile = new File(args[1]);

            if (!firstFile.exists()) {
                System.err.println("File " + firstFile + " not found.");
                System.exit(-1);
            }

            if (!secondFile.exists()) {
                System.err.println("File " + secondFile + " not found.");
                System.exit(-1);
            }

            final List<String> originalFile = Files.readLines(firstFile, Charset.defaultCharset());
            final List<String> newFile = Files.readLines(secondFile, Charset.defaultCharset());

            final List<Instruction> instructions = new Diffr(originalFile, newFile).diff();

            final Optional<String> outputFile = ArgumentsProcessor.extractOutputFile(args);

            if (4 == args.length
                    && outputFile.isPresent()) {

                final File file = new File(outputFile.get());
                final BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
                for (final Instruction instruction : instructions) {
                    Instructions.writeInstruction(instruction, bufferedWriter);
                }
                bufferedWriter.close();
            } else {
                for (final Instruction instruction : instructions) {
                    System.out.println(InstructionComposer.composeString(instruction));
                }
                System.out.flush();
            }

            System.exit(0);
        } catch (final IOException io) {
            System.err.println("There was a problem reading the files: " + io);
        }
    }
}
