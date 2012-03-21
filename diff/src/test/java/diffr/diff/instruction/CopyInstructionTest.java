package diffr.diff.instruction;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Tests {@link CopyInstruction}
 *
 * @author William Martin
 * @since 0.1
 */
public class CopyInstructionTest {

    private int fromID;
    private int toID;
    private CopyInstruction copyInstruction;

    @BeforeMethod
    public void setUp() {
        fromID = 5;
        toID = 10;
        copyInstruction = new CopyInstruction(fromID, toID);
    }

    /**
     * Tests whether the fromID getter works correctly.
     */
    @Test
    public void testGetFromID() {
        assertThat(fromID, is(copyInstruction.getFromID()));
    }

    /**
     * Tests whether the fromID setter works correctly with negative ID.
     */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testSetFromIDExceptionNegative() {
        copyInstruction.setFromID(-1);
    }

    /**
     * Tests whether the fromID setter works correctly with incorrect range.
     */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testSetFromIDExceptionRange() {
        copyInstruction.setFromID(toID + 1);
    }

    /**
     * Tests whether the fromID setter works correctly.
     */
    @Test
    public void testSetFromID() {
        copyInstruction.setFromID(toID - 1);
        assertThat(copyInstruction.getFromID(), is(toID - 1));
    }

    /**
     * Tests whether the toID getter works correctly.
     */
    @Test
    public void testGetToID() {
        assertThat(toID, is(copyInstruction.getToID()));
    }

    /**
     * Tests whether the toID setter works correctly with incorrect range.
     */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testSetToIDExceptionRange() {
        copyInstruction.setToID(fromID - 1);
    }

    /**
     * Tests whether the fromID setter works correctly.
     */
    @Test
    public void testSetToID() {
        copyInstruction.setToID(toID + 1);
        assertThat(copyInstruction.getToID(), is(toID + 1));
    }
}
