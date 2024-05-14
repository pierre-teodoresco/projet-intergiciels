package log;

public class Logger {
    private static boolean debug = false;

    public static void setDebug(boolean d) {
        debug = d;
    }

    public static void log(String message) {
        if (debug) {
            System.out.println(message);
        }
    }
}
