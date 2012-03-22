package diffr.util.instruction;

import com.google.common.base.Optional;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

/**
 * Factory methods for Instruction classes.
 *
 * @author William Martin
 * @since 0.2
 */
public class Instructions {

    /**
     * Writes the given Instruction to the supplied writer and ends the line.
     *
     * @param instruction the Instruction to write.
     * @param writer      writer to write to.
     * @throws java.io.IOException if there was an error when writing.
     * @see {@link java.io.BufferedWriter#write(String)}
     */
    public static void writeInstruction(final Instruction instruction,
                                        final BufferedWriter writer) throws IOException {
        final String string = InstructionComposer.composeString(instruction);
        writer.write(string);
        writer.write("\n");
    }

    /**
     * Reads the next {@link diffr.util.instruction.Instruction} in the input.
     *
     * @param reader input to read from.
     * @return the next {@link diffr.util.instruction.Instruction} in the input,
     *         or null if the end of the input is reached.
     * @throws IOException if an error reading the input occurred.
     */
    public static Optional<Instruction> readInstruction(final BufferedReader reader) throws IOException {
        final String line = reader.readLine();
        if (null != line) {
            try {
                return InstructionParser.parseInstruction(line);
            } catch (final IllegalArgumentException iae) {
            }
        }
        return Optional.absent();
    }
}
