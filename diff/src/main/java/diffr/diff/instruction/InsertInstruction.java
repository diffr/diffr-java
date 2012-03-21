package diffr.diff.instruction;

import java.io.PrintStream;

/**
 * {@link Instruction} for inserting some text.
 *
 * @author William Martin
 * @since 0.1
 */
public class InsertInstruction extends Instruction {

    private String text;

    /**
     * Default constructor.
     *
     * @param text the text to insert.
     */
    public InsertInstruction(final String text) {
        if (null == text) {
            throw new IllegalArgumentException("Text cannot be null.");
        }
        this.text = text;
    }

    /**
     * Gets the text.
     *
     * @return the text.
     */
    public String getText() {
        return text;
    }

    /**
     * Sets the text.
     *
     * @param text the text.
     */
    public void setText(final String text) {
        if (null == text) {
            throw new IllegalArgumentException("Text cannot be null.");
        }
        this.text = text;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void write(final PrintStream out) {
        out.println("> " + text);
    }
}
