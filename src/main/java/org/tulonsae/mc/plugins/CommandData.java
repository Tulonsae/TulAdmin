package org.tulonsae.mc.plugins;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.World;

/**
 * Represents the data for the command.
 *
 * @author Tulonsae
 */
public class CommandData {

    private TulAdmin plugin;
    private CommandSender sender;

    private static final String optDelim = ":";
    private static final String posDelim = ",";

    protected static final int chunkSize = 16;
    protected static final int numChunks = 32;
    protected static final int regionSize = numChunks * chunkSize;

    protected Player player = null;
    protected World world = null;

    // x and z positions
    protected int xPosStart = 0;
    protected int zPosStart = 0;
    protected int xPosEnd = 0;
    protected int zPosEnd = 0;

    protected boolean border = false;
    // xborder is a east/west border
    protected boolean xborder = false;
    // zborder is a north/south border
    protected boolean zborder = false;
    // corner is a single column in a corner
    protected boolean corner = false;
    // inner is a border just inside the area
    protected boolean inner = false;
    // outer is a border just outside the area
    protected boolean outer = false;
    // border wall height
    protected int height = 63;
    // border wall thickness
    protected int thickness = 1;
    // name of border type
    protected String borderType = "none";
    // border material type
    protected int material = 7;

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
     * @param sender source of the command
     */
    public CommandData(TulAdmin plugin, CommandSender sender) {
        this.plugin = plugin;
        this.sender = sender;

        // check for console or player
        if (sender instanceof Player) {
            player = (Player) sender;
            // set default world to player's world
            world = player.getWorld();
        } else {
            // set default world to primary world
            // TODO
        }
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
                world = plugin.getServer().getWorld(value);
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

            // border wall height
            if (option.equals("height") || option.equals("h")) {
                height = Integer.parseInt(value);
            }

            // border wall thickness
            if (option.equals("thick") || option.equals("thickness") || option.equals("k")) {
                thickness = Integer.parseInt(value);
                if (thickness < 1) {
                    return false;
                }
            }

            // border wall material
            if (option.equals("material") || option.equals("mat") || option.equals("m")) {
                material = Integer.parseInt(value);
            }

            // the border type
            if (option.equals("border") || option.equals("b")) {
                Util.logDebug("border option = " + value);
                border = true;
                if (value.equals("north") || value.equals("n")) {
                    xborder = true;
                    borderType = "northern";
                } else if (value.equals("south") || value.equals("s")) {
                    xborder = true;
                    borderType = "southern";
                } else if (value.equals("east") || value.equals("e")) {
                    zborder = true;
                    borderType = "eastern";
                } else if (value.equals("west") || value.equals("w")) {
                    zborder = true;
                    borderType = "western";
                } else if (value.equals("northwest") || value.equals("nw")) {
                    corner = true;
                    borderType = "northwestern";
                } else if (value.equals("northeast") || value.equals("ne")) {
                    corner = true;
                    borderType = "northeastern";
                } else if (value.equals("southeast") || value.equals("se")) {
                    corner = true;
                    borderType = "southeastern";
                } else if (value.equals("southwest") || value.equals("sw")) {
                    corner = true;
                    borderType = "southwestern";
                } else if (value.equals("inner") || value.equals("inside") || value.equals("i")) {
                    inner = true;
                    borderType = "inner";
                } else if (value.equals("outer") || value.equals("outside") || value.equals("o")) {
                    outer = true;
                    borderType = "outer";
                } else {
                    return false;
                }
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
        if (!chunk && !block) {
            region = true;
            coordType = "region";
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
