package diffr.util.instruction;

import com.google.common.collect.Range;

/**
 * Composes a string representation of an Instruction.
 *
 * @author William Martin
 * @since 0.2
 */
public class InstructionComposer {

    public static final String COMMA = ",", INSERT = "> ";

    /**
     * Composes a string representation of the given Instruction.
     *
     * @param instruction the Instruction to convert.
     * @return the Instruction as a string.
     */
    public static String composeString(final Instruction instruction) {
        final StringBuilder stringBuilder = new StringBuilder();
        switch (instruction.getType()) {
            case Copy:
                final Range range = ((CopyInstruction) instruction).getRange();
                stringBuilder.append(range.lowerEndpoint());
                stringBuilder.append(COMMA);
                stringBuilder.append(range.upperEndpoint());
                break;
            case Insert:
                final String text = ((InsertInstruction) instruction).getText();
                stringBuilder.append(INSERT);
                stringBuilder.append(text);
        }
        return stringBuilder.toString();
    }
}
