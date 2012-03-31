package diffr.patch;

import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Tests {@link diffr.patch.IllegalPatchFileException}.
 *
 * @author William Martin
 * @since 1.0
 */
public class IllegalPatchFileExceptionTest {

    /**
     * Tests the default message.
     */
    @Test
    public void testDefaultMessage() {
        assertThat(new IllegalPatchFileException().getMessage(), is(IllegalPatchFileException.MESSAGE));
    }

    /**
     * Tests a custom message.
     */
    @Test
    public void testMessage() {
        final String testMessage = "test message";
        assertThat(new IllegalPatchFileException(testMessage).getMessage(), is(testMessage));
    }
}
