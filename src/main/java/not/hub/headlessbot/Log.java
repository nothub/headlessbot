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

    public static void info(String module, String message) {
        info.accept(module + ": " + message);
    }

    public static void info(Class<?> module, String message) {
        info(module.getSimpleName(), message);
    }

    public static void warn(String module, String message) {
        warn.accept(module + ": " + message);
    }

    public static void warn(Class<?> module, String message) {
        warn(module.getSimpleName(), message);
    }

    public static void error(String module, String message) {
        error.accept(module + ": " + message);
    }

    public static void error(Class<?> module, String message) {
        error(module.getSimpleName(), message);
    }

}
