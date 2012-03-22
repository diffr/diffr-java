package diffr.util.instruction;

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
     * Tests whether an exception is thrown when the constructor is invoked with null text.
     */
    @Test(expectedExceptions = NullPointerException.class)
    public void testNullConstructorArgument() {
        new InsertInstruction(null);
    }

    /**
     * Tests whether the text getter works correctly.
     */
    @Test
    public void testGetText() {
        assertThat(text, is(insertInstruction.getText()));
    }
}
