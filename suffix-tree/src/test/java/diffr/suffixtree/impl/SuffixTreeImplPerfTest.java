package diffr.suffixtree.impl;

import com.google.caliper.Param;
import com.google.caliper.Runner;
import com.google.caliper.SimpleBenchmark;
import diffr.suffixtree.SuffixTrees;
import javolution.text.Text;
import org.testng.annotations.Test;

import java.util.List;

import static diffr.suffixtree.impl.SuffixTreeImplTestUtils.validateSuffixTree;

/**
 * Performance tests for {@link SuffixTreeImpl}.
 *
 * @author Jakub D Kozlowski
 * @since 0.1
 */
@Test(groups = "performance-tests")
public class SuffixTreeImplPerfTest {

    @Test
    public void testPerformance() {
        Runner.main(SuffixTreeImplBenchmark.class, new String[]{"--trials", "2", "-DfileLength=100,1000,10000"});
    }

    /**
     * Benchmarks various operations on {@link SuffixTreeImpl}.
     */
    public static class SuffixTreeImplBenchmark extends SimpleBenchmark {

        List<Text> lines;

        SuffixTreeImpl<Text> suffixTree;

        @Param
        int fileLength;

        @Override
        protected void setUp() {
            lines = SuffixTreeImplTestUtils.getRandomFile(fileLength, 1341376661698861013L + fileLength);
            suffixTree = SuffixTreeImpl.newSuffixTree(lines);
        }

        /**
         * Build the {@link SuffixTreeImpl} {@code reps} times.
         *
         * @param reps number of repetitions.
         */
        public void timeBuildSuffixTreeImpl(int reps) {
            for (int i = 0; i < reps; i++) {
                SuffixTrees.newSuffixTree(lines);
            }
        }

        /**
         * Build the {@link SuffixTreeImpl} and validates it {@code reps} times.
         *
         * @param reps number of repetitions.
         */
        public void timeVerifySuffixTreeImpl(int reps) {
            for (int i = 0; i < reps; i++) {
                validateSuffixTree(suffixTree);
            }
        }
    }
}
