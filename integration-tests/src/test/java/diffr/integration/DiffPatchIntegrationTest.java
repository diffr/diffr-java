package diffr.integration;

import com.google.common.io.Files;
import com.google.common.io.Resources;
import diffr.patch.IllegalPatchFileException;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Tests diff/patch integration.
 *
 * @author Jakub D Kozlowski
 * @since 1.0
 */
public class DiffPatchIntegrationTest {

    @Test
    public void testKernel01ToKernel26() throws IllegalPatchFileException, URISyntaxException, IOException {
        testDiffrPatchr("kernel01.txt", "kernel26.txt");
    }

    @Test
    public void testKernel26ToKernel01() throws IllegalPatchFileException, URISyntaxException, IOException {
        testDiffrPatchr("kernel26.txt", "kernel01.txt");
    }

    @Test
    public void testKernel01ToKernel33() throws IllegalPatchFileException, URISyntaxException, IOException {
        testDiffrPatchr("kernel01.txt", "kernel33.txt");
    }

    @Test
    public void testKernel33ToKernel01() throws IllegalPatchFileException, URISyntaxException, IOException {
        testDiffrPatchr("kernel33.txt", "kernel01.txt");
    }

    @Test
    public void testKernel26ToKernel33() throws IllegalPatchFileException, URISyntaxException, IOException {
        testDiffrPatchr("kernel26.txt", "kernel33.txt");
    }

    @Test
    public void testKernel33ToKernel26() throws IllegalPatchFileException, URISyntaxException, IOException {
        testDiffrPatchr("kernel33.txt", "kernel26.txt");
    }

    /**
     * Runs diffr on {@code originalFileName} and {@code newFileName}, runs patchr on the resulting patch file and
     * {@code originalFileName} and compares the result to {@code newFileName}.
     *
     * @param originalFileName file name of the original file.
     * @param newFileName      file name of the new file.
     *
     * @throws IOException        if there is a problem reading or writing the files.
     * @throws URISyntaxException if the file names cannot be found.
     */
    public static void testDiffrPatchr(final String originalFileName, final String newFileName)
            throws IOException, URISyntaxException {

        final File originalFile = getFile(originalFileName);
        final File newFile = getFile(newFileName);

        final File tmpPatchFile = File.createTempFile("diffr", "patch", Files.createTempDir());
        final File tmpNewFile = File.createTempFile("diffr", "new", Files.createTempDir());

        diffr.diff.Main.run(originalFile.getAbsolutePath(), newFile.getAbsolutePath(), "-o",
                            tmpPatchFile.getAbsolutePath());

        diffr.patch.Main.run(originalFile.getAbsolutePath(), tmpPatchFile.getAbsolutePath(), "-o",
                             tmpNewFile.getAbsolutePath());

        assertThat(Files.equal(newFile, tmpNewFile), is(true));
    }

    /**
     * Gets the {@code fileName} from the classloader.
     *
     * @param fileName name of file to get.
     *
     * @return {@code fileName} from the classloader.
     */
    public static File getFile(final String fileName) throws URISyntaxException, IOException {
        return new File(Resources.getResource(fileName).toURI());
    }
}
