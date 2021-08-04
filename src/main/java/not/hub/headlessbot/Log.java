package not.hub.headlessbot;

import org.apache.logging.log4j.Logger;

import java.util.function.Consumer;

public class Log {

    private static Consumer<String> info = System.out::println;
    private static Consumer<String> warn = System.err::println;
    private static Consumer<String> error = System.err::println;

    public static void setLogger(Logger modLog) {
        info = modLog::info;
        warn = modLog::warn;
        error = modLog::error;
    }

    public static void info(String sender, String message) {
        info.accept(sender + ": " + message);
    }

    public static void info(Class<?> sender, String message) {
        info(sender.getSimpleName(), message);
    }

    public static void warn(String sender, String message) {
        warn.accept(sender + ": " + message);
    }

    public static void warn(Class<?> sender, String message) {
        warn(sender.getSimpleName(), message);
    }

    public static void error(String sender, String message) {
        error.accept(sender + ": " + message);
    }

    public static void error(Class<?> sender, String message) {
        error(sender.getSimpleName(), message);
    }

}
