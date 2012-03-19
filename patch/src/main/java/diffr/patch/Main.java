package diffr.patch;

/**
 * Main entry point to diffr's PATCH tool.
 *
 * <p>
 * Expects two arguments:
 * <ul>
 * <li>&lt;original-file&gt; - The original file.</li>
 * <li>&lt;diff-file&gt; - The diff file created using the diff tool.</li>
 * </ul>
 * </p>
 *
 * @author Jakub D Kozlowski
 * @since 0.1
 */
public final class Main {

    /**
     * Prints the usage of this tool.
     */
    private static void printUsage() {
        System.out.println("diffr-patch <original-file> <diff-file>");
    }

    /**
     * Runs the patch tool on the original file.
     *
     * @param args arguments to this tool.
     */
    public static void main(String... args) {

        if (args.length != 2) {
            printUsage();
            System.exit(-1);
        }
        
        Patchr p = new Patchr(args[0], args[1]);
        p.patch();
        
        System.exit(0);
    }
}
