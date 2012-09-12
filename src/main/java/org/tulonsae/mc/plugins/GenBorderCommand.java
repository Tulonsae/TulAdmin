package org.tulonsae.mc.plugins;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Creates a border for a region or series of regions.
 *
 * @author Tulonsae
 */
public class GenBorderCommand implements CommandExecutor {

    private TulAdmin plugin;

    /**
     * Make a border in a region.
     *
     * @param plugin this plugin object
     */
    public GenBorderCommand(TulAdmin plugin) {
        this.plugin = plugin;
    }

    /**
     * Called when the command is sent.
     *
     * @param sender source of the command
     * @param cmd command to be executed
     * @param label command alias
     * @param args command arguments
     */
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        // must have arguments
        if (args.length == 0) {
            return false;
        }

        CommandData data = new CommandData(plugin, sender);
        if (!data.parseArgs(args)) {
            return false;
        }

        // must have a border type
        if (!data.border) {
            Util.sendMessage(sender, "Border type not specified.");
            return false;
        }

        // TODO - implement thickness
        if (data.thickness > 1) {
            Util.sendMessage(sender, "Thickness greater than 1 not implemented yet.");
            return true;
        }

        // TODO - implement corners
        if (data.corner) {
            Util.sendMessage(sender, "Corners not implemented yet.");
            return true;
        }

        Util.sendMessage(sender, "Starting to make " + data.borderType + " border with thickness " + data.thickness + " for world " + data.world.getName() + " at " + data.coordType + " positions:" + data.xPosStart + "," + data.zPosStart + " thru " + data.xPosEnd + "," + data.zPosEnd);

        // create outer border
        if (data.outer) {
            int xStart = data.xPosStart;
            int zStart = data.zPosStart;
            int xEnd = data.xPosEnd;
            int zEnd = data.zPosEnd;
            if (data.block) {
                xStart = xStart - 1;
                zStart = zStart - 1;
                xEnd = xEnd + 1;
                zEnd = zEnd + 1;
            }
            if (data.chunk) {
                xStart = (xStart * data.chunkSize) - 1;
                zStart = (zStart * data.chunkSize) - 1;
                xEnd = (xEnd + 1) * data.chunkSize;
                zEnd = (zEnd + 1) * data.chunkSize;
            }
            if (data.region) {
                xStart = (xStart * data.chunkSize * data.numChunks) - 1;
                zStart = (zStart * data.chunkSize * data.numChunks) - 1;
                xEnd = (xEnd + 1) * data.regionSize;
                zEnd = (zEnd + 1) * data.regionSize;
            }

            Util.logDebug("Starting coords: " + xStart + "," + zStart);
            Util.logDebug("Ending coords: " + xEnd + "," + zEnd);

            // this message is to reassure the sender so don't log it
            sender.sendMessage("Northern wall: " + xStart + "," + zStart + " thru " + xEnd + "," + zStart);
            for (int y = 0; y < data.height; y++) {
                for (int x = xStart; x < xEnd; x++) {
                    data.world.getBlockAt(x, y, zStart).setTypeId(data.material);
                }
            }

            // this message is to reassure the sender so don't log it
            sender.sendMessage("Southern wall: " + xStart + "," + zEnd + " thru " + xEnd + "," + zEnd);
            for (int y = 0; y < data.height; y++) {
                for (int x = xStart; x < xEnd; x++) {
                    data.world.getBlockAt(x, y, zEnd).setTypeId(data.material);
                }
            }

            // this message is to reassure the sender so don't log it
            sender.sendMessage("Western wall: " + xStart + "," + zStart + " thru " + xStart + "," + zEnd);
            for (int y = 0; y < data.height; y++) {
                for (int z = zStart; z < zEnd; z++) {
                    data.world.getBlockAt(xStart, y, z).setTypeId(data.material);
                }
            }

            // this message is to reassure the sender so don't log it
            sender.sendMessage("Eastern wall: " + xEnd + "," + zStart + " thru " + xEnd + "," + zEnd);
            for (int y = 0; y < data.height; y++) {
                for (int z = zStart; z < zEnd; z++) {
                    data.world.getBlockAt(xEnd, y, z).setTypeId(data.material);
                }
            }
        }

/*
        int xChunk = xRegion * numChunks;
        int zChunk = zRegion * numChunks;

        if (horizontal) {
            int xStart = xChunk * chunkSize;
            int xEnd = xStart + regionSize;
            int z = zChunk * chunkSize + pos;
            for (int x = xStart; x < xEnd; x++) {
                for (int y = 0; y < wallHeight; y++) {
                    world.getBlockAt(x, y, z).setTypeId(7);;
                }
            }
        }

        if (vertical) {
            int zStart = zChunk * chunkSize;
            int zEnd = zStart + regionSize;
            int x = xChunk * chunkSize + pos;
            for (int z = zStart; z < zEnd; z++) {
                for (int y = 0; y < wallHeight; y++) {
                    world.getBlockAt(x, y, z).setTypeId(7);
                }
            }
        }

        if (corner) {
            int x = xChunk * chunkSize + xPos;
            int z = zChunk * chunkSize + zPos;
            for (int y = 0; y < wallHeight; y++) {
                world.getBlockAt(x, y, z).setTypeId(7);
            }
        }
*/

        Util.sendMessage(sender, "Finished making " + data.borderType + " border with thickness " + data.thickness + " for world " + data.world.getName() + " at " + data.coordType + " positions:" + data.xPosStart + "," + data.zPosStart + " thru " + data.xPosEnd + "," + data.zPosEnd);
        return true;
    }
}
