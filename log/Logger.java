package log;

public class Logger {
    private static boolean debug = false;

    public static void setDebug(boolean d) {
        debug = d;
    }

    public static void info(String message) {
        if (debug) {
            System.out.println(message);
        }
    }

    public static void error(String message, Exception e) {
        System.err.println(message);
        e.printStackTrace();
    }
}
