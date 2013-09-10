package org.tulonsae.mc.plugins;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.World;

/**
 * Generates a map area or series of areas.
 *
 * @author Tulonsae
 */
public class GenMap implements CommandExecutor {

    private TulAdmin plugin;

    private static int chunkSize = 16;
    private static int numChunks = 32;
    private static int areaSize = 512;

    private Player player = null;
    private World world = null;
    private int taskId;
    private boolean taskExists = false;

    /**
     * Generate a map area.
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

        // convert coords to chunks
        int xStartChunk = 0;
        int zStartChunk = 0;
        int xEndChunk = 0;
        int zEndChunk = 0;
        if (data.region) {
            xStartChunk = data.xPosStart * numChunks;
            zStartChunk = data.zPosStart * numChunks;
            // staring pos + ending chunk of starting region + number of chunks
            // in additional regions
            xEndChunk = xStartChunk + (numChunks - 1) + ((data.xPosEnd - data.xPosStart) * numChunks);
            zEndChunk = zStartChunk + (numChunks - 1) + ((data.zPosEnd - data.zPosStart) * numChunks);
        }
        if (data.block) {
            xStartChunk = (int) Math.floor(data.xPosStart / chunkSize);
            zStartChunk = (int) Math.floor(data.zPosStart / chunkSize);
            xEndChunk = (int) Math.floor(data.xPosEnd / chunkSize);
            zEndChunk = (int) Math.floor(data.zPosEnd / chunkSize);
        }

        // schedule bukkit tasks to generate chunks
        GenChunksTask genChunksTask = new GenChunksTask(this, sender, world, xStartChunk, zStartChunk, xEndChunk, zEndChunk, areaSize);
        taskId = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, genChunksTask, 10L, 10L);
        if (taskId == -1) {
            Util.sendMessage(sender, "Could not schedule Bukkit task to generate chunks.");
            return true;
        }
        taskExists = true;

/*
        // generate chunks
        int saveCnt = 0;
        int saveAt = regionChunkArea;
        for (int xc = xStartChunk; xc <= xEndChunk; ++xc) {
            for (int zc = zStartChunk; zc <= zEndChunk; ++zc) {
                sender.sendMessage("Chunk " + xc + "," + zc);
                world.loadChunk(xc, zc);
                world.unloadChunk(xc, zc);
                if (++saveCnt < saveAt) {
                   continue;
                }
                Util.sendMessage(sender, "Saving world...");
                world.save();
                Util.sendMessage(sender, "      ...done.");
                saveCnt = 0;
            }
        }
*/

/*
        // generate regions
        if (data.region) {
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
*/

        Util.sendMessage(sender, "Finished generating land for world " + data.worldName + " at " + data.coordType + " positions: " + data.xPosStart + "," + data.zPosStart + " thru " + data.xPosEnd + "," + data.zPosEnd);

        Util.sendMessage(sender, "Saving world " + data.worldName + "...");
        world.save();
        Util.sendMessage(sender, "      ...done.");

        return true;
    }

    /**
     * Cancels repeating task to generate chunks.
     */
    void cancelTask() {
        if (taskExists) {
            plugin.getServer().getScheduler().cancelTask(taskId);
        }
    }

}
