package diffr.diff;

/**
 * Main entry point to diffr's DIFF tool.
 *
 * <p>
 * Expects two arguments:
 * <ul>
 * <li>&lt;original-file&gt; - The original file to diff.</li>
 * <li>&lt;new-file&gt; - The new version of the original file to diff.</li>
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
        System.out.println("diffr-diff <original-file> <new-file>");
    }

    /**
     * Runs the diff tool on two files.
     *
     * @param args arguments to this tool.
     */
    public static void main(String... args) {

        if (args.length != 2) {
            printUsage();
            System.exit(-1);
        }

        System.exit(0);
    }
}
