package diffr.util.instruction;

import com.google.common.collect.Ranges;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Tests {@link diffr.util.instruction.InstructionComposer}
 *
 * @author William Martin
 * @since 0.2
 */
public class InstructionComposerTest {

    Instruction instruction;

    /**
     * Tests whether the compose string method works correctly.
     */
    @Test
    public void testStringComposer() {
        final String text = "hello world";
        instruction = new InsertInstruction(text);
        final String converted = InstructionComposer.composeString(instruction);
        assertThat(converted.matches(InstructionParser.INSERT_REGEX), is(true));

        instruction = new CopyInstruction(Ranges.closed(5, 10));
        final String formattedString = InstructionComposer.composeString(instruction);
        assertThat(formattedString.matches(InstructionParser.COPY_REGEX), is(true));
    }
}
