package diffr.util.instruction;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Tests {@link diffr.util.instruction.InstructionParser}
 *
 * @author William Martin
 * @since 0.2
 */
public class InstructionParserTest {

    private InstructionParser instructionParser;
    public static final String[] trueInsertExamples = {"> hello",
            "> 1245oweifhwoeijoij//fwefwgtwgt", ">                  \n\n\n\n"};
    public static final String[] falseInsertExamples = {">hello", ">\t\n", ">\n", "><",
            ">>", ">", " >", "hello", "123\n", "\t> hello"};
    public static final String[] trueCopyExamples = {"5,41", "50,425", "234565,2344000", "0,0"};
    public static final String[] falseCopyExamples = {"05,4", "5f,4", ",0", ",",
            "0,", "hello sir!", " 3,4", "4,2 ", "4, 2", "4 ,2"};

    @BeforeMethod
    public void setUp() {
        instructionParser = new InstructionParser();
    }

    /**
     * Tests whether the parse copy instruction method works correctly.
     */
    @Test
    public void testParseCopyInstruction() {
        for (final String s : trueCopyExamples) {
            final CopyInstruction copyInstruction =
                    instructionParser.parseCopyInstruction(s);
            assertThat(InstructionComposer.composeString(copyInstruction), is(s));
        }
    }

    /**
     * Tests whether the parse insert instruction method works correctly.
     */
    @Test
    public void testParseInsertInstruction() {
        for (final String s : trueInsertExamples) {
            final InsertInstruction insertInstruction =
                    instructionParser.parseInsertInstruction(s);
            assertThat(InstructionComposer.composeString(insertInstruction), is(s));
        }
    }

    /**
     * Tests whether the parse instruction method works correctly.
     */
    @Test
    public void testParseInstruction() {
        for (final String s : trueInsertExamples) {
            final Instruction instruction =
                    instructionParser.parseInstruction(s).get();
            assertThat(InstructionComposer.composeString(instruction), is(s));
        }
        for (final String s : trueCopyExamples) {
            final Instruction instruction =
                    instructionParser.parseInstruction(s).get();
            assertThat(InstructionComposer.composeString(instruction), is(s));
        }
    }

    /**
     * Tests whether the static regex for InsertInstruction works correctly.
     */
    @Test
    public void testInsertRegex() {
        for (final String s : trueInsertExamples) {
            assertThat(s.matches(InstructionParser.INSERT_REGEX), is(true));
        }
        for (final String s : falseInsertExamples) {
            assertThat(s.matches(InstructionParser.INSERT_REGEX), is(false));
        }
    }

    /**
     * Tests whether the static regex for CopyInstruction works correctly.
     */
    @Test
    public void testCopyRegex() {
        for (final String s : trueCopyExamples) {
            assertThat(s.matches(InstructionParser.COPY_REGEX), is(true));
        }
        for (final String s : falseCopyExamples) {
            assertThat(s.matches(InstructionParser.COPY_REGEX), is(false));
        }
    }
}
