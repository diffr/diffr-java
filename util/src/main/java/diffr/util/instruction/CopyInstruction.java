package diffr.util.instruction;

import com.google.common.collect.Range;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * {@link Instruction} for copying a set of lines from the original file.
 * <p>Example output:<br>
 * 7,10<br>
 * 15,18</p>
 *
 * @author William Martin
 * @since 0.1
 */
public class CopyInstruction implements Instruction {

    private final Range<Integer> range;

    /**
     * Default constructor.
     *
     * @param range the range of this copy.
     */
    public CopyInstruction(final Range<Integer> range) {
        checkNotNull(range);
        checkArgument(range.hasLowerBound());
        checkArgument(0 <= range.lowerEndpoint());
        this.range = range;
    }

    /**
     * Gets the range.
     *
     * @return the range.
     */
    public Range<Integer> getRange() {
        return range;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Type getType() {
        return Type.Copy;
    }
}
