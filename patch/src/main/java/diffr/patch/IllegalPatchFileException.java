package diffr.patch;

/**
 * Exception that announces an illegal patch file.
 *
 * @author William Martin
 * @author Amaury Couste
 * @since 0.3
 */
public class IllegalPatchFileException extends Exception {

    public static final String MESSAGE = "Error. Illegal patch file.";

    public IllegalPatchFileException(final String text) {
        super(text);
    }

    public IllegalPatchFileException() {
        super(MESSAGE);
    }
}
