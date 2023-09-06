package lol.hub.headlessbot;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Log {
    private static final String prefix = "[BOT] ";
    private static final Logger log = LogManager.getLogger("BOT");

    private static String fmt(String format, Object[] args) {
        return String.format(format, args);
    }

    public static void info(String message) {
        log.info(prefix + message);
    }

    public static void info(String format, String... args) {
        info(fmt(format, args));
    }

    public static void warn(String message) {
        log.warn(prefix + message);
    }

    public static void warn(String format, String... args) {
        warn(fmt(format, args));
    }

    public static void error(String message) {
        log.error(prefix + message);
    }

    public static void error(String format, String... args) {
        error(fmt(format, args));
    }

}
