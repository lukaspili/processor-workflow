package processorworkflow;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class Logger {

    private static String TAG;
    private static boolean enabled;

    public static void init(String tag, boolean enabled) {
        Logger.TAG = tag;
        Logger.enabled = enabled;
    }

    public static void d(String message, Object... format) {
        if (TAG == null) {
            throw new IllegalStateException("Must call Logger.init() before using logger");
        }

        if (!enabled) {
            return;
        }

        if (format != null) {
            message = String.format(message, format);
        }

        System.out.println(TAG + " - " + message);
    }
}
