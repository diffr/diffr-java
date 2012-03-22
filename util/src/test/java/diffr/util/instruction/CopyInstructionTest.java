package diffr.util.instruction;

import com.google.common.collect.Range;
import com.google.common.collect.Ranges;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Tests {@link CopyInstruction}
 *
 * @author William Martin
 * @since 0.1
 */
public class CopyInstructionTest {

    private int fromID, toID;
    private Range<Integer> range;
    private CopyInstruction copyInstruction;

    @BeforeMethod
    public void setUp() {
        fromID = 5;
        toID = 10;
        range = Ranges.closed(fromID, toID);
        copyInstruction = new CopyInstruction(range);
    }

    /**
     * Tests whether the constructor works correctly with negative from ID.
     */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testConstructorNegativeFromID() {
        new CopyInstruction(Ranges.closed(-1, toID));
    }

    /**
     * Tests whether the constructor works correctly with illegal range.
     */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testSetFromIDExceptionRange() {
        new CopyInstruction(Ranges.closed(toID + 1, toID));
    }

    /**
     * Tests whether the range getter works correctly.
     */
    @Test
    public void testRange() {
        assertThat(copyInstruction.getRange(), is(range));
    }
}
