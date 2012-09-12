package org.tulonsae.mc.plugins;

import java.util.logging.Logger;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Utility class for TulAdmin.
 * <p />
 * Contains logging utility methods.
 *
 * @author Tulonsae
 */
public class Util {

    // log message prefixes
    private static final String logPrefix = "[TulAdmin]";
    private static final String debugPrefix = "DEBUG:";

    // debug flag
    private static boolean debug = false;

    // Bukkit server logger
    private static final Logger log = Logger.getLogger("Minecraft");


    /**
     * Set the debug flag.
     *
     * @param flag boolean value for flag
     */
    protected static void setDebug(boolean flag) {
        debug = flag;

        logInfo("Set debug flag to: " + flag);
    }

    /**
     * Send a debug log message to the console, if debug flag is true.
     *
     * @param message message to log
     */
    protected static void logDebug(String message) {
        if (debug) {
            log.info(logPrefix + " " + debugPrefix + " " + message);
        }
    }

    /**
     * Send an info log message to the console.
     *
     * @param message message to log
     */
    protected static void logInfo(String message) {
        log.info(logPrefix + " " + message);
    }

    /**
     * Send a warning log message to the console.
     *
     * @param message message to log
     */
    protected static void logWarning(String message) {
        log.warning(logPrefix + " " + message);
    }

    /**
     * Send a severe log message to the console.
     *
     * @param message message to log
     */
    protected static void logSevere(String message) {
        log.severe(logPrefix + " " + message);
    }

    /**
     * Send a message to player and console.
     *
     * @param sender source of the command
     * @param message string message to send/log
     */
    protected static void sendMessage(CommandSender sender, String message) {
        Player player = null;

        // check for console or player
        if (sender instanceof Player) {
            player = (Player) sender;
        }

        // if a player, then make sure to log it
        if (player != null) {
            logInfo("Player " + player + " is " + message);
        }

        // if the console, then it will get this so no need for duplicate
        sender.sendMessage(message);
    }
}
