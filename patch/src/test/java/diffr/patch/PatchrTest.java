package diffr.patch;

import com.google.common.collect.Lists;
import com.google.common.io.Files;
import com.google.common.io.Resources;
import diffr.util.instruction.IllegalPatchInstructionException;
import org.testng.annotations.Test;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Tests {@link Patchr}
 */
public class PatchrTest {

    /**
     * Tests patch on ready made files.
     */
    @Test
    public void testExistingFiles() throws Exception {
        final File originalFile = new File(Resources.getResource("files/copyright.txt").toURI());
        final File patchFile = new File(Resources.getResource("patches/cp.patch").toURI());
        final List<String> firstFileStrings = Files.readLines(originalFile, Charset.defaultCharset());
        final List<String> patchFileStrings = Files.readLines(patchFile, Charset.defaultCharset());

        new Patchr(firstFileStrings, patchFileStrings).patch();
    }

    /**
     * Tests whether an artificial example works.
     */
    @Test
    public void testPatch() throws IllegalPatchInstructionException, IllegalPatchFileException {
        final List<String> original = Lists.newArrayList("hello world!", "2", "");
        final List<String> next = Lists.newArrayList("hello world.", "2", "3", "2", "", "hello world!");
        final List<String> patch = Lists.newArrayList("> hello world.", "1,1", "> 3", "1,2", "0,0");

        final Patchr patchr = new Patchr(original, patch);
        final List<String> result = patchr.patch();
        System.out.println(result.toString());
        assertThat(next.size(), is(result.size()));
        for (int i = 0; i < next.size(); i++) {
            assertThat(result.get(i), is(next.get(i)));
        }
    }

    /**
     * Tests whether the Constructor throws an exception when given an illegal instruction.
     */
    @Test(expectedExceptions = IllegalPatchFileException.class)
    public void testIllegalInstructionConstructor() throws IllegalPatchFileException {
        final List<String> broken = Lists.newArrayList("brokenInstruction");
        new Patchr(new ArrayList<String>(), broken);
    }
}
