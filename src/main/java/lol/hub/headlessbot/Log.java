package lol.hub.headlessbot;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Log {

    private static final Logger log = LogManager.getLogger("BOT");

    private static String fmt(String format, Object[] args) {
        return String.format(format, args);
    }

    public static void debug(String message) {
        log.debug(message);
    }

    public static void debug(String format, String... args) {
        log.debug(fmt(format, args));
    }

    public static void info(String message) {
        log.info(message);
    }

    public static void info(String format, String... args) {
        log.info(fmt(format, args));
    }

    public static void warn(String message) {
        log.warn(message);
    }

    public static void warn(String format, String... args) {
        log.warn(fmt(format, args));
    }

    public static void error(String message) {
        log.error(message);
    }

    public static void error(String format, String... args) {
        log.error(fmt(format, args));
    }

}
