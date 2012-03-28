package diffr.diff;

import com.google.common.collect.Lists;
import diffr.suffixtree.SuffixTree;
import diffr.suffixtree.SuffixTree.Matcher;
import diffr.suffixtree.SuffixTrees;
import diffr.util.instruction.CopyInstruction;
import diffr.util.instruction.InsertInstruction;
import diffr.util.instruction.Instruction;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Generates a list of {@link Instruction}s to transform original file into a new file.
 *
 * @author Sarina Gurung
 * @author Jakub D Kozlowski
 * @since 0.3
 */
public final class Diffr {

    private final List<String> originalFile;

    private final List<String> newFile;

    /**
     * Default constructor.
     *
     * @param originalFile original file to be transform.
     * @param newFile      new file to transform {@code originalFile} to.
     *
     * @throws NullPointerException if any parameter is null.
     */
    public Diffr(final List<String> originalFile, final List<String> newFile) {
        this.originalFile = checkNotNull(originalFile);
        this.newFile = checkNotNull(newFile);
    }

    /**
     * Gets the list of {@link Instruction}s to transform {@code originalFile} to {@code newFile}.
     *
     * @return list of {@link Instruction}s.
     */
    public List<Instruction> diff() {

        final List<Instruction> instructions = Lists.newArrayList();
        final SuffixTree<String> suffixTree = SuffixTrees.newSuffixTree(this.originalFile);

        Matcher<String> matcher = suffixTree.matcher();

        for (final String newFileLine : newFile) {

            if (!matcher.matchNext(newFileLine).isMatched()) {
                if (!matcher.isRoot()) {
                    instructions.add(new CopyInstruction(matcher.range()));
                }
                instructions.add(new InsertInstruction(newFileLine));
                matcher = suffixTree.matcher();
            }
        }

        if (!matcher.isRoot()) {
            instructions.add(new CopyInstruction(matcher.range()));
        }

        return instructions;
    }

}
