package diffr.suffixtree.impl;

import com.google.caliper.Param;
import com.google.caliper.Runner;
import com.google.caliper.SimpleBenchmark;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import javolution.text.Text;
import org.testng.annotations.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Performance tests for {@link SuffixTreeImpl}.
 *
 * @author Jakub D Kozlowski
 * @since 0.1
 */
public class SuffixTreeImplPerfTest {

    @Test(groups = {"performance-tests"})
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
            lines = SuffixTreeImplTestUtils.getRandomFile(fileLength);
            suffixTree = SuffixTreeImpl.newSuffixTree(lines);
        }

        /**
         * Build the {@link SuffixTreeImpl} {@code reps} times.
         *
         * @param reps number of repetitions.
         */
        public void timeBuildSuffixTreeImpl(int reps) {
            for (int i = 0; i < reps; i++) {
                SuffixTreeImpl.newSuffixTree(lines);
            }
        }

        /**
         * Build the {@link SuffixTreeImpl} and validates it {@code reps} times.
         *
         * @param reps number of repetitions.
         */
        public void timeVerifySuffixTreeImpl(int reps) {
            for (int i = 0; i < reps; i++) {

                for (int suffixIndex = 0; suffixIndex < lines.size(); suffixIndex++) {

                    final List<Text> suffix = (suffixIndex != (lines.size() - 1)) ?
                            lines.subList(suffixIndex, lines.size() - 1) :
                            Lists.newArrayList(lines.get(lines.size() - 1));

                    Node curNode = suffixTree.getRoot();
                    int j = 0;
                    int p = 0;
                    Optional<Edge> curEdge = suffixTree.getEdge(NodeKey.lookup(curNode, suffix.get(j)));
                    assertThat(curEdge.isPresent(), is(true));

                    while (j < suffix.size()) {

                        assertThat(suffix.get(j), is(lines.get(curEdge.get().getRange().lowerEndpoint() + p)));
                        j++;
                        p++;

                        if (!curEdge.get().getRange().apply(
                                curEdge.get().getRange().lowerEndpoint() + p) &&
                                j < suffix.size()) {
                            curNode = curEdge.get().getChildNode();
                            curEdge = suffixTree.getEdge(NodeKey.lookup(curNode, suffix.get(j)));
                            assertThat(curEdge.isPresent(), is(true));
                            p = 0;

                        }
                    }

                }
            }
        }

    }
}
