package me.darkboy.nuclear.system.util.logging;

/**
 * @author NotStxnks_
 * @version 1.0
 */
public final class StaticLog {

    private static final ConsoleLog console = new ConsoleLog();

    static {
        console.setFormat("[&3Nuclear&bCore&r] [{nuclearcore_log_level}]: {nuclearcore_log_message}");
    }

    private StaticLog() {}


    public static void log(String message) {
        console.log(message);
    }

    public static void log(ConsoleLevel level, String message) {
        console.log(level, message);
    }

    public static void debug(String message) {
        console.debug(message);
    }

    public static void info(String message) {
        console.info(message);
    }

    public static void warn(String message) {
        console.warn(message);
    }

    public static void exception(Throwable throwable) {
        console.exception(throwable);
    }

    public static void error(String message) {
        console.error(message);
    }
}