package diffr.util.instruction;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Optional;
import com.google.common.collect.Range;
import com.google.common.collect.Ranges;

/**
 * Parses {@link Instruction}s.
 *
 * @author William Martin
 * @since 0.2
 */
public class InstructionParser {

    public static final String NUMBER_REGEX = "(0|([1-9]\\d*))",
            COPY_REGEX = NUMBER_REGEX + "," + NUMBER_REGEX,
            INSERT_REGEX = "> [\\S\\s]*";

    /**
     * Parses the given string into a CopyInstruction if it matches this Instruction's regex.
     *
     * @param text the text to parse.
     * @return the CopyInstruction.
     */
    @VisibleForTesting
    static CopyInstruction parseCopyInstruction(final String text) {
        final String[] segments = text.split(InstructionComposer.COMMA);
        final int fromID = Integer.parseInt(segments[0]);
        final int toID = Integer.parseInt(segments[1]);
        final Range<Integer> range = Ranges.closed(fromID, toID);
        return new CopyInstruction(range);
    }

    /**
     * Text constructor. Parses the given string into a CopyInstruction if it matches this Instruction's regex.
     *
     * @param text the text to parse.
     * @return the InsertInstruction.
     */
    @VisibleForTesting
    static InsertInstruction parseInsertInstruction(final String text) {
        return new InsertInstruction(text.substring(2));
    }

    /**
     * Parses the given text for an Instruction.
     *
     * @param text the text to parse for an Instruction.
     * @return the Instruction contained in the text, or null if no Instruction is found.
     */
    public static Optional<Instruction> parseInstruction(final String text) {
        Instruction instruction = null;
        if (text.matches(COPY_REGEX)) {
            instruction = parseCopyInstruction(text);
        } else if (text.matches(INSERT_REGEX)) {
            instruction = parseInsertInstruction(text);
        }
        return Optional.fromNullable(instruction);
    }
}
