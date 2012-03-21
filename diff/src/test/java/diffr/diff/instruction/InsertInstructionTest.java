package diffr.diff.instruction;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Tests {@link InsertInstruction}
 *
 * @author William Martin
 * @since 0.1
 */
public class InsertInstructionTest {

    private String text;
    private InsertInstruction insertInstruction;

    @BeforeMethod
    public void setUp() {
        text = "hello world";
        insertInstruction = new InsertInstruction(text);
    }

    /**
     * Tests whether the text getter works correctly.
     */
    @Test
    public void testGetText() {
        assertThat(text, is(insertInstruction.getText()));
    }

    /**
     * Tests whether the text setter works correctly with null text.
     */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testSetTextExceptionNegative() {
        insertInstruction.setText(null);
    }
}
