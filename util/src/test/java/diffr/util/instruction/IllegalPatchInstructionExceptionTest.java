package diffr.util.instruction;

import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Tests {@link IllegalPatchInstructionException}.
 *
 * @author William Martin
 * @since 1.0
 */
public class IllegalPatchInstructionExceptionTest {

    /**
     * Tests the default message.
     */
    @Test
    public void testDefaultMessage() {
        assertThat(new IllegalPatchInstructionException().getMessage(), is(IllegalPatchInstructionException.MESSAGE));
    }

    /**
     * Tests a custom message.
     */
    @Test
    public void testMessage() {
        final String testMessage = "test message";
        assertThat(new IllegalPatchInstructionException(testMessage).getMessage(), is(testMessage));
    }
}
