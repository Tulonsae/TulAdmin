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
public class GenMap implements CommandExecutor {

    private TulAdmin plugin;

    private int chunkSize = 16;
    private int numChunks = 32;
    private int regionSize = numChunks * chunkSize;

    private Player player = null;
    private World world = null;

    /**
     * Generate a region or series of regions.
     *
     * @param plugin this plugin object
     */
    public GenMap(TulAdmin plugin) {
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

        if (args.length == 0) {
            return false;
        }

        CommandData data = new CommandData(plugin);
        if (!data.parseArgs(args)) {
            return false;
        } else if (data.chunk) {
            Util.sendMessage(sender, "Chunk coordinates not implemented yet.");
            return true;
        } else if (data.block) {
            Util.sendMessage(sender, "Block coordinates not implemented yet.");
            return true;
        }

        world = plugin.getServer().getWorld(data.worldName);
        if (world == null) {
            Util.sendMessage(sender, "World " + data.worldName + " does not exist.");
            return true;
        }

        Util.sendMessage(sender, "Saving world " + data.worldName + "...");
        world.save();
        Util.sendMessage(sender, "      ...done.");

        Util.sendMessage(sender, "Starting to generate land for world " + data.worldName + " at " + data.coordType + " positions: " + data.xPosStart + "," + data.zPosStart + " thru " + data.xPosEnd + "," + data.zPosEnd);

        if(data.region) {
           for(int xr = data.xPosStart; xr <= data.xPosEnd; ++xr) {
              for(int zr = data.zPosStart; zr <= data.zPosEnd; ++zr) {
                 sender.sendMessage("Region " + xr + "," + zr);
                 int xChunk = xr * numChunks;
                 int zChunk = zr * numChunks;

                 for(int xc = xChunk; xc < xChunk + numChunks; ++xc) {
                    for(int zc = zChunk; zc < zChunk + numChunks; ++zc) {
                       sender.sendMessage("Chunk " + xc + "," + zc);
                       world.loadChunk(xc, zc);
                       world.unloadChunk(xc, zc);
                    }
                 }
                 Util.sendMessage(sender, "Saving world...");
                 world.save();
                 Util.sendMessage(sender, "      ...done.");
              }
           }
        }

        Util.sendMessage(sender, "Finished generating land for world " + data.worldName + " at " + data.coordType + " positions: " + data.xPosStart + "," + data.zPosStart + " thru " + data.xPosEnd + "," + data.zPosEnd);

        Util.sendMessage(sender, "Saving world " + data.worldName + "...");
        world.save();
        Util.sendMessage(sender, "      ...done.");

        return true;
    }

}
