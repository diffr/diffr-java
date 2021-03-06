package diffr.util.instruction;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import org.testng.annotations.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Tests {@link InstructionParser}
 *
 * @author William Martin
 * @since 0.2
 */
public class InstructionsTest {

    BufferedReader reader;

    /**
     * Tests whether the read instruction method works correctly.
     */
    @Test
    public void testReadInstruction() throws IOException {
        final String text = "3,4\n> hello world\n1,2\nbroken line!\n>anotherbrokenline\n\n";
        reader = new BufferedReader(new StringReader(text));

        assertThat(Instructions.readInstruction(reader).get(),
                is(CopyInstruction.class));
        assertThat(Instructions.readInstruction(reader).get(),
                is(InsertInstruction.class));
        assertThat(Instructions.readInstruction(reader).get(),
                is(CopyInstruction.class));
        assertThat(Instructions.readInstruction(reader).isPresent(),
                is(false));
        assertThat(Instructions.readInstruction(reader).isPresent(),
                is(false));
        assertThat(Instructions.readInstruction(reader).isPresent(),
                is(false));
        assertThat(Instructions.readInstruction(reader).isPresent(),
                is(false));
    }

    /**
     * Tests whether the parse copy instruction method works correctly.
     */
    @Test
    public void testWriteInstruction() throws IOException {
        final List<Instruction> instructionList = new ArrayList<Instruction>();
        final InstructionParser parser = new InstructionParser();
        for (final String s : InstructionParserTest.trueCopyExamples) {
            instructionList.add(parser.parseInstruction(s).get());
        }
        for (final String s : InstructionParserTest.trueInsertExamples) {
            instructionList.add(parser.parseInstruction(s).get());
        }
        Collections.shuffle(instructionList);
        final StringWriter stringWriter = new StringWriter();
        final BufferedWriter writer = new BufferedWriter(stringWriter);

        for (final Instruction instruction : instructionList) {
            Instructions.writeInstruction(instruction, writer);
        }
        final String result = stringWriter.toString();
        reader = new BufferedReader(new StringReader(result));
        Optional<Instruction> instruction = null;
        final List<Instruction> resultList = new ArrayList<Instruction>();
        while ((instruction = Instructions.readInstruction(reader)).isPresent()) {
            resultList.add(instruction.get());
        }
        for (
                Iterator<Instruction> it1 = instructionList.iterator(),
                        it2 = resultList.iterator();
                it1.hasNext() && it2.hasNext();
                ) {
            assertThat(it1.next() == it2.next(), is(true));
        }
    }

    /**
     * Tests whether the read instructions method works correctly.
     */
    @Test
    public void testReadInstructions() throws IOException, IllegalPatchInstructionException {
        final List<String> lines = Lists.newArrayList("3,4", "> hello world", "1,2", "11,12", "> okline");
        final List<Instruction> instructions = Instructions.readInstructions(lines);
        assertThat(instructions.get(0), is(CopyInstruction.class));
        assertThat(instructions.get(1), is(InsertInstruction.class));
        assertThat(instructions.get(2), is(CopyInstruction.class));
        assertThat(instructions.get(3), is(CopyInstruction.class));
        assertThat(instructions.get(4), is(InsertInstruction.class));
    }

    @Test(expectedExceptions = IllegalPatchInstructionException.class)
    public void testReadInstructionsException() throws IllegalPatchInstructionException {
        final List<String> broken = Lists.newArrayList("brokeninstruction");
        Instructions.readInstructions(broken);
    }
}
