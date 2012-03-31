package diffr.util;

import com.google.common.collect.Lists;
import org.testng.annotations.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Tests {@link diffr.util.ArgumentsProcessor}.
 *
 * @author William Martin
 * @since 1.0
 */
public class ArgumentsProcessorTest {

    /**
     * Tests whether the contains help argument method works correctly.
     */
    @Test
    public void testContainsHelpArgument() {
        final List<String> valid = Lists.newArrayList("-hElp!!----", "me", "please");
        final List<String> inValid = Lists.newArrayList("hlp", "me", "please");
        assertThat(ArgumentsProcessor.containsHelpArgument(valid.toArray(new String[0])), is(true));
        assertThat(ArgumentsProcessor.containsHelpArgument(inValid.toArray(new String[0])), is(false));
    }

    /**
     * Tests whether the extract output file method works correctly.
     */
    @Test
    public void testExtractOutputFile() {
        final List<String> valid = Lists.newArrayList("-hElp!!----", "-O", "please");
        final List<String> inValid = Lists.newArrayList("-ohlp", "me", "-o");
        assertThat(ArgumentsProcessor.extractOutputFile(valid.toArray(new String[0])).isPresent(), is(true));
        assertThat(ArgumentsProcessor.extractOutputFile(inValid.toArray(new String[0])).isPresent(), is(false));
    }
}
