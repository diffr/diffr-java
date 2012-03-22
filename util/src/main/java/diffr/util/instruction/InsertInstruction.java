package diffr.util.instruction;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * {@link Instruction} for inserting some text.
 *
 * @author William Martin
 * @since 0.1
 */
public class InsertInstruction implements Instruction {

    private final String text;

    /**
     * Default constructor.
     *
     * @param text the text to insert.
     */
    public InsertInstruction(final String text) {
        checkNotNull(text);
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
     * {@inheritDoc}
     */
    @Override
    public Type getType() {
        return Type.Insert;
    }
}
