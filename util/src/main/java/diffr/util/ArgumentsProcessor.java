package diffr.util;

import com.google.common.base.Optional;

/**
 * Processes command line arguments.
 *
 * @author William Martin
 * @since 1.0
 */
public class ArgumentsProcessor {

    /**
     * Checks if the given arguments contain a call for help.
     *
     * @param args the arguments to check for help.
     * @return true if the given arguments contain a call for help.
     */
    public static boolean containsHelpArgument(final String... args) {
        for (final String s : args) {
            if (s.equalsIgnoreCase("--help")
                    || s.equalsIgnoreCase("-help")) {
                return true;
            }
        }
        return false;
    }

    /**
     * Extract the output file location from the given arguments.
     *
     * @param args the arguments to extract the output file location from.
     * @returnthe output file location from the given arguments.
     */
    public static Optional<String> extractOutputFile(final String... args) {
        String result = null;
        for (int i = 0; i < args.length - 1; i++) {
            final String s = args[i];
            if (s.equalsIgnoreCase("-o")) {
                result = args[i + 1];
            }
        }
        return Optional.fromNullable(result);
    }
}
