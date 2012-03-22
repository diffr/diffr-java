package diffr.util.instruction;

/**
 * Class for an instruction for creating the new file.
 *
 * @author William Martin
 * @since 0.1
 */
public interface Instruction {

    /**
     * Enumerates the possible Instruction types.
     */
    public enum Type {
        Copy, Insert;
    }

    /**
     * Gets the type of this Instruction.
     *
     * @return the type of this Instruction.
     */
    public Type getType();
}
