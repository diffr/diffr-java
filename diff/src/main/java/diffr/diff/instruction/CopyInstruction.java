package diffr.diff.instruction;

import java.io.PrintStream;

/**
 * {@link Instruction} for copying a set of lines from the original file.
 *
 * @author William Martin
 * @since 0.1
 */
public class CopyInstruction extends Instruction {

    public static final String COMMA = ",";
    private int fromID, toID;

    /**
     * Default constructor.
     *
     * @param fromID the beginning of the copy range.
     * @param toID   the end of the copy range.
     */
    public CopyInstruction(final int fromID, final int toID) {
        if (toID < fromID) {
            throw new IllegalArgumentException("toID cannot be before fromID.");
        } else if (0 > fromID) {
            throw new IllegalArgumentException("IDs cannot be negative.");
        }
        this.fromID = fromID;
        this.toID = toID;
    }

    /**
     * Gets the from ID.
     *
     * @return the from ID.
     */
    public int getFromID() {
        return fromID;
    }

    /**
     * Sets the from ID.
     *
     * @param fromID the from ID.
     */
    public void setFromID(final int fromID) {
        if (toID < fromID) {
            throw new IllegalArgumentException("toID cannot be before fromID.");
        } else if (0 > fromID) {
            throw new IllegalArgumentException("IDs cannot be negative.");
        }
        this.fromID = fromID;
    }

    /**
     * Gets the to ID.
     *
     * @return the to ID.
     */
    public int getToID() {
        return toID;
    }

    /**
     * Sets the to ID.
     *
     * @param toID the to ID.
     */
    public void setToID(final int toID) {
        if (toID < fromID) {
            throw new IllegalArgumentException("toID cannot be before fromID.");
        }
        this.toID = toID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void write(final PrintStream out) {
        out.println(fromID + COMMA + toID);
    }
}
