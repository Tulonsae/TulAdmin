package org.tulonsae.mc.plugins;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.World;

/**
 * Generates a region or series of regions, with an optional bedrock border.
 *
 * @author Tulonsae
 */
public class GenRegion implements CommandExecutor {

    private TulAdmin plugin;

    private int chunkSize = 16;
    private int numChunks = 32;
    private int regionSize = numChunks * chunkSize;

    // make this configurable
    private int height = 63;
    private int wallHeight = 129;

    private Player player = null;
    private World world = null;

    /**
     * Generate a region or series of regions.
     *
     * @param plugin this plugin object
     */
    public GenRegion(TulAdmin plugin) {
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

        // must have world, x, and z defined
        if (args.length < 3) {
            return false;
        }

        // check for console or player
        if (sender instanceof Player) {
            player = (Player) sender;
            // set default world to be player's world
            world = player.getWorld();
        }

        String worldName = args[0];
        int xRegion = Integer.parseInt(args[1]);
        int zRegion = Integer.parseInt(args[2]);

        world = plugin.getServer().getWorld(worldName);

        int xChunk = xRegion * numChunks;
        int zChunk = zRegion * numChunks;

/*
        int pos = 0;
        int xPos = 0;
        int zPos = 0;
        boolean horizontal = false;
        boolean vertical = false;
        boolean corner = false;

        // optional 3rd argument, uses new compass directions
        if (args.length == 3) {
            if (args[2].equalsIgnoreCase("n")) {
                horizontal = true;
                pos = 0;
            } else if (args[2].equalsIgnoreCase("s")) {
                horizontal = true;
                pos = regionSize - 1;
            } else if (args[2].equalsIgnoreCase("e")) {
                vertical = true;
                pos = regionSize - 1;
            } else if (args[2].equalsIgnoreCase("w")) {
                vertical = true;
                pos = 0;
            } else if (args[2].equalsIgnoreCase("nw")) {
                corner = true;
                xPos = 0;
                zPos = 0;
            } else if (args[2].equalsIgnoreCase("ne")) {
                corner = true;
                xPos = regionSize - 1;
                zPos = 0;
            } else if (args[2].equalsIgnoreCase("se")) {
                corner = true;
                xPos = regionSize - 1;
                zPos = regionSize - 1;
            } else if (args[2].equalsIgnoreCase("sw")) {
                corner = true;
                xPos = 0;
                zPos = regionSize - 1;
            } else {
                return false;
            }
        }
*/

        sendMessages(sender, "Starting to generate region " + xRegion + "," + zRegion + " for world " + worldName);

        for (int xc = xChunk; xc < (xChunk + numChunks); xc++) {
            for (int zc = zChunk; zc < (zChunk + numChunks); zc++) {
                // this message is to reassure the sender so don't log it
                sender.sendMessage("Chunk " + xc + "," + zc);
                int xBlock = xc * chunkSize;
                int zBlock = zc * chunkSize;
                world.getBlockAt(xBlock, height, zBlock);
            }
        }

/*
        if (horizontal || vertical || corner) {
            sendMessages(sender, "Making border on " + args[2] + " side.");

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
        }
*/

        sendMessages(sender, "Finished generating region " + xRegion + "," + zRegion + " for world " + worldName);
        return true;
    }

    /**
     * Send and log a message.
     *
     * @param sender source of the command
     * @param message string message to send/log
     */
    private void sendMessages(CommandSender sender, String message) {
        // if a player, then make sure to log it
        if (player != null) {
            LogUtil.info("Player " + player + " is " + message);
        }

        // if the console, then it will get this
        sender.sendMessage(message);
    }
}
