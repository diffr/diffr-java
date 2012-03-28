package diffr.diff;

import com.google.common.collect.Lists;
import com.google.common.io.Files;
import com.google.common.io.LineProcessor;
import com.google.common.io.Resources;
import diffr.util.instruction.Instruction;
import diffr.util.instruction.InstructionComposer;
import diffr.util.instruction.InstructionParser;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Tests {@link Diffr}.
 *
 * @author Sarina Gurung
 * @author Jakub D Kozlowski
 * @since 0.3
 */
public class DiffrTest {

    private static final String DEFAULT_PROVIDER = "default-provider";

    @DataProvider(name = "default-provider")
    public Object[][] getFiles() throws URISyntaxException, IOException {

        final List<Object[]> files = Lists.newArrayList();

        final File originalDir = new File(Resources.getResource("original").toURI());
        for (final File originalFile : originalDir.listFiles()) {
            final File newFile = new File(Resources.getResource("new/" + originalFile.getName()).toURI());
            final File patchFile = new File(Resources.getResource("patch/" + originalFile.getName()).toURI());
            files.add(new Object[]{
                    Files.readLines(originalFile, Charset.defaultCharset()),
                    Files.readLines(newFile, Charset.defaultCharset()),
                    Files.readLines(patchFile, Charset.defaultCharset(), new LineProcessor<List<Instruction>>() {

                        private List<Instruction> instructions = Lists.newArrayList();

                        @Override
                        public boolean processLine(String s) throws IOException {
                            instructions.add(InstructionParser.parseInstruction(s).get());
                            return true;
                        }

                        @Override
                        public List<Instruction> getResult() {
                            return instructions;
                        }
                    })
            });
        }

        return files.toArray(new Object[][]{});
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void testConstructorNullOriginalFile() {
        new Diffr(null, Collections.EMPTY_LIST);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void testConstructorNullNewFile() {
        new Diffr(Collections.EMPTY_LIST, null);
    }

    @Test(dataProvider = DEFAULT_PROVIDER)
    public void testDiff(final List<String> originalFile,
                          final List<String> newFile,
                          final List<Instruction> patchFile)
            throws IOException, URISyntaxException {


        final Diffr d = new Diffr(originalFile, newFile);

        final Iterator<Instruction> actualInstructions = d.diff().iterator();

        for (final Instruction expected : patchFile) {
            final Instruction actual = actualInstructions.next();
            assertThat(InstructionComposer.composeString(actual),
                       is(InstructionComposer.composeString(expected)));
        }
    }
}
