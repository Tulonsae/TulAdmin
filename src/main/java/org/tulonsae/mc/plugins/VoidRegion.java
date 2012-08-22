package org.tulonsae.mc.plugins;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.World;

/**
 * Creates a void region with optional bedrock border.
 *
 * @author Tulonsae
 */
public class VoidRegion implements CommandExecutor {

    private int chunkSize = 16;
    private int numChunks = 32;
    private int regionSize = numChunks * chunkSize;

    // make these configurable
    private int height = 130;
    private int wallHeight = 63;

    private Player player = null;
    private World world = null;

    public VoidRegion(TulAdmin plugin) {
    }

    /**
     * Called when the command is sent.
     */
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        // must have x and y defined
        if (args.length < 2) {
            return false;
        }

        // check for console or player
        if (sender instanceof Player) {
            player = (Player) sender;
            world = player.getWorld();
        }

        int xRegion = Integer.parseInt(args[0]);
        int zRegion = Integer.parseInt(args[1]);

        int xChunk = xRegion * numChunks;
        int zChunk = zRegion * numChunks;

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


        sendMessages(sender, "Starting to make void region " + xRegion + "," + zRegion);

        for (int xc = xChunk; xc < (xChunk + numChunks); xc++) {
            for (int zc = zChunk; zc < (zChunk + numChunks); zc++) {
                // this message is to reassure the sender so don't log it
                sender.sendMessage("Chunk " + xc + "," + zc);
                int xBlock = xc * chunkSize;
                int zBlock = zc * chunkSize;

                for (int x = xBlock; x < (xBlock + chunkSize); x++) {
                    for (int z = zBlock; z < (zBlock + chunkSize); z++) {
                        for (int y = 0; y < height; y++) {
                            world.getBlockAt(x, y, z).setTypeId(0);;
                        }
                    }
                }
            }
        }

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

        sendMessages(sender, "Finished making void region " + xRegion + "," + zRegion);
        return true;
    }

    /**
     * Send and log a message.
     *
     * @param message string message to send.
     */
    private void sendMessages(CommandSender sender, String message) {
        // if a player, then make sure to log it
        if (player != null) {
            LogUtil.info("Player " + player + "is " + message);
        }

        // if the console, then it will get this
        sender.sendMessage(message);
    }
}
