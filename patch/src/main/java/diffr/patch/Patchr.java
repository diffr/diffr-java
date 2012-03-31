package diffr.patch;

import com.google.common.collect.Range;
import diffr.util.instruction.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Generates a list of Strings by applying a patch to the original file.
 *
 * @author Amaury Couste
 * @author William Martin
 * @since 0.3
 */
public class Patchr {

    private final List<String> originalFile;
    private final List<Instruction> instructions;

    /**
     * Default constructor.
     *
     * @param originalFile the original file.
     * @param patchFile    the patch file.
     * @throws IllegalPatchFileException if there was an error reading the patch file.
     */
    public Patchr(final List<String> originalFile, final List<String> patchFile) throws IllegalPatchFileException {
        this.originalFile = originalFile;
        try {
            this.instructions = Instructions.readInstructions(patchFile);
        } catch (final IllegalPatchInstructionException ipe) {
            throw new IllegalPatchFileException("Error. Illegal patch file: " + ipe.getMessage());
        }
    }

    /**
     * Applies the patch file to the original file.
     *
     * @return a list of strings representing the patched file.
     */
    public List<String> patch() {
        final List<String> patchedFile = new ArrayList<String>();
        for (final Instruction instruction : instructions) {
            switch (instruction.getType()) {
                case Copy:
                    final CopyInstruction copyInstruction = (CopyInstruction) instruction;
                    final Range<Integer> range = copyInstruction.getRange();
                    patchedFile.addAll(originalFile.subList(range.lowerEndpoint(), 1 + range.upperEndpoint()));
                    break;
                case Insert:
                    final InsertInstruction insertInstruction = (InsertInstruction) instruction;
                    patchedFile.add(insertInstruction.getText());
            }
        }
        return patchedFile;
    }

}
