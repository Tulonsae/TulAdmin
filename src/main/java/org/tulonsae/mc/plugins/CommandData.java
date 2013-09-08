package org.tulonsae.mc.plugins;

import org.bukkit.command.CommandSender;
import org.bukkit.World;

/**
 * Represents the data for the command.
 *
 * @author Tulonsae
 */
public class CommandData {

    private TulAdmin plugin;

    private static final String optDelim = ":";
    private static final String posDelim = ",";
    private static final String radDelim = ";";

    protected static final int chunkSize = 16;
    protected static final int numChunks = 32;
    protected static final int regionSize = numChunks * chunkSize;

    protected String worldName = null;

    // x and z positions
    protected int xPosStart = 0;
    protected int zPosStart = 0;
    protected int xPosEnd = 0;
    protected int zPosEnd = 0;

    // coordinate refers to a region
    protected boolean region = false;
    // coordinate refers to a chunk
    protected boolean chunk = false;
    // coordinate refers to a block
    protected boolean block = false;
    // name of coordinate type
    protected String coordType;


    /**
     * Constructs the command data object.
     *
     * @param plugin this plugin object
     */
    public CommandData(TulAdmin plugin) {
        this.plugin = plugin;
    }

    /**
     * Parse command arguments.
     * <p />
     * Compass directions use the new ones (sun rises in the east).
     *
     * @param args command arguments
     * @return returns false if argument error, otherwise returns true
     */
    protected boolean parseArgs(String[] args) {

        if (args.length == 0) {
            return false;
        }

        for (String arg : args) {
            Util.logDebug("arg = " + arg);
            String[] token = arg.split(optDelim);

            if (token.length != 2) {
                return false;
            }

            String option = token[0].toLowerCase();
            String value = token[1].toLowerCase();

            // the world
            if (option.equals("world") || option.equals("w")) {
                Util.logDebug("world option = " + value);
                worldName = value;
            }

            // a single coordinate
            if (option.equals("position") || option.equals("pos") || option.equals("p")) {
                Util.logDebug("position option = " + value);
                int[] pos = parsePos(value);
                if (pos == null) {
                    return false;
                }
                xPosStart = pos[0];
                zPosStart = pos[1];
                xPosEnd = xPosStart;
                zPosEnd = zPosStart;
            }

            if(option.equals("radius") || option.equals("rad") || option.equals("square") || option.equals("sq")) {
               Util.logDebug("radius option = " + value);
               token = value.split(radDelim);
               int radius = Integer.parseInt(token[0].toLowerCase());
               if(radius < 0) {
                  return false;
               }
               int xPosCenter = 0;
               int zPosCenter = 0;
               if (token.length > 1) {
                   int[] pos = parsePos(token[1].toLowerCase());
                   if (pos == null) {
                       return false;
                   }
                   xPosCenter = pos[0];
                   zPosCenter = pos[1];
               }

               xPosStart = xPosCenter - radius;
               zPosStart = zPosCenter - radius;
               xPosEnd = xPosCenter + radius;
               zPosEnd = zPosCenter + radius;
            }

            // a starting coordinate
            if (option.equals("start") || option.equals("s")) {
                Util.logDebug("start option = " + value);
                int[] pos = parsePos(value);
                if (pos == null) {
                    return false;
                }
                xPosStart = pos[0];
                zPosStart = pos[1];
            }

            // an ending coordinate
            if (option.equals("end") || option.equals("e")) {
                Util.logDebug("end option = " + value);
                int[] pos = parsePos(value);
                if (pos == null) {
                    return false;
                }
                xPosEnd = pos[0];
                zPosEnd = pos[1];
            }

            // the coordinate type
            if (option.equals("type") || option.equals("t")) {
                Util.logDebug("type option = " + value);
                if (value.equals("region") || value.equals("reg") || value.equals("r")) {
                    coordType = "region";
                    region = true;
                } else if (value.equals("chunk") || value.equals("c")) {
                    chunk = true;
                    coordType = "chunk";
                } else if (value.equals("block") || value.equals("b")) {
                    block = true;
                    coordType = "block";
                } else {
                    return false;
                }
            }
        }

        // set default coordinate type
        if (!chunk && !region) {
            block = true;
            coordType = "block";
        }

        // make sure start is upper left and end is lower right
        if (xPosStart > xPosEnd) {
            int temp = xPosStart;
            xPosStart = xPosEnd;
            xPosEnd = temp;
        }
        if (zPosStart > zPosEnd) {
            int temp = zPosStart;
            zPosStart = zPosEnd;
            zPosEnd = temp;
        }

        if (worldName == null) {
            worldName = "world";
        }
        Util.logDebug("world name = " + worldName);

        return true;
    }

    /**
     * Parse a position option.
     *
     * @param pos string containing the position value
     * @return null if not valid, otherwise a list with x and z
     */
    private int[] parsePos(String pos) {
        String[] token = pos.split(posDelim);

        if (token.length != 2) {
            return null;
        }

        int[] result = new int[2];
        result[0] = Integer.parseInt(token[0]);
        result[1] = Integer.parseInt(token[1]);

        return result;
    }
}
