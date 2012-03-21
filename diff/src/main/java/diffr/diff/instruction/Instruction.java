package diffr.diff.instruction;

import java.io.OutputStream;
import java.io.PrintStream;

/**
 * Class for an instruction for creating the new file.
 *
 * @author William Martin
 * @since 0.1
 */
public abstract class Instruction {

    /**
     * Writes this Instruction to the given print stream.
     *
     * @param out the print stream to write to.
     */
    public abstract void write(final PrintStream out);
}
