package org.tulonsae.mc.plugins;

import java.util.logging.Logger;

/**
 * Logger utility class for NullRegion.
 *
 * @author Tulonsae
 */
public class LogUtil {

    /**
     * Constants
     */
    private static final String logPrefix = "[NullRegion]";

    /**
     * Bukkit server logger
     */
    private static final Logger log = Logger.getLogger("Minecraft");


    /**
     * Send an info level log to the console.
     */
    public static void info(String msg) {
        log.info(logPrefix + " " + msg);
    }

    /**
     * Send a warning level log to the console.
     */
    public static void warning(String msg) {
        log.warning(logPrefix + " " + msg);
    }

    /**
     * Send a severe level log to the console.
     */
    public static void severe(String msg) {
        log.severe(logPrefix + " " + msg);
    }
}
