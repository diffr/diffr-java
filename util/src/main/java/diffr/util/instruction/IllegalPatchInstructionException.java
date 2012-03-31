package diffr.util.instruction;

/**
 * Exception that announces an Illegal Instruction.
 *
 * @author William Martin
 * @since 1.0
 */
public class IllegalPatchInstructionException extends Exception {

    public static final String MESSAGE = "Error. Illegal instruction.";

    public IllegalPatchInstructionException(final String text) {
        super(text);
    }

    public IllegalPatchInstructionException() {
        super(MESSAGE);
    }
}
